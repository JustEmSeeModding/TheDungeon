package net.emsee.thedungeon.dungeon;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.damageType.ModDamageTypes;
import net.emsee.thedungeon.dungeon.types.Dungeon;
import net.emsee.thedungeon.dungeon.util.DungeonRank;
import net.emsee.thedungeon.events.ModDungeonDimensionEvents;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

public final class GlobalDungeonManager {
    private static final int forceLoadedChunkRadius = 30;
    private static final int killRadius = 500;

    final static Map<Dungeon, Integer> dungeons = new HashMap<>();
    private final static ResourceKey<Level> dungeonResourceKey = ModDimensions.DUNGEON_LEVEL_KEY;


    public static void Tick(ServerTickEvent.Pre event) {
        MinecraftServer server = event.getServer();
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        saveData.setTickInterval(GameruleRegistry.getIntegerGamerule(server, ModGamerules.TICKS_BETWEEN_COLLAPSES));
        if (!GameruleRegistry.getBooleanGamerule(server, ModGamerules.MANUAL_STEPPING)) {
            generationTick(server, saveData);
        }
        generationTimerTick(server, saveData);
    }

    /**
     * Send Generation tick to the current dungeon
     */
    public static void sendManualGenerationTick(MinecraftServer server) {
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.MANUAL_STEPPING)) {
            DungeonSaveData saveData = DungeonSaveData.Get(server);
            generationTick(server, saveData);
        }
    }

    /**
     * handles generation of current dungeon
     */
    private static void generationTick(MinecraftServer server, DungeonSaveData saveData) {
        if (saveData.isProgressQueueEmpty()) return;
        Dungeon currentDungeon = GetCurrentProgressDungeon(server);

        if (currentDungeon==null) return;

        ServerLevel dungeonDimension = server.getLevel(dungeonResourceKey);

        //check if the current queued dungeon is idle but not done and start it
        if (!currentDungeon.isDoneGenerating() && !currentDungeon.isBusyGenerating()) {
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"updating forced chunks");
            saveData.setDungeonOpen(currentDungeon.getRank(), false);
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"removing all portals");
            cleanAllPortals(server, saveData, currentDungeon.getRank());
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"killing all entities");
            KillAllInDungeon(server, currentDungeon.getRank());
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"starting generation");
            currentDungeon.generate(dungeonDimension, currentDungeon.getCenterPos());
        }

        currentDungeon.generationTick(dungeonDimension);
        if (currentDungeon.isDoneGenerating())
            saveData.removeFromProgressQueue();
    }

    /**
     * handles the timer and selecting a new dungeon
     */
    public static void generationTimerTick(MinecraftServer server, DungeonSaveData saveData) {
        long worldTime = server.overworld().getGameTime();

        if (!GameruleRegistry.getBooleanGamerule(server, ModGamerules.AUTO_DUNGEON_CYCLING)) {
            saveData.SetLastExecutionTime(worldTime);
            return;
        }

        long timeLeft = -((worldTime - saveData.GetLastExecutionTime()) - saveData.getTickInterval());

        long lastExecutionTime = saveData.GetLastExecutionTime();

        announceChatDecayTime(timeLeft, saveData, server);
        // if timeLeft is less than 0 select a new dungeon and generate it.
        if ((timeLeft <= 0 || worldTime < lastExecutionTime)) {
            CloseDungeon(saveData, saveData.getNextToCollapse());
            saveData.clearPortalPositions(saveData.getNextToCollapse());
            SelectNewProgressDungeon(server, false, saveData.getNextToCollapse());
            saveData.SelectNextForCollapse();
            saveData.SetLastExecutionTime(worldTime);

            saveData.SetLastMinuteAnnouncement(-1);
            saveData.SetLastSecondAnnouncement(-1);
        }

    }

    private static void announceChatDecayTime(long timeLeft, DungeonSaveData saveData, MinecraftServer server) {
        long secondsLeft = (long) Math.ceil(timeLeft / (20f));
        long minutesLeft = (long) Math.ceil(secondsLeft / (60f));

        if (minutesLeft>2) return;

        Level dungeonDimension = server.getLevel(dungeonResourceKey);

        if (saveData.getNextToCollapse() == null) return;

        if (minutesLeft <= 1) {
            if ((secondsLeft <= 10 || secondsLeft % 10 == 0) && (secondsLeft < saveData.GetLasSecondAnnouncement() || saveData.GetLasSecondAnnouncement() == -1)) {
                if (secondsLeft<=5)
                    SendMessageToPlayers(server, Component.translatable("announcement.thedungeon.seconds_left", saveData.getNextToCollapse().getName(), secondsLeft).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE));
                else
                    SendMessageToPlayersInLevel(server, Component.translatable("announcement.thedungeon.seconds_left", saveData.getNextToCollapse().getName(), secondsLeft).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE), dungeonDimension);
                saveData.SetLastSecondAnnouncement(secondsLeft);
                DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"{}-rank Ticks until next dungeon:{}", saveData.getNextToCollapse().getName(), timeLeft);
            }
        } else if ((minutesLeft <= saveData.GetLastMinuteAnnouncement() - 1) || saveData.GetLastMinuteAnnouncement() == -1) {
                SendMessageToPlayersInLevel(server, Component.translatable("announcement.thedungeon.minutes_left", saveData.getNextToCollapse().getName(), minutesLeft).withStyle(ChatFormatting.GOLD), dungeonDimension);
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"{}-rank Ticks until next dungeon:{}", saveData.getNextToCollapse().getName(), timeLeft);
            saveData.SetLastMinuteAnnouncement(minutesLeft);
        }
    }

    public static Dungeon GetCurrentProgressDungeon(MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        return saveData.peekProgressQueue();
    }

    static private void SelectNewProgressDungeon(MinecraftServer server, boolean skipPassiveQueue, DungeonRank rank) {
        progressQueueNULLCheck(server);
        passiveQueueNULLCheck(server, rank);
        if (dungeons.isEmpty()) return;
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_CLEAN_ON_REGEN))
            cleanup(server, rank);
        DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"Selecting new dungeon");
        if (!skipPassiveQueue && !saveData.isPassiveQueueEmpty(rank)) {
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"Dungeon found in passive queue");
            saveData.addToProgressQueue(saveData.removeFromPassiveQueue(rank));
        }
        else {
            Map<Dungeon,Integer> possibleDungeons = new HashMap<>();
            //DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"All Dungeons: {}", ListAndArrayUtils.mapToString(dungeons));
            for (Dungeon dungeon : dungeons.keySet())
                if (dungeon.getRank()==rank && dungeons.get(dungeon)>0)
                    possibleDungeons.put(dungeon, dungeons.get(dungeon));
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"Possible Dungeons: {}", ListAndArrayUtils.mapToString(possibleDungeons));
            if (possibleDungeons.isEmpty()) {
                DebugLog.logWarn(DebugLog.DebugLevel.WARNINGS,"Rank: {} has no dungeons assigned", rank.toString());
                return;
            }
            Dungeon newDungeon = ListAndArrayUtils.getRandomFromWeightedMapI(possibleDungeons, Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getRandom());
            if (newDungeon== null)
                throw new IllegalStateException("no new dungeon found");
            saveData.addToProgressQueue(newDungeon.getCopy());
        }
    }

    public static void KillAllInDungeon(MinecraftServer server, DungeonRank rank) {
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_KILL_ON_REGEN)) {
            ServerLevel dimension = server.getLevel(dungeonResourceKey);

            BlockPos blockPos = rank.getDefaultCenterPos();

            double centerX = blockPos.getX() + 0.5;
            double centerY = blockPos.getY() + 0.5;
            double centerZ = blockPos.getZ() + 0.5;

            AABB area = new AABB(
                    centerX - killRadius, centerY - 150, centerZ - killRadius, // Min corner
                    centerX + killRadius, centerY + 200, centerZ + killRadius  // Max corner
            );

            assert dimension != null;
            List<Entity> entities = dimension.getEntitiesOfClass(Entity.class, area, e -> true);


            for (Entity entity : entities) {
                if (entity != null) {
                    if (entity instanceof ServerPlayer player) {
                        if ((!player.isCreative()) && (player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR)) {
                            player.hurt(dimension.damageSources().source(ModDamageTypes.DUNGEON_RESET), player.getHealth());
                            ModDungeonDimensionEvents.setPlayerGameMode(player, true);
                            if (!player.isDeadOrDying()) player.kill();
                        }
                    } else
                        entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }
    /*
    todo rework
     needs to be made rank dependent somehow
    */
    public static void viewTime(Player player, MinecraftServer server) {
        /*if(!GameruleRegistry.getBooleanGamerule(server, ModGamerules.AUTO_DUNGEON_CYCLING)){
            player.sendSystemMessage(Component.translatable("message.thedungeon.cycling_disabled"));
        }
        long worldTime = server.overworld().getGameTime();
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (!saveData.isDungeonOpen()) {
            player.sendSystemMessage(Component.translatable("message.thedungeon.view_time_closed"));
            return;
        }
        long timeLeft = -((worldTime - saveData.GetLastExecutionTime()) - saveData.getTickInterval());
        long secondsLeft = (long) Math.ceil(timeLeft / (20f));
        long minutesLeft = (long) Math.floor(secondsLeft / (60f));

        if (secondsLeft <= 60) {
            player.sendSystemMessage(Component.translatable("message.thedungeon.view_time_seconds", secondsLeft));
        } else {
            player.sendSystemMessage(Component.translatable("message.thedungeon.view_time_minutes", minutesLeft, secondsLeft - minutesLeft * 60));
        }*/

        // temp code :
        player.sendSystemMessage(Component.literal("this feature is WIP"));
    }
    public static void CloseDungeon (MinecraftServer server, DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        CloseDungeon(saveData, rank);
    }

    public static void CloseDungeon (DungeonSaveData saveData, DungeonRank rank) {
        if (rank==null) return;
        saveData.setDungeonOpen(rank,false);
    }

    public static void OpenDungeon(MinecraftServer server, Dungeon dungeon, boolean isUtilDungeon) {
        if (!isUtilDungeon) SendMessageToPlayers(server, Component.translatable("announcement.thedungeon.open_dungeon").withStyle(ChatFormatting.GOLD));
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (!isUtilDungeon) saveData.setDungeonOpen(dungeon.getRank(),true);
    }

    private static void cleanAllPortals(MinecraftServer server, DungeonSaveData saveData, DungeonRank rank) {
        ServerLevel dungeonDimension = server.getLevel(dungeonResourceKey);
        List<BlockPos> posList = saveData.getAllPortalPositions(rank);
        DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS, "cleaning {} portals", posList.size());
        if (dungeonDimension==null) throw new IllegalStateException("dimension not found");
        for (BlockPos pos : posList) {
            dungeonDimension.destroyBlock(pos, false);
        }
        saveData.clearPortalPositions(rank);
    }

    private static void SendMessageToPlayers(MinecraftServer server, Component component) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(component);
        }
    }

    private static void SendMessageToPlayersInLevel(MinecraftServer server, Component component, Level level) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.level()==level)
                player.sendSystemMessage(component);
        }
    }

    /**
     * removes any NULL from the queue
     */
    private static void progressQueueNULLCheck(MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        Dungeon posOne = GetCurrentProgressDungeon(server);
        if (saveData.isProgressQueueEmpty()) return;
        while (posOne == null) {
            saveData.removeFromProgressQueue();
            if (saveData.isProgressQueueEmpty()) return;
            posOne = GetCurrentProgressDungeon(server);
        }
    }

    /**
     * removes any NULL from the queue
     */
    private static void passiveQueueNULLCheck(MinecraftServer server, DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        Dungeon posOne = saveData.peekPassiveQueue(rank);
        if (saveData.isPassiveQueueEmpty(rank)) return;
        while (posOne == null) {
            saveData.removeFromPassiveQueue(rank);
            if (saveData.isPassiveQueueEmpty(rank)) return;
            posOne = saveData.peekPassiveQueue(rank);
        }
    }

    /****
     * Inserts a dungeon at the front of the progress queue, preserving the order of existing dungeons.
     */
    public static void AddToQueueFront(Dungeon dungeon, MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);

        List<Dungeon> tempList = new ArrayList<>();
        while (!saveData.isProgressQueueEmpty()) {
            tempList.add(saveData.removeFromProgressQueue());
        }
        saveData.addToProgressQueue(dungeon);
        saveData.addAllToProgressQueue(tempList);
    }

    /**
     * called by the DungeonDebugTool
     */
    public static void GenerateDungeonFromTool(MinecraftServer server, int selectedDungeonID) {

        Dungeon newDungeon = getDungeonByID(selectedDungeonID).getCopy();

        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_CLEAN_ON_REGEN))
            cleanup(server, newDungeon.getRank());
        saveData.addToProgressQueue(newDungeon);
    }

    /**
     * returns true if the portals are open
     */
    public static boolean isOpen(DungeonSaveData saveData, DungeonRank rank) {
        return saveData.isDungeonOpen(rank);
    }

    /**
     * returns true if the portals are open
     */
    public static boolean isOpen(MinecraftServer server, DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        return isOpen(saveData,rank);
    }

    /**
     * Retrieves the portal position linked to the ID, if no portals ar present defaults to the rank center pos
     */
    public static BlockPos getPortalPosition(MinecraftServer server, int portalID, DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (portalID >= saveData.portalPositionAmount(rank) || portalID < 0) {
            return rank.getDefaultCenterPos();
        } else
            return saveData.getPortalPosition(portalID, rank);
    }

    public static int giveRandomPortalID(MinecraftServer server, DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (saveData.portalPositionsEmpty(rank)) return -1;
        return new Random().nextInt(saveData.portalPositionAmount(rank));
    }

    public static void addPortalLocation(MinecraftServer server, BlockPos pos, DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        saveData.addPortalPosition(pos, rank);
    }

    public static void removePortalLocation(MinecraftServer server, BlockPos pos, DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        saveData.removePortalPosition(pos, rank);
    }

    public static void updateForcedChunks(MinecraftServer server) {
        if (server == null) {
            DebugLog.logError(DebugLog.DebugLevel.WARNINGS, "UpdateForcedChunks: server is null");
            return;
        }
        ServerLevel level = server.getLevel(dungeonResourceKey);
        if (level == null) {
            DebugLog.logError(DebugLog.DebugLevel.WARNINGS, "UpdateForcedChunks: level is null");
            return;
        }
        for (DungeonRank rank : DungeonRank.values()) {
            BlockPos center = rank.getDefaultCenterPos();
            ChunkPos chunkPos = level.getChunkAt(center).getPos();

            for (int x = -forceLoadedChunkRadius; x < forceLoadedChunkRadius; x++) {
                for (int z = -forceLoadedChunkRadius; z < forceLoadedChunkRadius; z++) {
                    ChunkPos forcedChunkPos = new ChunkPos(chunkPos.x + x, chunkPos.z + z);
                    level.getChunk(forcedChunkPos.x, forcedChunkPos.z, ChunkStatus.FULL, true);
                    level.setChunkForced(forcedChunkPos.x, forcedChunkPos.z, true);
                    DebugLog.logInfo(DebugLog.DebugLevel.FORCED_CHUNK_UPDATES, "UpdateForcedChunks: Updated chunk force status at: {},{}", forcedChunkPos.x, forcedChunkPos.z);
                }
            }
        }
    }

    public static Dungeon getDungeonByID(int ID) {
        for (Dungeon dungeon: dungeons.keySet()) {
            if (dungeon.getID() == ID) return dungeon;
        }
        return null;
    }

    public static int getDungeonCount() {
        return dungeons.size();
    }

    /**
     * generates this dungeon the next time this rank collapses, if this rank ir already occupied it goes in the queue for the next collapse after etc.
     */
    public static void addToPassiveQueue(Dungeon dungeon, MinecraftServer server) {
        if (dungeon==null) return;
        DungeonSaveData.Get(server).addToPassiveQueue(dungeon);
    }

    /**
     * spawns a cleanup dungeon at the end of the queue
     */
    public static void cleanup(MinecraftServer server, DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        Dungeon cleanup = null;
        switch (rank) {
            case F -> cleanup= ModCleanupDungeons.CLEANUP_F;
            case E -> cleanup=ModCleanupDungeons.CLEANUP_E;
            case D -> cleanup=ModCleanupDungeons.CLEANUP_D;
            case C -> cleanup=ModCleanupDungeons.CLEANUP_C;
            case B -> cleanup=ModCleanupDungeons.CLEANUP_B;
            case A -> cleanup=ModCleanupDungeons.CLEANUP_A;
            case S -> cleanup=ModCleanupDungeons.CLEANUP_S;
            case SS -> cleanup=ModCleanupDungeons.CLEANUP_SS;
        }
        saveData.addToProgressQueue(cleanup.getCopy());
        GlobalDungeonManager.KillAllInDungeon(server, rank);
    }

    /**
     * spawns a cleanup dungeon at the beginning of the queue
     */
    public static void priorityCleanup(MinecraftServer server, DungeonRank rank) {
        Dungeon cleanup = null;
        switch (rank) {
            case F -> cleanup=ModCleanupDungeons.CLEANUP_F;
            case E -> cleanup=ModCleanupDungeons.CLEANUP_E;
            case D -> cleanup=ModCleanupDungeons.CLEANUP_D;
            case C -> cleanup=ModCleanupDungeons.CLEANUP_C;
            case B -> cleanup=ModCleanupDungeons.CLEANUP_B;
            case A -> cleanup=ModCleanupDungeons.CLEANUP_A;
            case S -> cleanup=ModCleanupDungeons.CLEANUP_S;
            case SS -> cleanup=ModCleanupDungeons.CLEANUP_SS;
        }

        if (cleanup==null)
            throw new IllegalStateException("cleanup was NULL");

        GlobalDungeonManager.AddToQueueFront(cleanup.getCopy(), server);
        GlobalDungeonManager.KillAllInDungeon(server, rank);
    }
}

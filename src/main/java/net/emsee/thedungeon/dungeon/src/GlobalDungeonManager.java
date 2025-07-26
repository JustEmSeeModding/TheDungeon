package net.emsee.thedungeon.dungeon.src;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.damageType.ModDamageTypes;
import net.emsee.thedungeon.dungeon.registry.ModCleanupDungeons;
import net.emsee.thedungeon.dungeon.registry.ModDungeons;
import net.emsee.thedungeon.dungeon.src.types.Dungeon;
import net.emsee.thedungeon.utils.ModDungeonTeleportHandling;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.utils.WeightedMap;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

public final class GlobalDungeonManager {
    private static final int forceLoadedChunkRadius = 35;
    private static final int killRadius = 500;

    private final static Map<DungeonRank,WeightedMap.Int<Dungeon>> cycleDungeons = Util.make(new HashMap<>(), map -> {for (DungeonRank rank : DungeonRank.values()) map.put(rank, new WeightedMap.Int<>());});
    private final static ResourceKey<Level> dungeonResourceKey = ModDimensions.DUNGEON_LEVEL_KEY;

    public static void registerToAutoGenerator(Dungeon dungeon, Integer weight) {
        cycleDungeons.get(dungeon.getRank()).put(dungeon, weight);
    }

    public static void tick(ServerTickEvent.Pre event) {
        MinecraftServer server = event.getServer();
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (!(GameruleRegistry.getBooleanGamerule(server, ModGamerules.MANUAL_STEPPING) && getCurrentProgressDungeon(server).canManualStepNow())) {
            generationTick(server, saveData);
        }
        generationTimerTick(server, saveData);
    }

    /**
     * Send Generation tick to the current dungeon
     */
    public static void sendManualGenerationTick(MinecraftServer server) {
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.MANUAL_STEPPING) && getCurrentProgressDungeon(server).canManualStepNow()) {
            DungeonSaveData saveData = DungeonSaveData.Get(server);
            generationTick(server, saveData);
        }
    }

    /**
     * handles generation of current dungeon
     */
    private static void generationTick(MinecraftServer server, DungeonSaveData saveData) {
        if (saveData.isProgressQueueEmpty()) return;
        Dungeon currentDungeon = getCurrentProgressDungeon(server);

        if (currentDungeon==null) return;

        ServerLevel dungeonDimension = server.getLevel(dungeonResourceKey);

        //check if the current queued dungeon is idle but not done and start it
        if (!currentDungeon.isDoneGenerating() && !currentDungeon.isBusyGenerating()) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"updating forced chunks");
            saveData.setDungeonOpen(currentDungeon.getRank(), false);
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"removing all portals");
            cleanAllPortals(server, saveData, currentDungeon.getRank());
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"killing all entities");
            killAllInDungeon(server, currentDungeon.getRank());
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"starting generation");
            assert dungeonDimension != null;
            currentDungeon.generate(dungeonDimension);
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

        long timeLeft = -((worldTime - saveData.GetLastExecutionTime()) - GameruleRegistry.getIntegerGamerule(server,ModGamerules.TICKS_BETWEEN_COLLAPSES));

        long lastExecutionTime = saveData.GetLastExecutionTime();

        announceChatDecayTime(timeLeft, saveData, server);
        // if timeLeft is less than 0 select a new dungeon and generate it.
        if ((timeLeft <= 0 || worldTime < lastExecutionTime)) {
            closeDungeon(saveData, saveData.getNextToCollapse());
            saveData.clearPortalPositions(saveData.getNextToCollapse());
            selectNewProgressDungeon(server, false, saveData.getNextToCollapse());
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
                    sendMessageToPlayers(server, Component.translatable("announcement.thedungeon.seconds_left", saveData.getNextToCollapse().getName(), secondsLeft).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE));
                else
                    sendMessageToPlayersInLevel(server, Component.translatable("announcement.thedungeon.seconds_left", saveData.getNextToCollapse().getName(), secondsLeft).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE), dungeonDimension);
                saveData.SetLastSecondAnnouncement(secondsLeft);
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"{}-rank Ticks until next dungeon:{}", saveData.getNextToCollapse().getName(), timeLeft);
            }
        } else if ((minutesLeft <= saveData.GetLastMinuteAnnouncement() - 1) || saveData.GetLastMinuteAnnouncement() == -1) {
                sendMessageToPlayersInLevel(server, Component.translatable("announcement.thedungeon.minutes_left", saveData.getNextToCollapse().getName(), minutesLeft).withStyle(ChatFormatting.GOLD), dungeonDimension);
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"{}-rank Ticks until next dungeon:{}", saveData.getNextToCollapse().getName(), timeLeft);
            saveData.SetLastMinuteAnnouncement(minutesLeft);
        }
    }

    public static Dungeon getCurrentProgressDungeon(MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        return saveData.peekProgressQueue();
    }

    static private void selectNewProgressDungeon(MinecraftServer server, boolean skipPassiveQueue, DungeonRank rank) {
        progressQueueNULLCheck(server);
        passiveQueueNULLCheck(server, rank);
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_CLEAN_ON_REGEN))
            cleanup(server, rank);
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Selecting new dungeon");
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Checking passive queue");
        if (!skipPassiveQueue && !saveData.isPassiveQueueEmpty(rank)) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Dungeon found in passive queue");
            saveData.addToProgressQueue(saveData.removeFromPassiveQueue(rank));
        }
        else {
            WeightedMap.Int<Dungeon> possibleDungeons = cycleDungeons.get(rank);
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"No dungeon in passive queue, selecting random");
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Possible Dungeons: {}", ListAndArrayUtils.mapToString(possibleDungeons));
            if (possibleDungeons.isEmpty()) {
                DebugLog.logWarn(DebugLog.DebugType.WARNINGS,"Rank: {} has no dungeons assigned", rank.toString());
                return;
            }
            ServerLevel overworld = server.getLevel(Level.OVERWORLD);
            if (overworld == null) throw new IllegalStateException("overworld not found");
            Dungeon newDungeon = possibleDungeons.getRandom(overworld.getRandom());
            if (newDungeon== null) throw new IllegalStateException("error with selecting dungeon");
            saveData.addToProgressQueue(newDungeon.getCopy());
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "added {} to queue", newDungeon);
        }
    }

    public static void killAllInDungeon(MinecraftServer server, DungeonRank rank) {
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
                            ModDungeonTeleportHandling.setPlayerGameMode(player, true);
                            if (!player.isDeadOrDying()) player.kill();
                        }
                    } else
                        entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    public static void closeDungeon(MinecraftServer server, DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        closeDungeon(saveData, rank);
    }

    public static void closeDungeon(DungeonSaveData saveData, DungeonRank rank) {
        if (rank==null) return;
        saveData.setDungeonOpen(rank,false);
    }

    public static void openDungeon(MinecraftServer server, Dungeon dungeon, boolean isUtilDungeon) {
        if (!isUtilDungeon) sendMessageToPlayers(server, Component.translatable("announcement.thedungeon.open_dungeon").withStyle(ChatFormatting.GOLD));
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (!isUtilDungeon) saveData.setDungeonOpen(dungeon.getRank(),true);
    }

    private static void cleanAllPortals(MinecraftServer server, DungeonSaveData saveData, DungeonRank rank) {
        ServerLevel dungeonDimension = server.getLevel(dungeonResourceKey);
        List<BlockPos> posList = saveData.getAllPortalPositions(rank);
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "cleaning {} portals", posList.size());
        if (dungeonDimension==null) throw new IllegalStateException("dimension not found");
        for (BlockPos pos : posList) {
            dungeonDimension.destroyBlock(pos, false);
        }
        saveData.clearPortalPositions(rank);
    }

    private static void sendMessageToPlayers(MinecraftServer server, Component component) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(component);
        }
    }

    private static void sendMessageToPlayersInLevel(MinecraftServer server, Component component, Level level) {
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
        Dungeon posOne = getCurrentProgressDungeon(server);
        if (saveData.isProgressQueueEmpty()) return;
        while (posOne == null) {
            saveData.removeFromProgressQueue();
            if (saveData.isProgressQueueEmpty()) return;
            posOne = getCurrentProgressDungeon(server);
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
    public static void addToQueueFront(Dungeon dungeon, MinecraftServer server) {
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
    public static void generateDungeonFromTool(MinecraftServer server, int selectedDungeonID) {

        Dungeon newDungeon = Objects.requireNonNull(getDungeonByID(selectedDungeonID)).getCopy();

        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_CLEAN_ON_REGEN))
            cleanup(server, newDungeon.getRank());
        saveData.addToProgressQueue(newDungeon);
    }

    /**
     * returns true if actively generating any dungeon.
     */
    public static boolean isGenerating(MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        return !saveData.isProgressQueueEmpty();
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
        ServerLevel level = server.getLevel(dungeonResourceKey);
        if (level == null) {
            DebugLog.logError(DebugLog.DebugType.WARNINGS, "UpdateForcedChunks: level is null");
            return;
        }
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (saveData.isFinishedForcedChunks()) return;
        for (DungeonRank rank : DungeonRank.values()) {
            DebugLog.logInfo(DebugLog.DebugType.FORCED_CHUNK_UPDATES, "UpdateForcedChunks: Starting rank: {}", rank.getName());
            BlockPos center = rank.getDefaultCenterPos();
            //ChunkPos chunkPos = level.getChunkAt(center).getPos();
            ChunkPos chunkPos = new ChunkPos(SectionPos.blockToSectionCoord(center.getX()), SectionPos.blockToSectionCoord(center.getZ()));

            for (int x = -forceLoadedChunkRadius; x < forceLoadedChunkRadius; x++) {
                for (int z = -forceLoadedChunkRadius; z < forceLoadedChunkRadius; z++) {
                    ChunkPos forcedChunkPos = new ChunkPos(chunkPos.x + x, chunkPos.z + z);
                    DebugLog.logInfo(DebugLog.DebugType.FORCED_CHUNK_UPDATES_DETAILS, "UpdateForcedChunks: Loading at: {},{}", forcedChunkPos.x, forcedChunkPos.z);
                    level.getChunk(forcedChunkPos.x, forcedChunkPos.z, ChunkStatus.FULL, true);
                    DebugLog.logInfo(DebugLog.DebugType.FORCED_CHUNK_UPDATES_DETAILS, "UpdateForcedChunks: Adding at: {},{} to list", forcedChunkPos.x, forcedChunkPos.z);
                    level.setChunkForced(forcedChunkPos.x, forcedChunkPos.z, true);
                    DebugLog.logInfo(DebugLog.DebugType.FORCED_CHUNK_UPDATES_DETAILS, "UpdateForcedChunks: Updated chunk force status at: {},{}", forcedChunkPos.x, forcedChunkPos.z);
                }
            }
        }
        saveData.setFinishedForcedChunks();
    }

    public static Dungeon getDungeonByID(int ID) {
        return ModDungeons.getByID(ID);
    }

    public static int getDungeonCount() {
        return ModDungeons.getMaxID();
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
        GlobalDungeonManager.killAllInDungeon(server, rank);
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

        GlobalDungeonManager.addToQueueFront(cleanup.getCopy(), server);
        GlobalDungeonManager.killAllInDungeon(server, rank);
    }
}

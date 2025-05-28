package net.emsee.thedungeon.dungeon;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.damageType.ModDamageTypes;
import net.emsee.thedungeon.dungeon.dungeon.Dungeon;
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
        progressQueueNULLCheck(server);
        //passiveQueueNULLCheck(server);
        if (saveData.isProgressQueueEmpty()) return;
        Dungeon currentDungeon = GetCurrentProgressDungeon(server);

        ServerLevel dungeonDimension = server.getLevel(dungeonResourceKey);
        //check if the current queued dungeon is idle but not done and start it
        if (!currentDungeon.IsDoneGenerating() && !currentDungeon.IsBusyGenerating()) {
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"removing all portals");
            cleanAllPortals(server, saveData, currentDungeon.getRank());
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"killing all entities");
            KillAllInDungeon(server, currentDungeon.getRank());
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"starting generation");
            currentDungeon.Generate(dungeonDimension, currentDungeon.getRank().getCenterPos());
        }

        currentDungeon.GenerationTick(dungeonDimension);
        if (currentDungeon.IsDoneGenerating())
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
            saveData.setTickInterval(GameruleRegistry.getIntegerGamerule(server, ModGamerules.TICKS_BETWEEN_COLLAPSES));
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
                if (secondsLeft<=10)
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

    static private void SelectNewProgressDungeon(MinecraftServer server, boolean skipPassiveQueue, Dungeon.DungeonRank rank) {
        if (dungeons.isEmpty()) return;
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_CLEAN_ON_REGEN))
            Dungeon.Cleanup(server, rank);
        DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"Selecting new dungeon");
        if (!skipPassiveQueue && !saveData.isPassiveQueueEmpty(rank)) {
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"Dungeon found in passive queue");
            saveData.addToProgressQueue(saveData.removeFromPassiveQueue(rank));
        }
        else {
            Map<Dungeon,Integer> possibleDungeons = new HashMap<>();
            for (Dungeon dungeon : dungeons.keySet())
                if (dungeon.getRank()==rank && dungeon.getWeight()>0)
                    possibleDungeons.put(dungeon, dungeon.getWeight());
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"Dungeons: {}", ListAndArrayUtils.mapToString(possibleDungeons));
            if (possibleDungeons.isEmpty()) {
                DebugLog.logWarn(DebugLog.DebugLevel.WARNINGS,"Rank: {} has no dungeons assigned", rank.toString());
                return;
            }
            Dungeon newDungeon = ListAndArrayUtils.getRandomFromWeightedMapI(possibleDungeons, Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getRandom());
            if (newDungeon== null)
                throw new IllegalStateException("no new dungeon found");
            saveData.addToProgressQueue(newDungeon.GetCopy());
        }
    }

    //TODO this is VERY slow
    //TODO also potentially remove forced chucks once done
    public static void KillAllInDungeon(MinecraftServer server, Dungeon.DungeonRank rank) {
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_KILL_ON_REGEN)) {
            updateForcedChunks(server, rank);
            ServerLevel dimension = server.getLevel(dungeonResourceKey);

            BlockPos blockPos = rank.getCenterPos();

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

    //todo rework
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
    }
    public static void CloseDungeon (MinecraftServer server, Dungeon.DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        CloseDungeon(saveData, rank);
    }

    public static void CloseDungeon (DungeonSaveData saveData, Dungeon.DungeonRank rank) {
        if (rank==null) return;
        saveData.setDungeonOpen(rank,false);
    }

    public static void OpenDungeon(MinecraftServer server, Dungeon dungeon, boolean isUtilDungeon) {
        if (!isUtilDungeon) SendMessageToPlayers(server, Component.translatable("announcement.thedungeon.open_dungeon").withStyle(ChatFormatting.GOLD));
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        saveData.SetLastMinuteAnnouncement(-1);
        saveData.SetLastSecondAnnouncement(-1);
        long worldTime = server.overworld().getGameTime();
        saveData.SetLastExecutionTime(worldTime);
        if (!isUtilDungeon) saveData.setDungeonOpen(dungeon.getRank(),true);
    }

    private static void cleanAllPortals(MinecraftServer server, DungeonSaveData saveData, Dungeon.DungeonRank rank) {
        ServerLevel dungeonDimension = server.getLevel(dungeonResourceKey);
        List<BlockPos> posList = saveData.getAllPortalPositions(rank);
        assert dungeonDimension != null;
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
    /*private static void passiveQueueNULLCheck(MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        Dungeon posOne = saveData.peekPassiveQueue();
        if (saveData.isPassiveQueueEmpty()) return;
        while (posOne == null) {
            saveData.removeFromPassiveQueue();
            if (saveData.isPassiveQueueEmpty()) return;
            posOne = saveData.peekPassiveQueue();
        }
    }*/

    /****
     * Inserts a dungeon at the front of the progress queue, preserving the order of existing dungeons.
     *
     * @param dungeon the dungeon to add to the front of the queue
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
     * Adds a copy of the specified dungeon to the progress queue, optionally cleaning up existing dungeons first.
     *
     * @param selectedDungeonID the ID of the dungeon to generate
     */
    public static void GenerateDungeonFromTool(MinecraftServer server, int selectedDungeonID) {
        Dungeon newDungeon = getDungeonByID(selectedDungeonID).GetCopy();

        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_CLEAN_ON_REGEN))
            Dungeon.Cleanup(server, newDungeon.getRank());
        saveData.addToProgressQueue(newDungeon);
    }

    /**
     * Determines whether the dungeon is currently open and no dungeons are in progress.
     *
     * @param saveData the dungeon save data to check
     * @return true if the dungeon is open and the progress queue is empty; false otherwise
     */
    public static boolean isOpen(DungeonSaveData saveData, Dungeon.DungeonRank rank) {
        return saveData.isProgressQueueEmpty() && saveData.isDungeonOpen(rank);
    }

    /**
     * Determines whether the dungeon is currently open and no dungeons are in the progress queue.
     *
     * @param server the Minecraft server instance
     * @return true if the dungeon is open and the progress queue is empty; false otherwise
     */
    public static boolean isOpen(MinecraftServer server, Dungeon.DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        return saveData.isProgressQueueEmpty() && saveData.isDungeonOpen(rank);
    }

    /**
     * Retrieves the portal position for the specified portal ID.
     *
     * @param portalID the ID of the portal to retrieve
     * @return the portal position if the ID is valid; otherwise, returns the dungeon center position
     */
    public static BlockPos getPortalPosition(MinecraftServer server, int portalID, Dungeon.DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (portalID >= saveData.portalPositionAmount(rank) || portalID < 0) {
            return rank.getCenterPos();
        } else
            return saveData.getPortalPosition(portalID, rank);
    }

    public static int giveRandomPortalID(MinecraftServer server, Dungeon.DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (saveData.portalPositionsEmpty(rank)) return -1;
        return new Random().nextInt(saveData.portalPositionAmount(rank));
    }

    public static void addPortalLocation(MinecraftServer server, BlockPos pos, Dungeon.DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        saveData.addPortalPosition(pos, rank);
    }

    public static void removePortalLocation(MinecraftServer server, BlockPos pos, Dungeon.DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        saveData.removePortalPosition(pos, rank);
    }

    private static void updateForcedChunks(MinecraftServer server, Dungeon.DungeonRank rank) {
        BlockPos center = rank.getCenterPos();
        ServerLevel level = server.getLevel(dungeonResourceKey);
        ChunkPos chunkPos = level.getChunkAt(center).getPos();

        for (int x = -forceLoadedChunkRadius; x<forceLoadedChunkRadius; x++) {
            for (int z = -forceLoadedChunkRadius; z<forceLoadedChunkRadius; z++) {
                ChunkPos forcedChunkPos = new ChunkPos(chunkPos.x+x, chunkPos.z+z);
                Objects.requireNonNull(level).setChunkForced(forcedChunkPos.x,forcedChunkPos.z,true);
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

    public static void AddToPassiveQueue(Dungeon dungeon, MinecraftServer server) {
        if (dungeon==null) return;
        DungeonSaveData.Get(server).addToPassiveQueue(dungeon);
    }
}

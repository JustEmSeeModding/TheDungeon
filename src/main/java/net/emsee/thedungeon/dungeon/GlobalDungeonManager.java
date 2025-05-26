package net.emsee.thedungeon.dungeon;

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
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

public final class GlobalDungeonManager {
    private static final int forceLoadedChunkRadius = 30;
    private static final BlockPos centerPos = new BlockPos(0, 150, 0);

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
        passiveQueueNULLCheck(server);
        if (saveData.isProgressQueueEmpty()) return;

        if (isOpen(saveData)) CloseDungeon(saveData);
        Dungeon currentDungeon = GetCurrentProgressDungeon(server);

        ServerLevel dungeonDimension = server.getLevel(dungeonResourceKey);
        //check if the current queued dungeon is idle but not done and start it
        if (!currentDungeon.IsDoneGenerating() && !currentDungeon.IsBusyGenerating()) {
            cleanAllPortals(server, saveData);
            KillAllInDungeon(server);
            currentDungeon.Generate(dungeonDimension, centerPos);
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

        if (!saveData.isTiming()) {
            saveData.SetLastExecutionTime(worldTime);
            if ((timeLeft <= 0 || worldTime < lastExecutionTime) && saveData.peekProgressQueue()==null)
                SelectNewProgressDungeon(server, false);
            return;
        }

        announceChatDecayTime(timeLeft, saveData, server);
        // if timeLeft is less than 0 select a new dungeon and generate it.
        if ((timeLeft <= 0 || worldTime < lastExecutionTime)) {
            CloseDungeon(saveData);
            saveData.clearPortalPositions();
            SelectNewProgressDungeon(server, false);

            saveData.SetLastMinuteAnnouncement(-1);
            saveData.SetLastSecondAnnouncement(-1);
        }

    }

    private static void announceChatDecayTime(long timeLeft, DungeonSaveData saveData, MinecraftServer server) {
        long secondsLeft = (long) Math.ceil(timeLeft / (20f));
        long minutesLeft = (long) Math.ceil(secondsLeft / (60f));

        if (minutesLeft>2) return;

        Level dungeonDimension = server.getLevel(dungeonResourceKey);

        if (minutesLeft <= 1) {
            if ((secondsLeft <= 10 || secondsLeft % 10 == 0) && (secondsLeft < saveData.GetLasSecondAnnouncement() || saveData.GetLasSecondAnnouncement() == -1)) {
                if (secondsLeft<=10)
                    SendMessageToPlayers(server, Component.translatable("announcement.thedungeon.seconds_left", secondsLeft).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE));
                else
                    SendMessageToPlayersInLevel(server, Component.translatable("announcement.thedungeon.seconds_left", secondsLeft).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE), dungeonDimension);
                saveData.SetLastSecondAnnouncement(secondsLeft);
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) TheDungeon.LOGGER.info("Ticks until next dungeon:{}", timeLeft);
            }
        } else if ((minutesLeft <= saveData.GetLastMinuteAnnouncement() - 1) || saveData.GetLastMinuteAnnouncement() == -1) {
            SendMessageToPlayersInLevel(server, Component.translatable("announcement.thedungeon.minutes_left", minutesLeft).withStyle(ChatFormatting.GOLD), dungeonDimension);
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) TheDungeon.LOGGER.info("Ticks until next dungeon:{}", timeLeft);
            saveData.SetLastMinuteAnnouncement(minutesLeft);
        }
    }

    public static Dungeon GetCurrentProgressDungeon(MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        return saveData.peekProgressQueue();
    }

    static private void SelectNewProgressDungeon(MinecraftServer server, boolean skipPassiveQueue) {
        if (dungeons.isEmpty()) return;
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_CLEAN_ON_REGEN))
            Dungeon.Cleanup(server);
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) {
            TheDungeon.LOGGER.info("Selecting new dungeon");
        }
        if (!skipPassiveQueue && !saveData.isPassiveQueueEmpty()) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) {
                TheDungeon.LOGGER.info("Dungeon found in passive queue");
            }
            saveData.addToProgressQueue(saveData.removeFromPassiveQueue());
        }
        else {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC))
                TheDungeon.LOGGER.info("Dungeons: {}", dungeons);
            saveData.addToProgressQueue(Objects.requireNonNull(ListAndArrayUtils.getRandomFromWeightedMapI(dungeons, server.getLevel(Level.OVERWORLD).getRandom())).GetCopy());
        }
    }

    public static void KillAllInDungeon(MinecraftServer server) {
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_KILL_ON_REGEN)) {
            updateForcedChunks(server);
            ServerLevel dimension = server.getLevel(dungeonResourceKey);
            List<Entity> all = new ArrayList<>();
            dimension.getAllEntities().forEach(all::add);
            for (Entity entity : all) {
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

    public static void viewTime(Player player, MinecraftServer server) {
        if(!GameruleRegistry.getBooleanGamerule(server, ModGamerules.AUTO_DUNGEON_CYCLING)){
            player.sendSystemMessage(Component.translatable("message.thedungeon.cycling_disabled"));
        }
        long worldTime = server.overworld().getGameTime();
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (!saveData.isTiming()) {
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
        }
    }
    public static void CloseDungeon (MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        CloseDungeon(saveData);
    }

    public static void CloseDungeon (DungeonSaveData saveData) {
        saveData.setDungeonOpen(false);
        saveData.setTiming(false);
    }

    public static void OpenDungeon(MinecraftServer server, Dungeon dungeon, boolean isUtilDungeon) {
        if (!isUtilDungeon) SendMessageToPlayers(server, Component.translatable("announcement.thedungeon.open_dungeon").withStyle(ChatFormatting.GOLD));
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        saveData.SetLastMinuteAnnouncement(-1);
        saveData.SetLastSecondAnnouncement(-1);
        long worldTime = server.overworld().getGameTime();
        saveData.SetLastExecutionTime(worldTime);
        if (!isUtilDungeon) saveData.setDungeonOpen(true);
        saveData.setTiming(true);
        saveData.setTickInterval(dungeon.getTickInterval());
    }

    private static void cleanAllPortals(MinecraftServer server, DungeonSaveData saveData) {
        ServerLevel dungeonDimension = server.getLevel(dungeonResourceKey);
        List<BlockPos> posList = saveData.getAllPortalPositions();
        for (BlockPos pos : posList) {
            assert dungeonDimension != null;
            dungeonDimension.destroyBlock(pos, false);
        }
        saveData.clearPortalPositions();
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
    private static void passiveQueueNULLCheck(MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        Dungeon posOne = saveData.peekPassiveQueue();
        if (saveData.isPassiveQueueEmpty()) return;
        while (posOne == null) {
            saveData.removeFromPassiveQueue();
            if (saveData.isPassiveQueueEmpty()) return;
            posOne = saveData.peekPassiveQueue();
        }
    }

    /**
     * adds to the start of the generation queue
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

    public static void GenerateDungeonFromTool(MinecraftServer server, int selectedDungeonID) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_CLEAN_ON_REGEN))
            Dungeon.Cleanup(server);
        saveData.addToProgressQueue(getDungeonByID(selectedDungeonID).GetCopy());
    }

    public static boolean isOpen(DungeonSaveData saveData) {
        return saveData.isProgressQueueEmpty() && saveData.isDungeonOpen();
    }

    public static boolean isOpen(MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        return saveData.isProgressQueueEmpty() && saveData.isDungeonOpen();
    }

    public static BlockPos getPortalPosition(MinecraftServer server, int portalID) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (portalID >= saveData.portalPositionAmount() || portalID < 0) {
            return centerPos;
        } else
            return saveData.getPortalPosition(portalID);
    }

    public static int giveRandomPortalID(MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        if (saveData.portalPositionsEmpty()) return -1;
        return new Random().nextInt(saveData.portalPositionAmount());
    }

    public static void addPortalLocation(MinecraftServer server, BlockPos pos) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        saveData.addPortalPosition(pos);
    }

    public static void removePortalLocation(MinecraftServer server, BlockPos pos) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        saveData.removePortalPosition(pos);
    }

    private static void updateForcedChunks(MinecraftServer server) {
        for (int x = -forceLoadedChunkRadius; x<forceLoadedChunkRadius; x++) {
            for (int z = -forceLoadedChunkRadius; z<forceLoadedChunkRadius; z++) {
                Objects.requireNonNull(server.getLevel(dungeonResourceKey)).setChunkForced(x,z,true);
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

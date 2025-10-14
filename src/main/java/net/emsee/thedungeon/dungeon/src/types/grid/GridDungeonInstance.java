package net.emsee.thedungeon.dungeon.src.types.grid;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.emsee.thedungeon.dungeon.src.generators.GridDungeonGenerator;
import net.emsee.thedungeon.dungeon.src.types.DungeonInstance;
import net.emsee.thedungeon.dungeon.src.types.roomCollection.GridRoomCollectionInstance;
import net.minecraft.server.level.ServerLevel;

public class GridDungeonInstance extends DungeonInstance<GridDungeon> {
    GridRoomCollectionInstance collection;
    private boolean generated;
    protected GridDungeonGenerator generator = null;

    public GridDungeonInstance(GridDungeon dungeon) {
        super(dungeon);
        collection = dungeon.getRoomCollection().createInstance();
    }

    private void reset() {
        collection = dungeon.getRoomCollection().createInstance();
    }

    @Override
    public void generateSeeded(long seed) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "Starting Dungeon Generation...");
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "Dungeon: {}", dungeon.getResourceName());
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "Seed: {}", seed);
        generator = new GridDungeonGenerator(this, seed);
        reset();
    }


    @Override
    public void generationTick(ServerLevel serverLevel) {
        if (generator != null) {
            generator.step(serverLevel);
            if (generator.isDone()) {
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "Finished Dungeon Generation");
                generator = null;
                generated = true;
                GlobalDungeonManager.openDungeon(serverLevel.getServer(), dungeon, dungeon.isUtilDungeon());
            }
        }
    }

    @Override
    public boolean isDoneGenerating() {
        return (!isBusyGenerating()) && generated;
    }

    @Override
    public boolean isBusyGenerating() {
        return generator != null;
    }

    @Override
    public boolean canManualStepNow() {
        if (generator != null)
            return generator.currentStep()==GridDungeonGenerator.GenerationTask.PLACING_ROOMS;
        return false;
    }

    public GridRoomCollectionInstance getRoomCollection() {
        return collection;
    }
}

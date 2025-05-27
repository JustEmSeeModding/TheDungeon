package net.emsee.thedungeon.dungeon.connectionRules;

import net.emsee.thedungeon.dungeon.room.GeneratedRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class FailInstance {
    final FailRule failRule;
    final GeneratedRoom generatedRoom;
    final GridRoomUtils.Connection connection;
    final boolean wouldPlaceFallback;
    final boolean exitObstructed;

    public FailInstance(FailRule failRule, GeneratedRoom generatedRoom, GridRoomUtils.Connection connection, boolean wouldPlaceFallback, boolean exitObstructed) {
        this.failRule = failRule.getCopy();
        this.generatedRoom = generatedRoom;
        this.connection = connection;
        this.wouldPlaceFallback = wouldPlaceFallback;
        this. exitObstructed = exitObstructed;
    }

    public void finalize(ServerLevel level, StructureProcessorList processors) {
        failRule.ApplyFail(generatedRoom, connection, level, processors, wouldPlaceFallback, exitObstructed);
    }

    public boolean isFinished() {
        return failRule.isFinished();
    }
}

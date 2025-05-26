package net.emsee.thedungeon.dungeon.connectionRules;

import net.emsee.thedungeon.dungeon.room.GeneratedRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class FailInstance {
    final FailRule failRule;
    final GeneratedRoom generatedRoom;
    final GridRoomUtils.Connection connection;

    public FailInstance(FailRule failRule, GeneratedRoom generatedRoom, GridRoomUtils.Connection connection) {
        this.failRule = failRule.getCopy();
        this.generatedRoom = generatedRoom;
        this.connection = connection;
    }

    public void finalize(ServerLevel level, StructureProcessorList processors) {
        failRule.ApplyFail(generatedRoom, connection, level, processors);
    }

    public boolean isFinished() {
        return failRule.isFinished();
    }
}

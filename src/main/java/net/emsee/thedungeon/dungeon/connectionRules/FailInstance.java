package net.emsee.thedungeon.dungeon.connectionRules;

import net.emsee.thedungeon.dungeon.room.GeneratedRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

/**
 * a fail instance get placed when the tag fails placing the next room through any means
 */
public class FailInstance {
    final FailRule failRule;
    final GeneratedRoom generatedRoom;
    final GridRoomUtils.Connection connection;
    final boolean wouldPlaceFallback;
    final boolean exitObstructed;

    /**
     * a fail instance get placed when the tag fails placing the next room through any means
     */
    public FailInstance(FailRule failRule, GeneratedRoom generatedRoom, GridRoomUtils.Connection connection, boolean wouldPlaceFallback, boolean exitObstructed) {
        this.failRule = failRule.getCopy();
        this.generatedRoom = generatedRoom;
        this.connection = connection;
        this.wouldPlaceFallback = wouldPlaceFallback;
        this. exitObstructed = exitObstructed;
    }

    /**
     * finalizes placement
     */
    public void finalizeTick(ServerLevel level, StructureProcessorList processors) {
        failRule.ApplyFail(generatedRoom, connection, level, processors, wouldPlaceFallback, exitObstructed);
    }

    /**
     * checks if finalization is complete
     */
    public boolean isFinished() {
        return failRule.isFinished();
    }
}

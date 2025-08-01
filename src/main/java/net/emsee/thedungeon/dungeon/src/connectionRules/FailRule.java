package net.emsee.thedungeon.dungeon.src.connectionRules;

import net.emsee.thedungeon.dungeon.src.room.GeneratedRoom;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

/**
 * a fail rule is triggered when a tag fails placing the next room through any means
 */
public abstract class FailRule {
    protected final String tag;
    protected final boolean horizontalConnection;
    protected final boolean verticalConnection;

    protected FailRule(String tag, boolean horizontalConnection, boolean verticalConnection) {
        this.tag = tag;
        this.horizontalConnection = horizontalConnection;
        this.verticalConnection = verticalConnection;
    }

    /**
     * returns true if the tag matches this rule
     */
    public final boolean match(String tag, Connection connection) {
        boolean connectionCorrect =
                (connection.isHorizontal() && horizontalConnection) ||
                (connection.isVertical() && verticalConnection);
        return connectionCorrect && tag.equals(this.tag);
    }

    /**
     * placement tick
     */
    public abstract void applyFailTick(GeneratedRoom room, Connection connection, ServerLevel level, StructureProcessorList processors, boolean wouldPlaceFallback, boolean exitObstructed);

    /**
     * returns true if this rule is finished and should no longer tick
     */
    public abstract boolean isFinished();

    /**
     * returns true if this rule should stop fallback placement
     */
    public boolean stopFallbackPlacement() {
        return false;
    }

    public abstract FailRule getCopy();

    /**
     * Instance is the placed version of a FailRule
     */
    public static class Instance {
        final FailRule failRule;
        final GeneratedRoom generatedRoom;
        final Connection connection;
        final boolean wouldPlaceFallback;
        final boolean exitObstructed;

        /**
         * a fail instance get placed when the tag fails placing the next room through any means
         */
        public Instance(FailRule failRule, GeneratedRoom generatedRoom, Connection connection, boolean wouldPlaceFallback, boolean exitObstructed) {
            this.failRule = failRule.getCopy();
            this.generatedRoom = generatedRoom;
            this.connection = connection;
            this.wouldPlaceFallback = wouldPlaceFallback;
            this.exitObstructed = exitObstructed;
        }

        /**
         * finalizes placement
         */
        public void finalizeTick(ServerLevel level, StructureProcessorList processors) {
            failRule.applyFailTick(generatedRoom, connection, level, processors, wouldPlaceFallback, exitObstructed);
        }

        /**
         * checks if finalization is complete
         */
        public boolean isFinished() {
            return failRule.isFinished();
        }
    }
}

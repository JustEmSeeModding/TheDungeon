package net.emsee.thedungeon.dungeon.connectionRules;

import net.emsee.thedungeon.dungeon.room.GeneratedRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

/**
 * a fail rule is what happens the tag fails placing the next room through any means
 */
public abstract class FailRule {
    protected final String tag;

    /**
     * a fail rule is what happens the tag fails placing the next room through any means
     */
    protected FailRule(String tag) {
        this.tag = tag;
    }

    /**
     * returns true if the tag matches this rule
     */
    public final boolean match(String tag) {
        return tag.equals(this.tag);
    }

    public abstract void ApplyFail(GeneratedRoom room, GridRoomUtils.Connection connection, ServerLevel level, StructureProcessorList processors, boolean wouldPlaceFallback, boolean exitObstructed);
    public abstract boolean isFinished();
    public boolean stopFallbackPlacement() {return false;}

    public abstract FailRule getCopy();
}

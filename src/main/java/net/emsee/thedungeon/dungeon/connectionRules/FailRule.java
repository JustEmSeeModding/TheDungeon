package net.emsee.thedungeon.dungeon.connectionRules;

import net.emsee.thedungeon.dungeon.room.GeneratedRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public abstract class FailRule {
    protected final String tag;

    public FailRule(FailRule rule) {
        tag = rule.tag;
    }

    protected FailRule(String tag) {
        this.tag = tag;
    }

    public final boolean match(String tag) {
        return tag.equals(this.tag);
    }

    public abstract void ApplyFail(GeneratedRoom room, GridRoomUtils.Connection connection, ServerLevel level, StructureProcessorList processors, boolean wouldPlaceFallback, boolean exitObstructed);
    public abstract boolean isFinished();
    public boolean stopFallbackPlacement() {return false;}

    public abstract FailRule getCopy();
}

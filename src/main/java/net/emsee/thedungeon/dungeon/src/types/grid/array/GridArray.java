package net.emsee.thedungeon.dungeon.src.types.grid.array;

import net.emsee.thedungeon.dungeon.src.types.grid.room.GeneratedRoom;
import net.minecraft.Util;
import net.minecraft.core.Vec3i;

import java.util.HashMap;
import java.util.Map;

public final class GridArray {
    private final Map<Vec3i, GridCell> array;
    private final int maxDepth;
    private final int maxFloorHeight;
    private final boolean doGenerateDown;

    public GridArray(int maxDepth, int maxFloorHeight, boolean doGenerateDown) {
        this.maxDepth = maxDepth;
        this.maxFloorHeight = maxFloorHeight;
        this.doGenerateDown = doGenerateDown;

        array = Util.make(new HashMap<>(), map -> {
            for (int x = -maxDepth; x <= maxDepth; x++) {
                for (int y = -maxDepth; y <= maxDepth; y++) {
                    for (int z = -maxDepth; z <= maxDepth; z++) {
                        map.put(new Vec3i(x, y, z), new EmptyGridCell());
                    }
                }
            }
        });
    }

    public GridCell getCellAt(Vec3i pos) {
        return array.get(pos);
    }


    /**
     * Positions outside the array default to occupied
     */
    public boolean isCellOccupiedAt(Vec3i pos) {
        if (isInsideGrid(pos, false))
            return getCellAt(pos).isOccupied();
        return true;
    }

    public boolean isCellEmptyAt(Vec3i pos) {
        return !isCellOccupiedAt(pos);
    }

    public boolean isInsideGrid(Vec3i pos, boolean checkFloorLimit) {
        return array.get(pos)!=null && (!checkFloorLimit || isInsideFloorLimit(pos));
    }

    public boolean isInsideFloorLimit(Vec3i pos) {
        if (pos.getY()<0 && !doGenerateDown)
            return false;
        else
            return !(Math.abs(pos.getY()) > maxFloorHeight);
    }

    public boolean isAtBorder(Vec3i pos, boolean checkFloorLimit) {
        return Math.abs(pos.getX()) == maxDepth
                || Math.abs(pos.getY()) == maxDepth
                || Math.abs(pos.getZ()) == maxDepth
                || (checkFloorLimit
                && ((!doGenerateDown && pos.getY() == -1)
                || Math.abs(pos.getY()) == maxFloorHeight
        ));
    }

    public GridCell insertRoomAt(GeneratedRoom room, Vec3i pos, boolean allowReplace) {
        if (!isInsideGrid(pos, false)) return null; //TODO add error message
        array.put(pos, new OccupiedGridCell(room, allowReplace));
        return array.get(pos);
    }

    public int getDepth() {
        return maxDepth;
    }

    public void insertChildren(GridCell parent, Vec3i from, Vec3i to, Boolean allowReplace) {
        for (int y = from.getY(); y < to.getY(); y++) {
            for (int x = from.getX(); x <= to.getX(); x++) {
                for (int z = from.getZ(); z <= to.getZ(); z++) {
                    if (getCellAt(new Vec3i(x,y,z)) == parent) continue;
                    array.put(new Vec3i(x,y,z), new ChildGridCell(parent));
                }
            }
        }
    }
}

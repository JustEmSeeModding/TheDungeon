package net.emsee.thedungeon.structureProcessor;

import net.minecraft.core.BlockPos;

public abstract class HollowOrganicClusterProcessor extends OrganicClusterProcessor{
    /**
     * Defines how thick the shell should be.
     * 0.0 = perfectly thin shell, higher = thicker layer.
     */
    protected float getShellThickness() {
        return 1f;
    }


    @Override
    protected boolean shouldReplace(BlockPos worldPos, BlockPos center, float radius, ClusterContext context) {
        double distance = distortedDistance(worldPos, center, context);

        // Outer layer bounds
        float smoothnessFactor = (1 + context.smoothness * context.random.nextFloat());
        double outerRadius = radius * smoothnessFactor;

        if (distance >= outerRadius - 0.5 && distance <= outerRadius) {
            return true;
        }

        double innerRadius = outerRadius - getShellThickness();
        // Only replace blocks within a thin shell near the edge
        return distance >= innerRadius && distance <= outerRadius;
    }
}

package net.emsee.thedungeon.structureProcessor;

import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public abstract class OrganicClusterProcessor extends AbstractReplacementProcessor {
    protected abstract long getBaseSeed();

    /**
     * when true has a different seed per original block
     */
    protected boolean getIsSeparateSeedPerReplacementBlock() {return true;}

    /** Cluster size parameters*/
    protected int getBaseClusterRadius() {
        return 5;
    }  // Average cluster size

    /** 0-1, how much size varies*/
    protected float getClusterSizeVariation() {
        return 0.3f;
    }

    /** 0-1, how smooth edges are*/
    protected float getClusterEdgeSmoothness() {
        return 0.4f;
    }

    /** Noise parameters for organic shapes, Smaller = smoother blobs */
    protected float getNoiseScale() {
        return 0.15f;
    }

    /** More octaves = more detail */
    protected int getNoiseOctaves() {
        return 20;
    }

    protected float getClusterDensity() {
        return 1.0f;
    } // Default density

    // Calculate grid size for cluster centers
    protected int getGridSize() {
        return (int) (getBaseClusterRadius() * 2 * (1 + getClusterSizeVariation()) / getClusterDensity());
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos pos,
                                                        StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo,
                                                        StructurePlaceSettings settings, @Nullable StructureTemplate template) {

        BlockPos worldPos = relativeBlockInfo.pos();
        Block currentBlock = relativeBlockInfo.state().getBlock();
        Map<Block, WeightedMap.Int<ReplaceInstance>> replacements = getReplacements();

        if (!replacements.containsKey(currentBlock)) {
            return relativeBlockInfo;
        }

        int gridSize = getGridSize();
        // Calculate cluster grid coordinates (world-based, not chunk-based)
        int gridX = Math.floorDiv(worldPos.getX(), gridSize);
        int gridZ = Math.floorDiv(worldPos.getZ(), gridSize);
        int gridY = Math.floorDiv(worldPos.getY(), gridSize); // Use same grid for Y

        // Unique seed for cluster center (shared across all blocks in this grid cell)
        long centerSeed = calculateCenterSeed(getBaseSeed(), gridX, gridY, gridZ);
        ClusterContext context = new ClusterContext(centerSeed, getBaseClusterRadius());
        context.variation = getClusterSizeVariation();
        context.smoothness = getClusterEdgeSmoothness();
        context.noiseScale = getNoiseScale();
        context.octaves = getNoiseOctaves();

        // Get cluster center and radius
        Tuple<BlockPos, Float> cluster = getClusterCenter(gridX, gridY, gridZ, context);
        BlockPos center = cluster.getA();
        float radius = cluster.getB();

        // Check if this position should be replaced
        if (shouldReplace(worldPos, center, radius, context)) {
            // Unique seed per cluster AND block type
            long replacementSeed = calculateReplacementSeed(getBaseSeed(), gridX, gridY, gridZ, currentBlock);
            return getReplacement(level, offset, pos, blockInfo ,relativeBlockInfo, settings, template, replacements, replacementSeed);
        }

        return relativeBlockInfo;
    }

    private StructureTemplate.StructureBlockInfo getReplacement(
            LevelReader level, BlockPos offset, BlockPos pos,
            StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo,
            StructurePlaceSettings settings, @Nullable StructureTemplate template,
            Map<Block, WeightedMap.Int<ReplaceInstance>> replacements,
            long seed) {

        Block currentBlock = relativeBlockInfo.state().getBlock();
        RandomSource random = RandomSource.create(seed);
        WeightedMap.Int<ReplaceInstance> allOptions = replacements.get(currentBlock);

        if (allOptions == null || allOptions.isEmpty()) return relativeBlockInfo;

        final WeightedMap.Int<Supplier<BlockState>> options = new WeightedMap.Int<>();

        allOptions.forEach((instance, weight) -> {
            if (instance.test(level, offset, pos, blockInfo ,relativeBlockInfo, settings, template))
                options.put(instance.stateSupplier, weight);
        });

        if (options.isEmpty()) return relativeBlockInfo;

        Supplier<BlockState> newStateSupplier = options.getRandom(random);
        if (newStateSupplier == null) return relativeBlockInfo;

        BlockState newBlockState = newStateSupplier.get();
        if (newBlockState == null) return relativeBlockInfo;

        BlockState oldState = relativeBlockInfo.state();
        newBlockState = copyAllProperties(oldState, newBlockState);

        return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), newBlockState, relativeBlockInfo.nbt());
    }

    private boolean shouldReplace(BlockPos worldPos, BlockPos center, float radius, ClusterContext context) {
        double distance = distortedDistance(worldPos, center, context);
        return distance < radius * (1 + context.smoothness * context.random.nextFloat());
    }

    private Tuple<BlockPos, Float> getClusterCenter(int gridX, int gridY, int gridZ, ClusterContext context) {
        int gridSize = getGridSize();
        int centerX = gridX * gridSize + gridSize / 2;
        int centerZ = gridZ * gridSize + gridSize / 2;
        int centerY = gridY * gridSize + gridSize / 2;

        // Apply consistent random offset within the grid cell
        centerX += context.random.nextInt(gridSize) - gridSize / 2;
        centerY += context.random.nextInt(gridSize / 2) - gridSize / 4; // Less vertical variation
        centerZ += context.random.nextInt(gridSize) - gridSize / 2;

        // Randomize cluster size
        float radius = context.baseRadius * (1 + context.variation * (context.random.nextFloat() - 0.5f));

        return new Tuple<>(new BlockPos(centerX, centerY, centerZ), radius);
    }

    private double distortedDistance(BlockPos pos, BlockPos center, ClusterContext context) {
        double dx = pos.getX() - center.getX();
        double dy = (pos.getY() - center.getY()) * 1.4; // Vertical stretch
        double dz = pos.getZ() - center.getZ();

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        // Apply noise distortion
        double noise = fractalNoise(
                pos.getX() * context.noiseScale,
                pos.getY() * context.noiseScale,
                pos.getZ() * context.noiseScale,
                context.octaves,
                context.random
        );

        return distance * (0.9 + 0.2 * noise);
    }

    private double fractalNoise(double x, double y, double z, int octaves, RandomSource random) {
        double value = 0;
        double amplitude = 1;
        double frequency = 1;
        double maxValue = 0;

        for (int i = 0; i < octaves; i++) {
            value += amplitude * simplexNoise(x * frequency, y * frequency, z * frequency, random);
            maxValue += amplitude;
            amplitude *= 0.5;
            frequency *= 2;
        }

        return value / maxValue;
    }

    private double simplexNoise(double x, double y, double z, RandomSource random) {
        // Simple 3D noise implementation
        final double F3 = 1.0 / 3.0;
        double s = (x + y + z) * F3;
        int i = (int) Math.floor(x + s);
        int j = (int) Math.floor(y + s);
        int k = (int) Math.floor(z + s);

        final double G3 = 1.0 / 6.0;
        double t = (i + j + k) * G3;
        double x0 = x - (i - t);
        double y0 = y - (j - t);
        double z0 = z - (k - t);

        long seed = random.nextLong() ^ (i * 131L) ^ (j * 7919L) ^ (k * 3413L);
        RandomSource gradRandom = RandomSource.create(seed);
        double grad = gradRandom.nextDouble() * 2 - 1;

        return (x0 + y0 + z0) * grad;
    }

    // Unique seed for cluster center (ignores block type)
    private long calculateCenterSeed(long baseSeed, int gridX, int gridY, int gridZ) {
        return baseSeed +
                gridX * 39916801L +
                gridY * 479001599L +
                gridZ * 3413L;
    }

    // Unique seed per cluster AND block type
    private long calculateReplacementSeed(long baseSeed, int gridX, int gridY, int gridZ, Block block) {
        return calculateCenterSeed(baseSeed, gridX, gridY, gridZ) + (getIsSeparateSeedPerReplacementBlock()? block.hashCode():0);
    }

    private static class ClusterContext {
        final RandomSource random;
        final int baseRadius;
        float variation;
        float smoothness;
        float noiseScale;
        int octaves;

        ClusterContext(long seed, int baseRadius) {
            this.random = RandomSource.create(seed);
            this.baseRadius = baseRadius;
        }
    }
}
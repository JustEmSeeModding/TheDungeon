package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.component.ModDataComponentTypes;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.emsee.thedungeon.utils.ParticleUtils;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;

public class DungeonPortalCompas extends DungeonItem {
    // Optimization parameters
    private static final boolean ONLY_UPDATE_IN_HAND = false;

    private static final int MAX_RANGE = 150;
    private static final int NODE_LIMIT = 300;
    private static final int SERVER_UPDATE_INTERVAL = 30; // ticks
    private static final int CLIENT_UPDATE_INTERVAL = 20;  // ticks
    private static final double MOVE_THRESHOLD_UPDATE = 2;

    private static final ParticleOptions PARTICLE_OPTIONS = ParticleTypes.END_ROD;

    // Cache for performance
    private static final Map<UUID, Long> lastServerUpdate = new HashMap<>();
    private static final Map<UUID, BlockPos> lastPlayerPosition = new HashMap<>();
    private static final Map<UUID, Long> lastClientUpdate = new HashMap<>();

    // Reusable mob entity to avoid creation/destruction overhead
    private Mob cachedPathfindingMob = null;

    public DungeonPortalCompas(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!(entity instanceof Player player)) return;
        boolean stackInHand = (player.getMainHandItem() == stack || player.getOffhandItem() == stack))
        if (ONLY_UPDATE_IN_HAND && !stackInHand) return;

        UUID playerId = player.getUUID();
        long currentTime = level.getGameTime();
        BlockPos currentPos = player.blockPosition();

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            handleServer(serverLevel, player, stack, playerId, currentTime, currentPos);
        }
        else if (stackInHand && level.isClientSide && level instanceof ClientLevel clientLevel) {
            handleClient(clientLevel, player, stack, playerId, currentTime, currentPos);
        }
    }

    @Nullable
    public static GlobalPos getTargetPosition(ItemStack stack) {
        BlockPos target = stack.get(ModDataComponentTypes.COMPAS_SAVED_BLOCK_POS.get());
        return target != null ? GlobalPos.of(ModDimensions.DUNGEON_LEVEL_KEY, target) : null;
    }

    protected void handleClient(ClientLevel level, Player player, ItemStack itemStack,
                                UUID playerId, long currentTime, BlockPos currentPos) {
        // Only update if enough time has passed or player moved significantly
        Long lastUpdate = lastClientUpdate.get(playerId);
        BlockPos lastPos = lastPlayerPosition.get(playerId);

        boolean shouldUpdate = lastUpdate == null ||
                currentTime - lastUpdate >= CLIENT_UPDATE_INTERVAL ||
                (lastPos != null && currentPos.distSqr(lastPos) > MOVE_THRESHOLD_UPDATE*MOVE_THRESHOLD_UPDATE);

        if (!shouldUpdate) return;

        BlockPos targetPos = itemStack.get(ModDataComponentTypes.COMPAS_SAVED_BLOCK_POS.get());
        if (targetPos == null) return;

        List<Vec3> path = findPathToBlock(level, player, targetPos);
        if (!path.isEmpty()) {
            renderPathTrail(path, level);
        }

        // Update cache
        lastClientUpdate.put(playerId, currentTime);
        lastPlayerPosition.put(playerId, currentPos);
    }

    protected void handleServer(ServerLevel level, Player player, ItemStack itemStack,
                                UUID playerId, long currentTime, BlockPos currentPos) {
        // Only update if enough time has passed or player moved significantly
        Long lastUpdate = lastServerUpdate.get(playerId);
        BlockPos lastPos = lastPlayerPosition.get(playerId);

        boolean shouldUpdate = lastUpdate == null ||
                currentTime - lastUpdate >= SERVER_UPDATE_INTERVAL ||
                (lastPos != null && currentPos.distSqr(lastPos) > MOVE_THRESHOLD_UPDATE*MOVE_THRESHOLD_UPDATE);

        if (!shouldUpdate || !player.onGround()) return;

        BlockPos targetPos = findClosestPathablePortal(
                level,
                player,
                GlobalDungeonManager.getPortalPositions(
                        level.getServer(),
                        DungeonRank.getClosestTo(currentPos)
                )
        );

        itemStack.set(ModDataComponentTypes.COMPAS_SAVED_BLOCK_POS, targetPos);

        // Update cache
        lastServerUpdate.put(playerId, currentTime);
        lastPlayerPosition.put(playerId, currentPos);
    }


    protected BlockPos findClosestPathablePortal(ServerLevel level, Player player, List<BlockPos> portals) {
        // Only calculate if we have valid portals
        if (portals.isEmpty()) return null;

        // Get cached mob or create one
        if (cachedPathfindingMob == null || !cachedPathfindingMob.isAlive()) {
            cachedPathfindingMob = EntityType.ZOMBIE.create(level);
            if (cachedPathfindingMob == null) return null;
        }
        cachedPathfindingMob.setPos(player.position());

        // Pre-filter candidates
        final double maxDistSq = (MAX_RANGE * 2) * (MAX_RANGE * 2);
        List<BlockPos> candidates = portals.stream()
                .filter(pos -> pos.distSqr(player.blockPosition()) < maxDistSq)
                .sorted(Comparator.comparingDouble(pos -> pos.distSqr(player.blockPosition())))
                .limit(5)
                .toList();

        BlockPos closestPortal = null;
        double shortestPathLength = Double.MAX_VALUE;

        PathNavigationRegion region = new PathNavigationRegion(
                level,
                player.blockPosition().offset(-MAX_RANGE, -MAX_RANGE, -MAX_RANGE),
                player.blockPosition().offset(MAX_RANGE, MAX_RANGE, MAX_RANGE)
        );

        WalkNodeEvaluator nodeEvaluator = new WalkNodeEvaluator();
        nodeEvaluator.prepare(region, cachedPathfindingMob);
        PathFinder pathFinder = new PathFinder(nodeEvaluator, NODE_LIMIT);

        for (BlockPos portal : candidates) {
            Path path = pathFinder.findPath(
                    region,
                    cachedPathfindingMob,
                    Collections.singleton(portal),
                    MAX_RANGE,
                    1,
                    100f
            );

            if (path != null && path.canReach()) {
                double pathLength = calculatePathLength(path);
                if (pathLength < shortestPathLength) {
                    shortestPathLength = pathLength;
                    closestPortal = portal;

                    // Early exit if we find a very close portal
                    if (pathLength < 10) break;
                }
            }
        }

        return closestPortal;
    }

    protected static double calculatePathLength(Path path) {
        double length = 0;
        for (int i = 1; i < path.getNodeCount(); i++) {
            Node a = path.getNode(i - 1);
            Node b = path.getNode(i);
            double dx = b.x - a.x;
            double dy = b.y - a.y;
            double dz = b.z - a.z;
            length += Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
        return length;
    }

    protected List<Vec3> findPathToBlock(ClientLevel level, Player player, BlockPos target) {
        List<Vec3> pathPoints = new ArrayList<>();
        if (level.dimension() != ModDimensions.DUNGEON_LEVEL_KEY)
            return pathPoints;

        // Get cached mob or create one
        if (cachedPathfindingMob == null || !cachedPathfindingMob.isAlive()) {
            cachedPathfindingMob = EntityType.ZOMBIE.create(level);
            if (cachedPathfindingMob == null) return pathPoints;
        }
        cachedPathfindingMob.setPos(player.position());

        PathNavigationRegion region = new PathNavigationRegion(
                level,
                player.blockPosition().offset(-MAX_RANGE, -MAX_RANGE, -MAX_RANGE),
                player.blockPosition().offset(MAX_RANGE, MAX_RANGE, MAX_RANGE)
        );

        WalkNodeEvaluator nodeEvaluator = new WalkNodeEvaluator();
        nodeEvaluator.prepare(region, cachedPathfindingMob);
        PathFinder pathFinder = new PathFinder(nodeEvaluator, NODE_LIMIT);

        Path path = pathFinder.findPath(
                region,
                cachedPathfindingMob,
                Collections.singleton(target),
                MAX_RANGE,
                1,
                100f
        );

        if (path != null && path.getNodeCount() > 0) {
            pathPoints.add(player.position().add(0, 0.25, 0));

            for (int i = 0; i < path.getNodeCount(); i++) {
                Node node = path.getNode(i);
                pathPoints.add(new Vec3(
                        node.x + 0.5,
                        node.y + 0.25,
                        node.z + 0.5
                ));
            }
        }

        return pathPoints;
    }

    protected static void renderPathTrail(List<Vec3> points, ClientLevel level) {
        if (points.size() < 2) return;

        // Only render every other segment to reduce particle count
        for (int i = 0; i < points.size() - 1; i += 2) {
            Vec3 start = points.get(i);
            Vec3 end = points.get(i + 1);

            Vec3 direction = end.subtract(start);
            float segmentLength = (float) direction.length();
            if (segmentLength < 0.01) continue; // Skip tiny segments

            direction = direction.normalize();

            // Use adaptive density - fewer particles for longer segments
            double probability = Math.min(0.05, 1.0 / segmentLength);

            ParticleUtils.line(
                    start.x, start.y + .5, start.z,
                    direction,
                    segmentLength,
                    level,
                    PARTICLE_OPTIONS,
                    0, -.001, 0,
                    probability
            );
        }
    }
}
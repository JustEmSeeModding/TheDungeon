package net.emsee.thedungeon.utils;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;


/**
 * Utility class for spawning particles in various geometric patterns.
 * Provides methods for both client-side and server-side particle effects.
 */
public final class ParticleUtils {

    /**
     * Spawns particles in a circular ring on the client.
     */
    public static void circle(double centerX, double centerY, double centerZ, double radius, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
        if (density <= 0 || probability <= 0 || radius == 0) return;
        double r = (radius * 2) / Math.PI;
        for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * r)) {
            double x = Math.cos(a) * r;
            double z = Math.sin(a) * r;
            double rand = Math.random();
            if (rand < probability) {
                level.addParticle(particle, true, x + centerX, centerY, z + centerZ, xSpeed, ySpeed, zSpeed);
            }
        }
    }

    /**
     * Spawns particles in a circular ring on the server.
     */
    public static void circle(double centerX, double centerY, double centerZ, double radius, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
        if (density <= 0 || probability <= 0 || radius == 0) return;
        double r = (radius * 2) / Math.PI;
        for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * r)) {
            double x = Math.cos(a) * r;
            double z = Math.sin(a) * r;
            double rand = Math.random();
            if (rand < probability) {
                level.sendParticles(particle, x + centerX, centerY, z + centerZ, 1, xSpeed, ySpeed, zSpeed, 1);
            }
        }
    }

    /**
     * Spawns particles in an expanding circular ring on the client.
     * Particles move radially outward from the center.
     */
    //client
    public static void circleExpand(double centerX, double centerY, double centerZ, double radius, double expandSpeed, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
        if (density <= 0 || probability <= 0 || radius == 0) return;
        double r = (radius * 2) / Math.PI;
        for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * r)) {
            double x = Math.cos(a) * r;
            double z = Math.sin(a) * r;
            double rand = Math.random();
            if (rand < probability) {
                level.addParticle(particle, true, x + centerX, centerY, z + centerZ, xSpeed - (x * expandSpeed), ySpeed, zSpeed - (z * expandSpeed));
            }
        }
    }

    /**
     * Spawns particles in an expanding circular ring on the server.
     * Particles move radially outward from the center.
     */
    //server
    public static void circleExpand(double centerX, double centerY, double centerZ, double radius, double expandSpeed, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
        if (density <= 0 || probability <= 0 || radius == 0) return;
        double r = (radius * 2) / Math.PI;
        for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * r)) {
            double x = Math.cos(a) * r;
            double z = Math.sin(a) * r;
            double rand = Math.random();
            if (rand < probability) {
                level.sendParticles(particle, x + centerX, centerY, z+centerZ, 1, xSpeed - (x * expandSpeed), ySpeed, zSpeed- (z * expandSpeed), 1);
            }
        }
    }

    /**
     * Spawns particles in a cylindrical shell on the client.
     */
    //client
    public static void cylinder(double centerX, double centerY, double centerZ, double radius, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density, double height) {
        if (density <= 0 || probability <= 0 || height <= 0) return;
        for (double d = 0; d < height; d += density / 100.00) {
            circle(centerX, centerY + d, centerZ, radius, level, particle, xSpeed, ySpeed, zSpeed, probability / 2, density);
        }
    }

    /**
     * Spawns particles in a cylindrical shell on the server.
     */
    //server
    public static void cylinder(double centerX, double centerY, double centerZ, double radius, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density, double height) {
        if (density <= 0 || probability <= 0 || height <= 0) return;
        for (double d = 0; d < height; d += density / 100.00) {
            circle(centerX, centerY + d, centerZ, radius, level, particle, xSpeed, ySpeed, zSpeed, probability / 2, density);
        }
    }

    /**
     * Spawns particles as a 3D grid of circles on the client.
     * Creates three perpendicular circle rings (one on each plane) intersecting at the center.
     */
    //client
    public static void sphereGrid(double centerX, double centerY, double centerZ, double radius, ClientLevel level, ParticleOptions Particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
        if (density <= 0 || probability <= 0 || radius == 0) return;

        circle(centerX, centerY, centerZ, radius, level, Particle, xSpeed, ySpeed, zSpeed, probability / 2, density);
        radius = (radius * 2) / Math.PI;
        for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * radius)) {
            double x = Math.cos(a) * radius;
            double y = Math.sin(a) * radius;
            double rand = Math.random();
            if (rand < probability) {
                level.addParticle(Particle, true, x + centerX, y + centerY, centerZ, xSpeed, ySpeed, zSpeed);
            }
        }
        for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * radius)) {
            double y = Math.cos(a) * radius;
            double z = Math.sin(a) * radius;
            double rand = Math.random();
            if (rand < probability) {
                level.addParticle(Particle, true, centerX, y + centerY, z + centerZ, xSpeed, ySpeed, zSpeed);
            }
        }
    }

    /**
     * Spawns particles as a 3D grid of circles on the server.
     * Creates three perpendicular circle rings (one on each plane) intersecting at the center.
     */
    //server
    public static void sphereGrid(double centerX, double centerY, double centerZ, double radius, ServerLevel level, ParticleOptions Particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
        if (density <= 0 || probability <= 0 || radius == 0) return;

        circle(centerX, centerY, centerZ, radius, level, Particle, xSpeed, ySpeed, zSpeed, probability / 2, density);
        radius = (radius * 2) / Math.PI;
        for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * radius)) {
            double x = Math.cos(a) * radius;
            double y = Math.sin(a) * radius;
            double rand = Math.random();
            if (rand < probability) {
                level.sendParticles(Particle, x + centerX, y + centerY, centerZ, 1, xSpeed, ySpeed, zSpeed, 1);
            }
        }
        for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * radius)) {
            double y = Math.cos(a) * radius;
            double z = Math.sin(a) * radius;
            double rand = Math.random();
            if (rand < probability) {
                level.sendParticles(Particle, centerX, y + centerY, z + centerZ, 1, xSpeed, ySpeed, zSpeed, 1);
            }
        }
    }

    /**
     * Spawns particles in a spherical shell on the client.
     */
    //client
    public static void sphere(double centerX, double centerY, double centerZ, double radius, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
        if (density <= 0 || probability <= 0 || radius == 0) return;
        radius = (radius * 2) / Math.PI;
        for (double i = 0; i <= Math.PI; i += Math.PI / (density * radius)) {
            double radi = Math.sin(i) * radius;
            double y = Math.cos(i) * radius;
            for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * radius)) {
                double x = Math.cos(a) * radi;
                double z = Math.sin(a) * radi;
                double rand = Math.random();
                if (rand < probability) {
                    level.addParticle(particle, true, x + centerX, y + centerY, z + centerZ, xSpeed, ySpeed, zSpeed);
                }
            }
        }
    }

    /**
     * Spawns particles in a spherical shell on the server.
     */
    //server
    public static void sphere(double centerX, double centerY, double centerZ, double radius, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
        if (density <= 0 || probability <= 0 || radius == 0) return;
        radius = (radius * 2) / Math.PI;
        for (double i = 0; i <= Math.PI; i += Math.PI / (density * radius)) {
            double radi = Math.sin(i) * radius;
            double y = Math.cos(i) * radius;
            for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * radius)) {
                double x = Math.cos(a) * radi;
                double z = Math.sin(a) * radi;
                double rand = Math.random();
                if (rand < probability) {
                    level.sendParticles(particle, x + centerX, y + centerY, z + centerZ, 1, xSpeed, ySpeed, zSpeed, 1);
                }
            }
        }
    }

    /**
     * Spawns particles in an expanding spherical shell on the client.
     * Particles move radially outward from the center.
     */
    //client
    public static void sphereExpand(double centerX, double centerY, double centerZ, double radius, double expandSpeed, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
        if (density <= 0 || probability <= 0 || radius == 0) return;
        radius = (radius * 2) / Math.PI;
        for (double i = 0; i <= Math.PI; i += Math.PI / (density * radius)) {
            double radi = Math.sin(i) * radius;
            double y = Math.cos(i) * radius;
            for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * radius)) {
                double x = Math.cos(a) * radi;
                double z = Math.sin(a) * radi;
                double rand = Math.random();
                if (rand < probability) {
                    level.addParticle(particle, true, x + centerX, y + centerY, z + centerZ, xSpeed - (x / 4 * expandSpeed), ySpeed - (y / 4 * expandSpeed), zSpeed - (z / 4 * expandSpeed));
                }
            }
        }
    }

    /**
     * Spawns particles in an expanding spherical shell on the server.
     * Particles move radially outward from the center.
     */
    public static void sphereExpand(double centerX, double centerY, double centerZ, double radius, double expandSpeed, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
        if (density <= 0 || probability <= 0 || radius == 0) return;
        radius = (radius * 2) / Math.PI;
        for (double i = 0; i <= Math.PI; i += Math.PI / (density * radius)) {
            double radi = Math.sin(i) * radius;
            double y = Math.cos(i) * radius;
            for (double a = 0; a < Math.PI * 2; a += Math.PI / (density * radius)) {
                double x = Math.cos(a) * radi;
                double z = Math.sin(a) * radi;
                double rand = Math.random();
                if (rand < probability) {
                    level.sendParticles(particle, x + centerX, y + centerY, z + centerZ, 1, xSpeed - (x / 4 * expandSpeed), ySpeed - (y / 4 * expandSpeed), zSpeed - (z / 4 * expandSpeed), 1);
                }
            }
        }
    }

    /**
     * Spawns particles along a line on the client.
     */
    public static void line(double xStart, double yStart, double zStart, Vec3 direction, float length, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability) {
        if (length <= 0 || probability <= 0) return;
        direction = direction.normalize();
        double D = 0;
        while (D <= length) {
            double rand = Math.random();
            if (rand < probability) {
                level.addParticle(particle, true, xStart + (direction.x * D), yStart + (direction.y * D), zStart + (direction.z * D), xSpeed, ySpeed, zSpeed);
            }
            D = D + 0.03;
        }
    }

    /**
     * Spawns particles along a line on the server.
     */
    //server
    public static void line(double xStart, double yStart, double zStart, Vec3 direction, float length, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability) {
        if (length <= 0 || probability <= 0) return;
        direction = direction.normalize();
        double D = 0;
        while (D <= length) {
            double rand = Math.random();
            if (rand < probability) {
                level.sendParticles(particle, xStart + (direction.x * D), yStart + (direction.y * D), zStart + (direction.z * D), 1, xSpeed, ySpeed, zSpeed, 1);
            }
            D = D + 0.03;
        }
    }

    /**
     * Spawns particles in a rectangular cuboid area on the client.
     */
    public static void area(double minX, double minY, double minZ, double xSize, double ySize, double zSize, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, int amount) {
        if (amount <= 0) return;
        int times = 0;
        while (times < amount) {
            double spawnX = minX + (Math.random() * xSize);
            double spawnY = minY + (Math.random() * ySize);
            double spawnZ = minZ + (Math.random() * zSize);
            level.addParticle(particle, true, spawnX, spawnY, spawnZ, xSpeed, ySpeed, zSpeed);
            times++;
        }
    }

    /**
     * Spawns a particle in a random position within a cuboid area on the client.
     */
    public static void area(double minX, double minY, double minZ, double xSize, double ySize, double zSize, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability) {
        if (probability <= 0) return;
        double spawnX = minX + (Math.random() * xSize);
        double spawnY = minY + (Math.random() * ySize);
        double spawnZ = minZ + (Math.random() * zSize);
        double rand = Math.random();
        if (rand < probability) {
            level.addParticle(particle, true, spawnX, spawnY, spawnZ, xSpeed, ySpeed, zSpeed);
        }
    }

    /**
     * Spawns particles in a rectangular cuboid area on the server.
     */
    //server
    public static void area(double minX, double minY, double minZ, double xSize, double ySize, double zSize, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, int amount) {
        if (amount <= 0) return;
        int times = 0;
        while (times < amount) {
            double spawnX = minX + (Math.random() * xSize);
            double spawnY = minY + (Math.random() * ySize);
            double spawnZ = minZ + (Math.random() * zSize);
            level.sendParticles(particle, spawnX, spawnY, spawnZ, 1, xSpeed, ySpeed, zSpeed, 1);
            times++;
        }
    }

    /**
     * Spawns a particle in a random position within a cuboid area on the server.
     */
    //server
    public static void area(double minX, double minY, double minZ, double xSize, double ySize, double zSize, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability) {
        if (probability <= 0) return;
        double spawnX = minX + (Math.random() * xSize);
        double spawnY = minY + (Math.random() * ySize);
        double spawnZ = minZ + (Math.random() * zSize);
        double rand = Math.random();
        if (rand < probability) {
            level.sendParticles(particle, spawnX, spawnY, spawnZ, 1, xSpeed, ySpeed, zSpeed, 1);
        }
    }
}

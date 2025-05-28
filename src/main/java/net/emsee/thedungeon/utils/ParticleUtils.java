package net.emsee.thedungeon.utils;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;


/**
 * a class I originally made for a different mod
 * has a lot of cool particle shapes
 * maybe useful for some artifacts or Mob attacks
 */
public final class ParticleUtils {
    //client
    public static void circle(double centerX, double centerY, double centerZ, double radius, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
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

    //server
    public static void circle(double centerX, double centerY, double centerZ, double radius, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
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

    //client
    public static void circleExpand(double centerX, double centerY, double centerZ, double radius, double expandSpeed, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
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

    //server
    public static void circleExpand(double centerX, double centerY, double centerZ, double radius, double expandSpeed, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
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

    //client
    public static void cylinder(double centerX, double centerY, double centerZ, double radius, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density, double height) {
        for (double d = 0; d < height; d += density / 100.00) {
            circle(centerX, centerY + d, centerZ, radius, level, particle, xSpeed, ySpeed, zSpeed, probability / 2, density);
        }
    }

    //server
    public static void cylinder(double centerX, double centerY, double centerZ, double radius, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density, double height) {
        for (double d = 0; d < height; d += density / 100.00) {
            circle(centerX, centerY + d, centerZ, radius, level, particle, xSpeed, ySpeed, zSpeed, probability / 2, density);
        }
    }

    //client
    public static void sphereGrid(double centerX, double centerY, double centerZ, double radius, ClientLevel level, ParticleOptions Particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {

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

    //server
    public static void sphereGrid(double centerX, double centerY, double centerZ, double radius, ServerLevel level, ParticleOptions Particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
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


    //client
    public static void sphere(double centerX, double centerY, double centerZ, double radius, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
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

    //server
    public static void sphere(double centerX, double centerY, double centerZ, double radius, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
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

    //client
    public static void sphereExpand(double centerX, double centerY, double centerZ, double radius, double expandSpeed, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
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

    //server
    public static void sphereExpand(double centerX, double centerY, double centerZ, double radius, double expandSpeed, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability, int density) {
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

    //client
    public static void line(double xStart, double yStart, double zStart, Vec3 direction, float length, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability) {
        direction.normalize();
        double D = 0;
        while (D <= length) {
            double rand = Math.random();
            if (rand < probability) {
                level.addParticle(particle, true, xStart + (direction.x * D), yStart + (direction.y * D), zStart + (direction.z * D), xSpeed, ySpeed, zSpeed);
            }
            D = D + 0.03;
        }
    }

    //server
    public static void line(double xStart, double yStart, double zStart, Vec3 direction, float length, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability) {
        direction.normalize();
        double D = 0;
        while (D <= length) {
            double rand = Math.random();
            if (rand < probability) {
                level.sendParticles(particle, xStart + (direction.x * D), yStart + (direction.y * D), zStart + (direction.z * D), 1, xSpeed, ySpeed, zSpeed, 1);
            }
            D = D + 0.03;
        }
    }

    //client
    public static void area(double minX, double minY, double minZ, double xSize, double ySize, double zSize, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, int amount) {
        int times = 0;
        while (times < amount) {
            double spawnX = minX + (Math.random() * xSize);
            double spawnY = minY + (Math.random() * ySize);
            double spawnZ = minZ + (Math.random() * zSize);
            level.addParticle(particle, true, spawnX, spawnY, spawnZ, xSpeed, ySpeed, zSpeed);
            times++;
        }
    }

    //client
    public static void area(double minX, double minY, double minZ, double xSize, double ySize, double zSize, ClientLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability) {
        double spawnX = minX + (Math.random() * xSize);
        double spawnY = minY + (Math.random() * ySize);
        double spawnZ = minZ + (Math.random() * zSize);
        double rand = Math.random();
        if (rand < probability) {
            level.addParticle(particle, true, spawnX, spawnY, spawnZ, xSpeed, ySpeed, zSpeed);
        }
    }

    //server
    public static void area(double minX, double minY, double minZ, double xSize, double ySize, double zSize, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, int amount) {
        int times = 0;
        while (times < amount) {
            double spawnX = minX + (Math.random() * xSize);
            double spawnY = minY + (Math.random() * ySize);
            double spawnZ = minZ + (Math.random() * zSize);
            level.sendParticles(particle, spawnX, spawnY, spawnZ, 1, xSpeed, ySpeed, zSpeed, 1);
            level.addParticle(particle, true, spawnX, spawnY, spawnZ, xSpeed, ySpeed, zSpeed);
            times++;
        }
    }

    //server
    public static void area(double minX, double minY, double minZ, double xSize, double ySize, double zSize, ServerLevel level, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed, double probability) {
        double spawnX = minX + (Math.random() * xSize);
        double spawnY = minY + (Math.random() * ySize);
        double spawnZ = minZ + (Math.random() * zSize);
        double rand = Math.random();
        if (rand < probability) {
            level.sendParticles(particle, spawnX, spawnY, spawnZ, 1, xSpeed, ySpeed, zSpeed, 1);
        }
    }
}

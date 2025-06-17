package cn.lunadeer.liteworldedit.utils;

import cn.lunadeer.liteworldedit.LiteWorldEdit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class ParticleRender {

    public static void showBoxBorder(JavaPlugin plugin, String world, int x1, int y1, int z1, int x2, int y2, int z2) {
        showBoxBorder(plugin, new Location(plugin.getServer().getWorld(world), x1, y1, z1),
                new Location(plugin.getServer().getWorld(world), x2, y2, z2));
    }

    public static void showBoxBorder(JavaPlugin plugin, Location loc1, Location loc2) {
        plugin.getServer().getGlobalRegionScheduler().run(LiteWorldEdit.instance, (instance) -> {
            if (!loc1.getWorld().equals(loc2.getWorld())) {
                return;
            }
            int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
            int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
            int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
            int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX()) + 1;
            int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY()) + 1;
            int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ()) + 1;
            World world = loc1.getWorld();
            for (int x = minX; x <= maxX; x++) {
                spawnParticle(world, x, minY, minZ);
                spawnParticle(world, x, minY, maxZ);
                spawnParticle(world, x, maxY, minZ);
                spawnParticle(world, x, maxY, maxZ);
            }
            for (int y = minY; y <= maxY; y++) {
                spawnParticle(world, minX, y, minZ);
                spawnParticle(world, minX, y, maxZ);
                spawnParticle(world, maxX, y, minZ);
                spawnParticle(world, maxX, y, maxZ);
            }
            for (int z = minZ; z <= maxZ; z++) {
                spawnParticle(world, minX, minY, z);
                spawnParticle(world, minX, maxY, z);
                spawnParticle(world, maxX, minY, z);
                spawnParticle(world, maxX, maxY, z);
            }
        });
    }

    private static void spawnParticle(World world, double x, double y, double z) {
        world.spawnParticle(Particle.FLAME, x, y, z, 10, 0, 0, 0, 0);
    }
}

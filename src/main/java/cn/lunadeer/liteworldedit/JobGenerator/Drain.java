package cn.lunadeer.liteworldedit.JobGenerator;

import cn.lunadeer.liteworldedit.Cuboid;
import cn.lunadeer.liteworldedit.Jobs.Absorb;
import cn.lunadeer.liteworldedit.Managers.Cache;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Drain {
    public static void drain(Player player, World world, Cuboid cuboid) {
        for (int y = cuboid.y2(); y >= cuboid.y1(); y--) {
            for (int x = cuboid.x1(); x <= cuboid.x2(); x++) {
                for (int z = cuboid.z1(); z <= cuboid.z2(); z++) {
                    Location location = new Location(world, x, y, z);
                    Absorb absorb_job = new Absorb(location, player);
                    Cache.getInstance().getPlayer(player).addJob(absorb_job);
                }
            }
        }
    }
}

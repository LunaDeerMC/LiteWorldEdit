package cn.lunadeer.liteworldedit.JobGenerator;

import cn.lunadeer.liteworldedit.Cuboid;
import cn.lunadeer.liteworldedit.Jobs.Remove;
import cn.lunadeer.liteworldedit.Managers.Cache;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Empty {
    public static void empty(Player player, World world, Cuboid cuboid) {
        for (int y = cuboid.y2(); y >= cuboid.y1(); y--) {
            for (int x = cuboid.x1(); x <= cuboid.x2(); x++) {
                for (int z = cuboid.z1(); z <= cuboid.z2(); z++) {
                    Location location = new Location(world, x, y, z);
                    Remove remove_job = new Remove(location, player);
                    Cache.getInstance().getPlayer(player).addJob(remove_job);
                }
            }
        }

    }
}

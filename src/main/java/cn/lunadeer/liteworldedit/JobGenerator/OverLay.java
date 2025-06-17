package cn.lunadeer.liteworldedit.JobGenerator;

import cn.lunadeer.liteworldedit.Cuboid;
import cn.lunadeer.liteworldedit.Jobs.Place;
import cn.lunadeer.liteworldedit.Managers.Cache;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class OverLay {

    public static void overLay(Player player, World world, Cuboid cuboid, Material block) {
        for (int x = cuboid.x1(); x <= cuboid.x2(); x++) {
            for (int z = cuboid.z1(); z <= cuboid.z2(); z++) {
                for (int y = cuboid.y1(); y <= cuboid.y2(); y++) {
                    Block block1 = world.getBlockAt(x, y, z);
                    if (block1.getType() == Material.AIR) {
                        Location location = new Location(world, x, y, z);
                        Place place_job = new Place(location, player, block);
                        Cache.getInstance().getPlayer(player).addJob(place_job);
                        break;
                    }
                }
            }
        }
    }
}

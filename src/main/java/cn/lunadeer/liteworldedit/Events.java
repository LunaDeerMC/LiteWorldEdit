package cn.lunadeer.liteworldedit;

import cn.lunadeer.liteworldedit.Managers.Cache;
import cn.lunadeer.liteworldedit.Managers.Point;
import cn.lunadeer.liteworldedit.Managers.XPlayer;
import cn.lunadeer.liteworldedit.utils.Notification;
import cn.lunadeer.liteworldedit.utils.ParticleRender;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Cache.getInstance().playerQuit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Cache.getInstance().playerJoin(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void selectPoint(PlayerInteractEvent event) {
        XPlayer xplayer = Cache.getInstance().getPlayer(event.getPlayer());
        if (!xplayer.isSelectMode()) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.NETHERITE_PICKAXE) {
            return;
        }
        Block block = event.getClickedBlock();
        Action action = event.getAction();
        if (block == null) {
            return;
        }

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        Point point = new Point(x, y, z, player);

        if (action == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            Notification.info(player, "已选择第一个点: {0} {1} {2}", x, y, z);
            xplayer.addPoint(1, point);
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            Notification.info(player, "已选择第二个点: {0} {1} {2}", x, y, z);
            xplayer.addPoint(2, point);
        }

        if (xplayer.getPoints().get(1) != null && xplayer.getPoints().get(2) != null) {
            Point p1 = xplayer.getPoints().get(1);
            Point p2 = xplayer.getPoints().get(2);
            int deltaX = Math.abs(p1.x - p2.x);
            int deltaY = Math.abs(p1.y - p2.y);
            int deltaZ = Math.abs(p1.z - p2.z);
            if (deltaX > Configuration.maximumSize.x || deltaY > Configuration.maximumSize.y || deltaZ > Configuration.maximumSize.z) {
                return;
            }
            ParticleRender.showBoxBorder(LiteWorldEdit.instance, player.getWorld().getName(), p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
        }
    }
}

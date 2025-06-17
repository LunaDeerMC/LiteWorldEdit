package cn.lunadeer.liteworldedit.Jobs;

import cn.lunadeer.liteworldedit.utils.Notification;
import cn.lunadeer.liteworldedit.utils.XLogger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public abstract class Job {
    private final World world;
    private final Location location;
    private final Long time;
    private final Player creator;
    private final Inventory inventory;

    public Long getTime() {
        return time;
    }

    public Player getCreator() {
        return creator;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Location getLocation() {
        return location;
    }

    public World getWorld() {
        return world;
    }

    public Job(World world, Location location, Player player) {
        this.world = world;
        this.location = location;
        creator = player;
        time = System.currentTimeMillis();
        inventory = player.getInventory();
    }

    public abstract JobErrCode execution();

    public static boolean notInRange(Player player, Location location) {
        if (!player.getWorld().getName().equals(location.getWorld().getName())) {
            return true;
        }
        if (player.getLocation().distance(location) > 128) {
            Notification.error(player, "不允许超过128格操作！");
            return true;
        }
        return false;
    }

    public static HashMap<Integer, ?> getNetherPickaxes(Player player) {
        Inventory _inventory = player.getInventory();
        return _inventory.all(Material.NETHERITE_PICKAXE);
    }

    public static ItemStack getUsableNetherPickaxe(HashMap<Integer, ?> pickaxes, Player player) {
        ItemStack pickaxe = null;
        Damageable pickaxe_damage = null;
        for (Integer index : pickaxes.keySet()) {
            ItemStack p = player.getInventory().getItem(index);
            if (p == null) {
                XLogger.debug("{0} 获取到的下界合金镐为空！", index);
                continue;
            }
            ItemMeta pickaxe_meta = p.getItemMeta();
            if (pickaxe_meta == null) {
                XLogger.debug("{0} 获取到的下界合金镐元数据为空！", index);
                continue;
            }
            if (!(pickaxe_meta instanceof Damageable)) {
                XLogger.debug("{0} 无法转换为Damageable！", index);
                continue;
            }
            pickaxe_damage = (Damageable) pickaxe_meta;
            if (!pickaxe_meta.isUnbreakable()) {
                if (pickaxe_damage.getDamage() >= 2031 - 10) {
                    XLogger.debug("{0} 下界合金镐耐久太低！", index);
                    continue;
                }
            }
            pickaxe = p;
            break;
        }
        return pickaxe;
    }

    public static ItemStack useNetherPickaxe(ItemStack pickaxe) {
        int durability = pickaxe.getEnchantmentLevel(Enchantment.DURABILITY);
        double random = Math.random();
        Damageable pickaxe_damage = (Damageable) pickaxe.getItemMeta();
        if (pickaxe_damage.isUnbreakable()) {
            return pickaxe;
        }
        if (random < 1.0 / (durability + 1)) {
            pickaxe_damage.setDamage(pickaxe_damage.getDamage() + 1);
            pickaxe.setItemMeta(pickaxe_damage);
        }
        return pickaxe;
    }
}

package cn.lunadeer.liteworldedit.Jobs;

import cn.lunadeer.liteworldedit.Configuration;
import cn.lunadeer.liteworldedit.utils.XLogger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;

public class Remove extends Job {

    public Remove(Location location, Player player) {
        super(player.getWorld(), location, player);
    }

    @Override
    public JobErrCode execution() {
        // 超出距离
        if (notInRange(getCreator(), getLocation())) {
            XLogger.debug("超出距离！");
            return JobErrCode.OUT_OF_RANGE;
        }
        Block raw_block = getWorld().getBlockAt(getLocation());
        // 跳过不破坏的对象
        if (raw_block.isLiquid() || raw_block.isEmpty() || raw_block.getType().getHardness() == -1) {
            XLogger.debug("目标方块是液体或空气或不可破坏！");
            return JobErrCode.NO_BREAKABLE;
        }
        // 获取玩家背包中的下届合金镐
        HashMap<Integer, ?> pickaxes = getNetherPickaxes(getCreator());
        if (pickaxes.isEmpty()) {
            return JobErrCode.NO_PICKAXE;
        }
        ItemStack pickaxe = getUsableNetherPickaxe(pickaxes, getCreator());
        // 没有合适的镐
        if (pickaxe == null) {
            return JobErrCode.NOT_ENOUGH_DURATION;
        }
        BlockBreakEvent event = new BlockBreakEvent(raw_block, getCreator());
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            Collection<ItemStack> drops = raw_block.getDrops(pickaxe, getCreator());
            raw_block.setType(Material.AIR);
            if (Configuration.dropItems) {
                for (ItemStack drop : drops) {
                    raw_block.getWorld().dropItemNaturally(raw_block.getLocation(), drop);
                }
            }
            // 损坏镐
            if (!getCreator().isOp() && getCreator().getGameMode() != GameMode.CREATIVE) {
                useNetherPickaxe(pickaxe);
            }
            return JobErrCode.OK;
        } else {
            return JobErrCode.NO_PERMISSION;
        }
    }
}

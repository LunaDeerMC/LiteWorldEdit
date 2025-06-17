package cn.lunadeer.liteworldedit.Jobs;

import cn.lunadeer.liteworldedit.utils.XLogger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Absorb extends Job {
    public Absorb(Location location, Player player) {
        super(player.getWorld(), location, player);
    }

    @Override
    public JobErrCode execution() {
        // 超出距离
        if (notInRange(this.getCreator(), this.getLocation())) {
            XLogger.debug("超出距离！");
            return JobErrCode.OUT_OF_RANGE;
        }
        Block raw_block = this.getWorld().getBlockAt(this.getLocation());
        // 跳过非流体
        if (!raw_block.isLiquid()) {
            XLogger.debug("目标方块不是流体！");
            return JobErrCode.NOT_LIQUID;
        }
        HashMap<Integer, ?> sponge = this.getCreator().getInventory().all(Material.SPONGE);
        if (sponge.isEmpty()) {
            return JobErrCode.NO_SPONGE;
        }
        // 模拟海绵吸水事件
        BlockPlaceEvent event = new BlockPlaceEvent(raw_block, raw_block.getState(), raw_block, new ItemStack(Material.SPONGE), this.getCreator(), true);
        Bukkit.getPluginManager().callEvent(event);
        // 获取玩家背包中的下届合金镐
        HashMap<Integer, ?> pickaxes = getNetherPickaxes(this.getCreator());
        if (pickaxes.isEmpty()) {
            return JobErrCode.NO_PICKAXE;
        }
        ItemStack pickaxe = getUsableNetherPickaxe(pickaxes, this.getCreator());
        // 没有合适的镐
        if (pickaxe == null) {
            return JobErrCode.NOT_ENOUGH_DURATION;
        }
        if (!event.isCancelled()) {
            raw_block.setType(Material.SPONGE);
            raw_block.setType(Material.AIR);
            // 损坏镐
            if (!this.getCreator().isOp() && this.getCreator().getGameMode() != GameMode.CREATIVE) {
                useNetherPickaxe(pickaxe);
            }
            return JobErrCode.OK;
        } else {
            return JobErrCode.NO_PERMISSION;
        }
    }
}

package cn.lunadeer.liteworldedit.Jobs;

import cn.lunadeer.liteworldedit.utils.XLogger;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.HashMap;

public class Place extends Job {
    private final Material _block;


    public Place(Location location, Player player, Material material) {
        super(player.getWorld(), location, player);
        _block = material;
    }

    @Override
    public JobErrCode execution() {
        // 超出距离
        if (notInRange(getCreator(), getLocation())) {
            XLogger.debug("超出距离！");
            return JobErrCode.OUT_OF_RANGE;
        }
        // 跳过非空气方块
        Block raw_block = this.getWorld().getBlockAt(getLocation());
        if (!raw_block.isEmpty()) {
            XLogger.debug("目标方块不是空气！");
            return JobErrCode.NOT_AIR_BLOCK;
        }
        // 获取到玩家物品中材料的第一个堆叠
        ItemStack stack;
        int stack_index = getInventory().first(_block);
        if (stack_index == -1) {
            // 物品栏没有就去潜影盒里找
            if (!moveBlockFromShulkerBoxToInv()) {
                return JobErrCode.NOT_ENOUGH_ITEMS;
            }
            stack_index = getInventory().first(_block);
            if (stack_index == -1) {
                XLogger.debug("物品中没有该材料！");
                return JobErrCode.NOT_ENOUGH_ITEMS;
            }
        }
        stack = getInventory().getItem(stack_index);
        if (stack == null) {
            XLogger.debug("物品中没有该材料！");
            return JobErrCode.NOT_ENOUGH_ITEMS;
        }

        Block block = this.getWorld().getBlockAt(raw_block.getX() + 1, raw_block.getY(), raw_block.getZ());
        // 校验是否可以放置
        BlockPlaceEvent event = new BlockPlaceEvent(
                block,
                raw_block.getState(),
                block,
                stack,
                getCreator(),
                true,
                EquipmentSlot.HAND);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            raw_block.setType(_block);
            if (!getCreator().isOp() && getCreator().getGameMode() != GameMode.CREATIVE) {
                stack.setAmount(stack.getAmount() - 1);
            }
            return JobErrCode.OK;
        } else {
            return JobErrCode.NO_PERMISSION;
        }
    }

    private boolean moveBlockFromShulkerBoxToInv() {
        HashMap<Integer, ItemStack> boxes = new HashMap<>();
        int idx = 0;
        for (ItemStack item : getInventory().getContents()) {
            if (item != null && Tag.SHULKER_BOXES.isTagged(item.getType())) {
                boxes.put(idx, item);
            }
            idx++;
        }

        for (Integer index : boxes.keySet()) {
            XLogger.debug("找到潜影盒：" + index);
            ItemStack itemStack = getInventory().getItem(index);
            if (itemStack == null) {
                continue;
            }
            if (!(itemStack.getItemMeta() instanceof BlockStateMeta meta)) {
                XLogger.debug("不是BlockStateMeta！");
                continue;
            }
            if (!(meta.getBlockState() instanceof ShulkerBox shulkerBox)) {
                XLogger.debug("不是潜影盒！");
                continue;
            }
            Inventory boxInv = shulkerBox.getInventory();
            int item_idx = boxInv.first(_block);
            if (item_idx == -1) {
                continue;
            }
            ItemStack i = boxInv.getItem(item_idx);
            if (i == null) {
                continue;
            }
            // 把物品放到玩家物品栏
            getInventory().addItem(i);
            // 把潜影盒中的物品移除
            shulkerBox.getInventory().setItem(item_idx, null);
            // 更新潜影盒
            meta.setBlockState(shulkerBox);
            itemStack.setItemMeta(meta);
            return true;
        }
        return false;
    }
}

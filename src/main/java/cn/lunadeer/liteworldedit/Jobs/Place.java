package cn.lunadeer.liteworldedit.Jobs;

import cn.lunadeer.liteworldedit.LiteWorldEdit;
import cn.lunadeer.liteworldedit.LoggerX;
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
    public JobErrCode Do() {
        Player _creator = this.get_creator();
        Location _location = this.get_location();
        World _world = this.get_world();
        Inventory _inventory = this.get_inventory();
        // 超出距离
        if (!in_range(_creator, _location)) {
            LoggerX.debug("超出距离！");
            return JobErrCode.OUT_OF_RANGE;
        }
        // 跳过非空气方块
        Block raw_block = _world.getBlockAt(_location);
        if (!raw_block.isEmpty()) {
            LoggerX.debug("目标方块不是空气！");
            return JobErrCode.NOT_AIR_BLOCK;
        }
        // 获取到玩家物品中材料的第一个堆叠
        ItemStack stack;
        int stack_index = _inventory.first(_block);
        if (stack_index == -1) {
            // 物品栏没有就去潜影盒里找
            if (!moveBlockFromShulkerBoxToInv()) {
                return JobErrCode.NOT_ENOUGH_ITEMS;
            }
            stack_index = _inventory.first(_block);
            if (stack_index == -1) {
                LoggerX.debug("物品中没有该材料！");
                return JobErrCode.NOT_ENOUGH_ITEMS;
            }
        }
        stack = _inventory.getItem(stack_index);
        if (stack == null) {
            LoggerX.debug("物品中没有该材料！");
            return JobErrCode.NOT_ENOUGH_ITEMS;
        }
        // 检查货币消耗
        if (LiteWorldEdit.instance.getConfigMgr().isEconomyEnabled()) {
            if (!LiteWorldEdit.instance.getEconomyMgr().isEconomyExist()) {
                return JobErrCode.NO_ECONOMY_PROVIDER;
            }
            if (LiteWorldEdit.instance.getEconomyMgr().getBalance(_creator) < LiteWorldEdit.instance.getConfigMgr().getDeclineMoney()) {
                return JobErrCode.NOT_ENOUGH_MONEY;
            }
        }

        Block block = _world.getBlockAt(raw_block.getX() + 1, raw_block.getY(), raw_block.getZ());
        // 校验是否可以放置
        BlockPlaceEvent event = new BlockPlaceEvent(
                block,
                raw_block.getState(),
                block,
                stack,
                _creator,
                true,
                EquipmentSlot.valueOf("HAND"));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            raw_block.setType(_block);
            if (!_creator.isOp() && _creator.getGameMode() != GameMode.CREATIVE) {
                stack.setAmount(stack.getAmount() - 1);
            }
            // 消耗货币
            if (!_creator.isOp() && LiteWorldEdit.instance.getConfigMgr().isEconomyEnabled()) {
                LiteWorldEdit.instance.getEconomyMgr().withdrawPlayer(_creator, LiteWorldEdit.instance.getConfigMgr().getDeclineMoney());
            }
            return JobErrCode.OK;
        } else {
            return JobErrCode.NO_PERMISSION;
        }
    }

    private boolean moveBlockFromShulkerBoxToInv() {
        Inventory _inventory = this.get_inventory();
        HashMap<Integer, ItemStack> shulkerBoxes = new HashMap<>();
        HashMap<Integer, ? extends ItemStack> plainShulkerBoxes = _inventory.all(Material.SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> whiteShulkerBoxes = _inventory.all(Material.WHITE_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> orangeShulkerBoxes = _inventory.all(Material.ORANGE_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> magentaShulkerBoxes = _inventory.all(Material.MAGENTA_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> lightBlueShulkerBoxes = _inventory.all(Material.LIGHT_BLUE_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> yellowShulkerBoxes = _inventory.all(Material.YELLOW_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> limeShulkerBoxes = _inventory.all(Material.LIME_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> pinkShulkerBoxes = _inventory.all(Material.PINK_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> grayShulkerBoxes = _inventory.all(Material.GRAY_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> lightGrayShulkerBoxes = _inventory.all(Material.LIGHT_GRAY_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> cyanShulkerBoxes = _inventory.all(Material.CYAN_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> purpleShulkerBoxes = _inventory.all(Material.PURPLE_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> blueShulkerBoxes = _inventory.all(Material.BLUE_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> brownShulkerBoxes = _inventory.all(Material.BROWN_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> greenShulkerBoxes = _inventory.all(Material.GREEN_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> redShulkerBoxes = _inventory.all(Material.RED_SHULKER_BOX);
        HashMap<Integer, ? extends ItemStack> blackShulkerBoxes = _inventory.all(Material.BLACK_SHULKER_BOX);

        shulkerBoxes.putAll(plainShulkerBoxes);
        shulkerBoxes.putAll(whiteShulkerBoxes);
        shulkerBoxes.putAll(orangeShulkerBoxes);
        shulkerBoxes.putAll(magentaShulkerBoxes);
        shulkerBoxes.putAll(lightBlueShulkerBoxes);
        shulkerBoxes.putAll(yellowShulkerBoxes);
        shulkerBoxes.putAll(limeShulkerBoxes);
        shulkerBoxes.putAll(pinkShulkerBoxes);
        shulkerBoxes.putAll(grayShulkerBoxes);
        shulkerBoxes.putAll(lightGrayShulkerBoxes);
        shulkerBoxes.putAll(cyanShulkerBoxes);
        shulkerBoxes.putAll(purpleShulkerBoxes);
        shulkerBoxes.putAll(blueShulkerBoxes);
        shulkerBoxes.putAll(brownShulkerBoxes);
        shulkerBoxes.putAll(greenShulkerBoxes);
        shulkerBoxes.putAll(redShulkerBoxes);
        shulkerBoxes.putAll(blackShulkerBoxes);

        for (Integer index : shulkerBoxes.keySet()) {
            LoggerX.debug("找到潜影盒：" + index);
            ItemStack itemStack = _inventory.getItem(index);
            if (itemStack == null) {
                continue;
            }
            if (!(itemStack.getItemMeta() instanceof BlockStateMeta)) {
                LoggerX.debug("不是BlockStateMeta！");
                continue;
            }
            BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
            if (!(meta.getBlockState() instanceof ShulkerBox)) {
                LoggerX.debug("不是潜影盒！");
                continue;
            }
            ShulkerBox shulkerBox = (ShulkerBox) meta.getBlockState();
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
            _inventory.addItem(i);
            // 把潜影盒中的物品移除
            shulkerBox.getInventory().setItem(item_idx, null);
            // 更新潜影盒
            meta.setBlockState(shulkerBox);
            itemStack.setItemMeta(meta);
            return true;
        }
        LoggerX.debug("潜影盒中没有该材料！");
        return false;
    }
}

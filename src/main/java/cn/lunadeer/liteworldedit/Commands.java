package cn.lunadeer.liteworldedit;

import cn.lunadeer.liteworldedit.JobGenerator.Drain;
import cn.lunadeer.liteworldedit.JobGenerator.Empty;
import cn.lunadeer.liteworldedit.JobGenerator.Fill;
import cn.lunadeer.liteworldedit.JobGenerator.OverLay;
import cn.lunadeer.liteworldedit.Jobs.Job;
import cn.lunadeer.liteworldedit.Managers.ConfigManager;
import cn.lunadeer.liteworldedit.Managers.Point;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Commands implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            Notification.info((Player) sender, "使用 /lwe help 查看帮助。");
            Notification.info((Player) sender, "使用 /lwe help 查看帮助。");
            return true;
        }
        if (Objects.equals(args[0], "point") || Objects.equals(args[0], "p")) {
            addPoint(sender, args);
            return true;
        } else if (Objects.equals(args[0], "select")) {
            selectModeToggle(sender, args);
            return true;
        } else if (Objects.equals(args[0], "points")) {
            listPoints(sender);
            return true;
        } else if (Objects.equals(args[0], "fill")) {
            fillTask(sender, args);
            return true;
        } else if (Objects.equals(args[0], "empty")) {
            emptyTask(sender, args);
            return true;
        } else if (Objects.equals(args[0], "overlay")) {
            overlayTask(sender, args);
            return true;
        } else if (Objects.equals(args[0], "drain")) {
            drainTask(sender, args);
            return true;
        } else if (Objects.equals(args[0], "help")) {
            print_help(sender);
            return true;
        } else if (Objects.equals(args[0], "cancel")) {
            cancelJobs(sender);
            return true;
        } else if (Objects.equals(args[0], "reload")) {
            reloadConfigs(sender);
            return true;
        } else if (Objects.equals(args[0], "pause")) {
            pauseJobs(sender);
            return true;
        } else if (Objects.equals(args[0], "resume")) {
            resumeJobs(sender);
            return true;
        } else {
            Notification.info((Player) sender, "参数错误 使用 /lwe help 查看帮助。");
        }
        return true;
    }

    private static void resumeJobs(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LiteWorldEdit.instance.getCache().getPlayer(player).hasJob()) {
                LiteWorldEdit.instance.getCache().getPlayer(player).resumeJob();
                Notification.info(player, "已恢复。");
            } else {
                Notification.info(player, "你没有正在进行的任务。");
            }
        } else {
            LoggerX.info("该命令只能由玩家执行。");
        }
    }

    private static void pauseJobs(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LiteWorldEdit.instance.getCache().getPlayer(player).hasJob()) {
                LiteWorldEdit.instance.getCache().getPlayer(player).pauseJob();
                Notification.warn(player, "已暂停。");
            } else {
                Notification.warn(player, "你没有正在进行的任务。");
            }
        } else {
            LoggerX.info("该命令只能由玩家执行。");
        }
    }

    private static void reloadConfigs(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                Notification.error(player, "你没有权限。");
            } else {
                LiteWorldEdit.config.reload();
                LiteWorldEdit.economy.recheck();
                Notification.info(player, "已重载配置文件。");
            }
        } else {
            LiteWorldEdit.config.reload();
            LoggerX.info("已重载配置文件。");
        }
    }

    private static void cancelJobs(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LiteWorldEdit.instance.getCache().getPlayer(player).hasJob()) {
                LiteWorldEdit.instance.getCache().getPlayer(player).cancelJob();
                Notification.warn(player, "已取消。");
            } else {
                Notification.warn(player, "你没有正在进行的任务。");
            }
        } else {
            LoggerX.err("该命令只能由玩家执行。");
        }
    }

    private static void emptyTask(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            LoggerX.err("该命令只能由玩家执行。");
            return;
        }
        Player player = (Player) sender;
        try {
            Vector2 diagonalPoint = getVector2(sender, args, player);
            if (diagonalPoint == null) return;
            Empty.empty(player, player.getWorld(), diagonalPoint.pointA, diagonalPoint.pointB);
            Notification.info(player, "已添加任务。");
        } catch (NumberFormatException e) {
            Notification.error(player, "参数错误。");
        }
    }

    private static void drainTask(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            LoggerX.err("该命令只能由玩家执行。");
            return;
        }
        Player player = (Player) sender;
        try {
            Vector2 diagonalPoint = getVector2(sender, args, player);
            if (diagonalPoint == null) return;
            Drain.drain(player, player.getWorld(), diagonalPoint.pointA, diagonalPoint.pointB);
            Notification.info(player, "已添加任务。");
        } catch (NumberFormatException e) {
            Notification.error(player, "参数错误。");
        }
    }

    private static void overlayTask(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            LoggerX.err("该命令只能由玩家执行。");
            return;
        }
        Player player = (Player) sender;
        try {
            Vector2 diagonalPoint = getVector2(sender, args, player);
            if (diagonalPoint == null) return;
            ItemStack items_in_hand = player.getInventory().getItemInMainHand();
            if (!items_in_hand.getType().isBlock() || items_in_hand.getType() == Material.AIR) {
                Notification.error(player, "你手上没有方块。");
                return;
            }
            Material material = Material.getMaterial(items_in_hand.getType().name());
            OverLay.overLay(player, player.getWorld(), diagonalPoint.pointA, diagonalPoint.pointB, material);
            Notification.info(player, "已添加任务。");
            return;
        } catch (NumberFormatException e) {
            Notification.error(player, "参数错误。");
        }
    }

    private static void fillTask(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            LoggerX.err("该命令只能由玩家执行。");
            return;
        }
        Player player = (Player) sender;
        try {
            Vector2 diagonalPoint = getVector2(sender, args, player);
            if (diagonalPoint == null) return;
            ItemStack items_in_hand = player.getInventory().getItemInMainHand();
            if (!items_in_hand.getType().isBlock() || items_in_hand.getType() == Material.AIR) {
                Notification.error(player, "你手上没有方块。");
                return;
            }
            Material material = Material.getMaterial(items_in_hand.getType().name());
            Fill.fill(player, player.getWorld(), diagonalPoint.pointA, diagonalPoint.pointB, material);
            Notification.info(player, "已添加任务。");
            return;
        } catch (NumberFormatException e) {
            Notification.error(player, "参数错误。");
        }
    }

    private static Vector2 getVector2(CommandSender sender, String[] args, Player player) {
        int indexA;
        int indexB;
        if (args.length == 3) {
            indexA = Integer.parseInt(args[1]);
            indexB = Integer.parseInt(args[2]);
        } else {
            indexA = 1;
            indexB = 2;
        }
        Map<Integer, Point> points = LiteWorldEdit.instance.getCache().getPlayer(player).getPoints();
        if (points == null) {
            Notification.error(player, "你没有设置任何点。");
            return null;
        }
        Point pointA = points.get(indexA);
        Point pointB = points.get(indexB);
        if (pointA == null || pointB == null) {
            Notification.error(player, "点不存在，使用 /lwe points 查看所有已添加的点。");
            return null;
        }
        if (out_of_region(pointA, pointB)) {
            Notification.error(player, "选择的区域不可以超过 " + LiteWorldEdit.instance.getConfigMgr().getXMax() + "x" + LiteWorldEdit.instance.getConfigMgr().getYMax() + "x" + LiteWorldEdit.instance.getConfigMgr().getZMax() + "。");
            return null;
        }
        return new Vector2(pointA, pointB);
    }

    private static class Vector2 {
        public final Point pointA;
        public final Point pointB;

        public Vector2(Point pointA, Point pointB) {
            this.pointA = pointA;
            this.pointB = pointB;
        }
    }

    private static void listPoints(CommandSender sender) {
        if (!(sender instanceof Player)) {
            LoggerX.err("该命令只能由玩家执行。");
            return;
        }
        Player player = (Player) sender;
        Map<Integer, Point> points = LiteWorldEdit.instance.getCache().getPlayer(player).getPoints();
        if (points != null) {
            Notification.info(player, "你创建的点：");
            for (Map.Entry<Integer, Point> entry : points.entrySet()) {
                Point point = entry.getValue();
                Notification.info(player, entry.getKey() + ": " + point.x + ", " + point.y + ", " + point.z);
            }
        } else {
            Notification.warn(player, "你没有设置任何点。");
        }
    }

    private static void addPoint(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            LoggerX.err("该命令只能由玩家执行。");
            return;
        }
        Player player = (Player) sender;
        if (args.length == 5) {
            try {
                Integer index = Integer.parseInt(args[1]);
                int x = Integer.parseInt(args[2]);
                int y = Integer.parseInt(args[3]);
                int z = Integer.parseInt(args[4]);
                // 选择的点不允许超过128格范围
                if (!Job.in_range(player, new Location(player.getWorld(), x, y, z))) {
                    return;
                }
                Point point = new Point(x, y, z, player);
                if (!LiteWorldEdit.instance.getCache().getPlayer(player).addPoint(index, point)) {
                    Notification.error(player, "点的数量不允许超过20，请使用已有点序号覆盖已有点。");
                    return;
                }
                Notification.info(player, "点 " + index + " 已设置为 " + x + ", " + y + ", " + z + "。");
            } catch (NumberFormatException e) {
                Notification.error(player, "参数错误。");
            }
        } else {
            Notification.error(player, "参数错误。");
        }
    }

    private static void selectModeToggle(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            LoggerX.err("该命令只能由玩家执行。");
            return;
        }
        Player player = (Player) sender;
        LiteWorldEdit.instance.getCache().getPlayer(player).toggleSelectMode();
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("point", "p", "select", "points", "fill", "empty", "overlay", "drain", "cancel", "pause", "resume", "help", "reload");
        } else if (args.length == 2) {
            if (args[0].equals("point") || args[0].equals("p")) {
                return Collections.singletonList("<点序号(整数)> <x> <y> <z> - 创建点");
            } else if (args[0].equals("fill") || args[0].equals("empty") || args[0].equals("overlay") || args[0].equals("drain")) {
                return Collections.singletonList("[点序号A] [点序号B] - 不填写默认为 1 2");
            }
        }
        return Collections.emptyList();
    }

    public void print_help(CommandSender sender) {
        if (!(sender instanceof Player)) {
            LoggerX.err("该命令只能由玩家执行。");
            return;
        }
        Notification.info((Player) sender, "LiteWorldEdit 帮助");
        Notification.info((Player) sender, "/lwe help - 查看帮助");
        Notification.info((Player) sender, "/lwe point|p <点序号(整数)> <x> <y> <z> - 创建点");
        Notification.info((Player) sender, "/lwe select - 开启/关闭选点模式 使用下届合金镐选择点");
        Notification.info((Player) sender, "/lwe points - 查看所有点");
        Notification.info((Player) sender, "/lwe fill [点序号A] [点序号B] - (在AB点对角线间放置方块 - 需要手持被放置的方块)");
        Notification.info((Player) sender, "/lwe empty [点序号A] [点序号B] - (破坏AB点对角线间方块 - 需要拥有下届合金镐)");
        Notification.info((Player) sender, "/lwe overlay [点序号A] [点序号B] - (在AB点对角线间放置方块 - 不需要手持被放置的方块)");
        Notification.info((Player) sender, "/lwe drain [点序号A] [点序号B] - (排干AB点对角线间的流体 - 需要背包里有一个海绵)");
        Notification.info((Player) sender, "/lwe cancel - 取消当前任务");
        Notification.info((Player) sender, "/lwe pause - 暂停当前任务");
        Notification.info((Player) sender, "/lwe resume - 恢复当前任务");
    }

    static public boolean out_of_region(Point A, Point B) {
        int minX = Math.min(A.x, B.x);
        int minY = Math.min(A.y, B.y);
        int minZ = Math.min(A.z, B.z);
        int maxX = Math.max(A.x, B.x);
        int maxY = Math.max(A.y, B.y);
        int maxZ = Math.max(A.z, B.z);
        return !(maxX - minX <= LiteWorldEdit.instance.getConfigMgr().getXMax() && maxY - minY <= LiteWorldEdit.instance.getConfigMgr().getYMax() && maxZ - minZ <= LiteWorldEdit.instance.getConfigMgr().getZMax());
    }

}

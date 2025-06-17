package cn.lunadeer.liteworldedit;

import cn.lunadeer.liteworldedit.JobGenerator.Drain;
import cn.lunadeer.liteworldedit.JobGenerator.Empty;
import cn.lunadeer.liteworldedit.JobGenerator.Fill;
import cn.lunadeer.liteworldedit.JobGenerator.OverLay;
import cn.lunadeer.liteworldedit.Jobs.Job;
import cn.lunadeer.liteworldedit.Managers.Cache;
import cn.lunadeer.liteworldedit.Managers.Point;
import cn.lunadeer.liteworldedit.utils.Notification;
import cn.lunadeer.liteworldedit.utils.XLogger;
import cn.lunadeer.liteworldedit.utils.command.Argument;
import cn.lunadeer.liteworldedit.utils.command.CommandManager;
import cn.lunadeer.liteworldedit.utils.command.Option;
import cn.lunadeer.liteworldedit.utils.command.SecondaryCommand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class Commands {

    public Commands(JavaPlugin plugin) {
        new CommandManager(LiteWorldEdit.instance, "lwe");
    }

    private static final String rootPermission = "lwe.command";

    public static SecondaryCommand point = new SecondaryCommand("point", List.of(
            new Option(List.of("1", "2")),
            new Argument("x", true),
            new Argument("y", true),
            new Argument("z", true)
    )) {
        @Override
        public void executeHandler(CommandSender sender) {
            if (!(sender instanceof Player player)) {
                XLogger.error("该命令只能由玩家执行。");
                return;
            }
            Integer index = Integer.parseInt(getArgumentValue(0));
            int x = Integer.parseInt(getArgumentValue(1));
            int y = Integer.parseInt(getArgumentValue(2));
            int z = Integer.parseInt(getArgumentValue(3));
            // 选择的点不允许超过128格范围
            if (Job.notInRange(player, new Location(player.getWorld(), x, y, z))) {
                return;
            }
            Point point = new Point(x, y, z, player);
            if (!Cache.getInstance().getPlayer(player).addPoint(index, point)) {
                Notification.error(player, "点的数量不允许超过20，请使用已有点序号覆盖已有点。");
                return;
            }
            Notification.info(player, "点 {0} 已设置为 {1} {2} {3}。", index, x, y, z);
        }
    }.needChildPermission(rootPermission, PermissionDefault.TRUE).register();

    public static SecondaryCommand select = new SecondaryCommand("select", List.of()) {
        @Override
        public void executeHandler(CommandSender sender) {
            if (!(sender instanceof Player player)) {
                XLogger.error("该命令只能由玩家执行。");
                return;
            }
            Cache.getInstance().getPlayer(player).toggleSelectMode();
        }
    }.needChildPermission(rootPermission, PermissionDefault.TRUE).register();

    public static SecondaryCommand fill = new SecondaryCommand("fill", List.of()) {
        @Override
        public void executeHandler(CommandSender sender) {
            if (!(sender instanceof Player player)) {
                XLogger.error("该命令只能由玩家执行。");
                return;
            }
            Cuboid cuboid = getVector2(player);
            if (cuboid == null) return;
            ItemStack items_in_hand = player.getInventory().getItemInMainHand();
            if (!items_in_hand.getType().isBlock() || items_in_hand.getType() == Material.AIR) {
                Notification.error(player, "你手上没有方块。");
                return;
            }
            Material material = Material.getMaterial(items_in_hand.getType().name());
            Fill.fill(player, player.getWorld(), cuboid, material);
            Notification.info(player, "已添加任务。");
        }
    }.needChildPermission(rootPermission, PermissionDefault.TRUE).register();

    public static SecondaryCommand empty = new SecondaryCommand("empty", List.of()) {
        @Override
        public void executeHandler(CommandSender sender) {
            if (!(sender instanceof Player player)) {
                XLogger.error("该命令只能由玩家执行。");
                return;
            }
            Cuboid cuboid = getVector2(player);
            if (cuboid == null) return;
            Empty.empty(player, player.getWorld(), cuboid);
            Notification.info(player, "已添加任务。");
        }
    }.needChildPermission(rootPermission, PermissionDefault.TRUE).register();

    public static SecondaryCommand overlay = new SecondaryCommand("overlay", List.of()) {
        @Override
        public void executeHandler(CommandSender sender) {
            if (!(sender instanceof Player player)) {
                XLogger.error("该命令只能由玩家执行。");
                return;
            }
            Cuboid cuboid = getVector2(player);
            if (cuboid == null) return;
            ItemStack items_in_hand = player.getInventory().getItemInMainHand();
            if (!items_in_hand.getType().isBlock() || items_in_hand.getType() == Material.AIR) {
                Notification.error(player, "你手上没有方块。");
                return;
            }
            Material material = Material.getMaterial(items_in_hand.getType().name());
            OverLay.overLay(player, player.getWorld(), cuboid, material);
            Notification.info(player, "已添加任务。");
        }
    }.needChildPermission(rootPermission, PermissionDefault.TRUE).register();

    public static SecondaryCommand drain = new SecondaryCommand("drain", List.of()) {
        @Override
        public void executeHandler(CommandSender sender) {
            if (!(sender instanceof Player player)) {
                XLogger.error("该命令只能由玩家执行。");
                return;
            }
            Cuboid cuboid = getVector2(player);
            if (cuboid == null) return;
            Drain.drain(player, player.getWorld(), cuboid);
            Notification.info(player, "已添加任务。");
        }
    }.needChildPermission(rootPermission, PermissionDefault.TRUE).register();

    public static SecondaryCommand cancel = new SecondaryCommand("cancel", List.of()) {
        @Override
        public void executeHandler(CommandSender sender) {
            if (!(sender instanceof Player player)) {
                XLogger.error("该命令只能由玩家执行。");
                return;
            }
            if (Cache.getInstance().getPlayer(player).hasJob()) {
                Cache.getInstance().getPlayer(player).cancelJob();
                Notification.warn(player, "已取消。");
            } else {
                Notification.warn(player, "你没有正在进行的任务。");
            }
        }
    }.needChildPermission(rootPermission, PermissionDefault.TRUE).register();

    public static SecondaryCommand pause = new SecondaryCommand("pause", List.of()) {
        @Override
        public void executeHandler(CommandSender sender) {
            if (!(sender instanceof Player player)) {
                XLogger.error("该命令只能由玩家执行。");
                return;
            }
            if (Cache.getInstance().getPlayer(player).hasJob()) {
                Cache.getInstance().getPlayer(player).pauseJob();
                Notification.warn(player, "已暂停。");
            } else {
                Notification.warn(player, "你没有正在进行的任务。");
            }
        }
    }.needChildPermission(rootPermission, PermissionDefault.TRUE).register();

    public static SecondaryCommand resume = new SecondaryCommand("resume", List.of()) {
        @Override
        public void executeHandler(CommandSender sender) {
            if (!(sender instanceof Player player)) {
                XLogger.error("该命令只能由玩家执行。");
                return;
            }
            if (Cache.getInstance().getPlayer(player).hasJob()) {
                Cache.getInstance().getPlayer(player).resumeJob();
                Notification.info(player, "已恢复。");
            } else {
                Notification.info(player, "你没有正在进行的任务。");
            }
        }
    }.needChildPermission(rootPermission, PermissionDefault.TRUE).register();

    private static Cuboid getVector2(Player player) {
        Map<Integer, Point> points = Cache.getInstance().getPlayer(player).getPoints();
        if (points == null) {
            Notification.error(player, "你没有设置任何点。");
            return null;
        }
        Point pointA = points.get(1);
        Point pointB = points.get(2);
        if (pointA == null || pointB == null) {
            Notification.error(player, "请先使用 /lwe point 1 x y z 和 /lwe point 2 x y z 设置两个点。");
            return null;
        }
        if (out_of_region(pointA, pointB)) {
            Notification.error(player, "选择的区域不可以超过：{0}x{1}x{2}。", Configuration.maximumSize.x, Configuration.maximumSize.y, Configuration.maximumSize.z);
            return null;
        }
        return new Cuboid(pointA.x, pointA.y, pointA.z, pointB.x, pointB.y, pointB.z);
    }


    static public boolean out_of_region(Point A, Point B) {
        int minX = Math.min(A.x, B.x);
        int minY = Math.min(A.y, B.y);
        int minZ = Math.min(A.z, B.z);
        int maxX = Math.max(A.x, B.x);
        int maxY = Math.max(A.y, B.y);
        int maxZ = Math.max(A.z, B.z);
        return !(maxX - minX <= Configuration.maximumSize.x && maxY - minY <= Configuration.maximumSize.y && maxZ - minZ <= Configuration.maximumSize.z);
    }

}

package cn.lunadeer.liteworldedit.Managers;

import cn.lunadeer.liteworldedit.Jobs.Job;
import cn.lunadeer.liteworldedit.Task;
import cn.lunadeer.liteworldedit.utils.Notification;
import cn.lunadeer.liteworldedit.utils.scheduler.Scheduler;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


public class XPlayer {

    private boolean select_mode = false;
    private final Map<Integer, Point> points;
    private final Player player;
    private final JobQueue queue;

    public XPlayer(Player player) {
        Notification.info(player, "当前选点模式: 关闭");
        this.player = player;
        this.points = new HashMap<>();
        this.queue = new JobQueue(player);
        Task task = new Task(this);
        Scheduler.runTaskRepeatAsync(task, 1, 1);
    }

    public boolean isSelectMode() {
        return select_mode;
    }

    public void toggleSelectMode() {
        select_mode = !select_mode;
        if (select_mode) {
            Notification.info(player, "当前选点模式: 开启");
            Notification.info(player, "左键选择第一个点, 右键选择第二个点");
        } else {
            Notification.info(player, "当前选点模式: 关闭");
        }
    }

    public void addJob(Job job) {
        queue.add(job);
    }

    public Job popJob() {
        return queue.pop();
    }

    public void pauseJob() {
        queue.pause();
    }

    public void resumeJob() {
        queue.resume();
    }

    public void cancelJob() {
        queue.cancel();
    }

    public boolean hasJob() {
        return !queue.isEmpty();
    }

    public boolean addPoint(Integer index, Point point) {
        if (points.size() >= 20) {
            return false;
        }
        points.put(index, point);
        return true;
    }

    public Map<Integer, Point> getPoints() {
        return points;
    }
}

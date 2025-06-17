package cn.lunadeer.liteworldedit.Managers;

import cn.lunadeer.liteworldedit.Jobs.Job;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class JobQueue {

    private final LinkedList<Job> queue = new LinkedList<Job>();

    private final BossBar bar = Bukkit.createBossBar("§a§lLiteWorldEdit", BarColor.GREEN, BarStyle.SOLID);

    boolean paused = false;

    int total = 0;

    int ms_last_pop;
    int time_remain_count = 0;

    int time_s_remaining = 0;
    int time_m_remaining = 0;

    Player player;

    public JobQueue(Player player) {
        this.player = player;
        this.bar.addPlayer(player);
        this.bar.setVisible(false);
    }

    public void add(Job job) {
        queue.add(job);
        total++;
    }

    public Job pop() {
        if (queue.isEmpty()) {
            total = 0;
            return null;
        }
        if (paused) {
            return null;
        }
        Job job = queue.pop();
        updateBarProgressBar();
        return job;
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void pause() {
        bar.setColor(BarColor.YELLOW);
        bar.setTitle("§e§lLiteWorldEdit [暂停] 剩余任务: " + queue.size());
        paused = true;
    }

    public void resume() {
        bar.setColor(BarColor.GREEN);
        paused = false;
    }

    public void cancel() {
        queue.clear();
        total = 0;
        bar.setVisible(false);
        paused = false;
    }

    private void updateBarProgressBar() {
        bar.setProgress((total - queue.size()) * 1.0 / total);
        time_remain_count++;
        if (time_remain_count >= 100) {
            time_remain_count = 0;
            int delta_time_ms = (int) System.currentTimeMillis() - ms_last_pop;
            time_s_remaining = queue.size() * delta_time_ms / 100 / 1000;
            time_m_remaining = time_s_remaining / 60;
            time_s_remaining = time_s_remaining % 60;
            ms_last_pop = (int) System.currentTimeMillis();
        }
        bar.setTitle("§a§lLiteWorldEdit [正在运行] 剩余任务: " + queue.size() + " 预计剩余时间: " + time_m_remaining + "分" + time_s_remaining + "秒");
        bar.setVisible(!queue.isEmpty());
    }

}

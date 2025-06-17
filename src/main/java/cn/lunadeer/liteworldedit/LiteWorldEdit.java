package cn.lunadeer.liteworldedit;

import cn.lunadeer.liteworldedit.Managers.Cache;
import cn.lunadeer.liteworldedit.utils.Notification;
import cn.lunadeer.liteworldedit.utils.XLogger;
import cn.lunadeer.liteworldedit.utils.bStatsMetrics;
import cn.lunadeer.liteworldedit.utils.configuration.ConfigurationManager;
import cn.lunadeer.liteworldedit.utils.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class LiteWorldEdit extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        new Notification(this);
        new XLogger(this);
        new Scheduler(this);

        try {
            ConfigurationManager.load(Configuration.class, new File(getDataFolder(), "config.yml"));
        } catch (Exception e) {
            XLogger.error(e);
        }

        new Cache(this);
        new Commands(this);

        Bukkit.getPluginManager().registerEvents(new Events(), this);

        bStatsMetrics metrics = new bStatsMetrics(this, 21436);

        XLogger.info("LiteWorldEdit 已加载");
        XLogger.info("版本: " + getPluginMeta().getVersion());
        XLogger.info("");
        // https://patorjk.com/software/taag/#p=display&f=Big&t=LiteWorldEdit
        XLogger.info(" _      _ _    __          __        _     _ ______    _ _ _   ");
        XLogger.info("| |    (_) |   \\ \\        / /       | |   | |  ____|  | (_) |  ");
        XLogger.info("| |     _| |_ __\\ \\  /\\  / /__  _ __| | __| | |__   __| |_| |_ ");
        XLogger.info("| |    | | __/ _ \\\\/  \\/ / _ \\| '__| |/ _` |  __| / _` | | __|");
        XLogger.info("| |____| | ||  __/\\  /\\  / (_) | |  | | (_| | |___| (_| | | |_ ");
        XLogger.info("|______|_|\\__\\___| \\/  \\/ \\___/|_|  |_|\\__,_|______\\__,_|_|\\__|");
        XLogger.info("");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static LiteWorldEdit instance;
}

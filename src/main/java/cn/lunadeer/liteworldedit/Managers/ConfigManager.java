package cn.lunadeer.liteworldedit.Managers;

import cn.lunadeer.liteworldedit.LiteWorldEdit;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    public ConfigManager() {
        _plugin.saveDefaultConfig();
        reload();
    }

    public void reload() {
        _plugin.reloadConfig();
        _file = _plugin.getConfig();
        _debug = _file.getBoolean("Debug", false);
        _x_max = _file.getInt("MaxX", 64);
        _y_max = _file.getInt("MaxY", 64);
        _z_max = _file.getInt("MaxZ", 64);
        _multiplier = _file.getInt("Multiplier", 1);
        _drop_items = _file.getBoolean("DropItems", false);
        _check_update = _file.getBoolean("CheckUpdate", true);
        _decline_money = _file.getDouble("DeclineMoney", 0);
        _plugin.saveConfig();
    }

    public Boolean isDebug() {
        return _debug;
    }

    public void setDebug(Boolean debug) {
        _debug = debug;
        _file.set("Debug", debug);
        _plugin.saveConfig();
    }

    public Integer getXMax() {
        return _x_max;
    }

    public Integer getYMax() {
        return _y_max;
    }

    public Integer getZMax() {
        return _z_max;
    }

    public Boolean isDropItems() {
        return _drop_items;
    }

    public Integer getMultiplier() {
        return _multiplier;
    }

    public double getDeclineMoney() {
        return _decline_money;
    }

    // 为EconomyManager使用的项目
    public Boolean isEconomyEnabled() {
        return _decline_money > 0;
    }

    public void setDropItems(Boolean drop_items) {
        _drop_items = drop_items;
        _file.set("DropItems", drop_items);
        _plugin.saveConfig();
    }

    public void setMaxSize(Integer x, Integer y, Integer z) {
        _x_max = x;
        _y_max = y;
        _z_max = z;
        _file.set("MaxX", x);
        _file.set("MaxY", y);
        _file.set("MaxZ", z);
        _drop_items = _file.getBoolean("DropItems", false);
        _plugin.saveConfig();
    }

    public Boolean isCheckUpdate() {
        return _check_update;
    }


    private final LiteWorldEdit _plugin = LiteWorldEdit.instance;
    private FileConfiguration _file;

    private Boolean _debug;

    private Integer _x_max;
    private Integer _y_max;
    private Integer _z_max;

    private Boolean _drop_items;

    private Integer _multiplier;

    private Boolean _check_update;

    private Double _decline_money;
}

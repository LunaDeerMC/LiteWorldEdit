package cn.lunadeer.liteworldedit.Managers;

import cn.lunadeer.liteworldedit.LiteWorldEdit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static Cache instance;

    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache(LiteWorldEdit.instance);
        }
        return instance;
    }

    private final Map<String, XPlayer> players;

    public Cache(JavaPlugin plugin) {
        this.players = new HashMap<>();
    }

    public void playerJoin(Player player) {
        players.put(player.getName(), new XPlayer(player));
    }

    public void playerQuit(Player player) {
        players.remove(player.getName());
    }

    public XPlayer getPlayer(Player player) {
        String name = player.getName();
        if (!players.containsKey(name)) playerJoin(player);
        return players.get(name);
    }
}

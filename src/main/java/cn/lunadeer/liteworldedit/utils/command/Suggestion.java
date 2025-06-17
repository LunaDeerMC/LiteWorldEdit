package cn.lunadeer.liteworldedit.utils.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface Suggestion {
    List<String> get(CommandSender sender);
}

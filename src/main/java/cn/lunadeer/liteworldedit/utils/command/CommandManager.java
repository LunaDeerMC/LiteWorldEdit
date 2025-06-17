package cn.lunadeer.liteworldedit.utils.command;

import cn.lunadeer.liteworldedit.utils.Notification;
import cn.lunadeer.liteworldedit.utils.XLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * Manages the registration and execution of commands.
 * <p>
 * CommandManager is a class that manages the command registration and execution.
 * This is used to create command system such as:
 * <blockquote><pre>
 * /rootCommand subCommand <arg1> <arg2> [arg3] ...
 * /rootCommand subCommand2 <arg1> [arg3] ...
 * ...
 * </pre></blockquote>
 */
public class CommandManager implements TabExecutor, Listener {

    private static String rootCommand;
    private final JavaPlugin plugin;
    private Consumer<CommandSender> rootCommandConsumer = null;

    /**
     * Constructs a CommandManager with the specified root command.
     *
     * @param rootCommand The root command to be managed, should start with a slash.
     */
    public CommandManager(JavaPlugin plugin, String rootCommand) {
        this(plugin, rootCommand, null);
    }

    public CommandManager(JavaPlugin plugin, String rootCommand, Consumer<CommandSender> rootCommandConsumer) {
        CommandManager.rootCommand = "/" + rootCommand;
        Objects.requireNonNull(Bukkit.getPluginCommand(rootCommand)).setExecutor(this);
        XLogger.debug("Registered {0} commands.", commands.size());
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.rootCommandConsumer = rootCommandConsumer;

        new SecondaryCommand("help", List.of(new Argument("page", "1"))) {
            @Override
            public void executeHandler(CommandSender sender) {
                printHelp(sender, getArguments().get(0).getValue());
            }
        }.register();

        if (this.rootCommandConsumer == null) {
            this.rootCommandConsumer = (sender) -> {
                printHelp(sender, "1");
            };
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getServer().getOnlinePlayers().isEmpty()) {
            for (String cmd : commands.keySet()) {
                // Unregister all commands that are hidden
                if (commands.get(cmd).isDynamic()) {
                    XLogger.debug("Due to no one online, Unregistering command: {0}", cmd);
                    unregisterCommand(cmd);
                }
            }
        }
    }

    /**
     * Retrieves the root command.
     *
     * @return The root command.
     */
    public static String getRootCommand() {
        return rootCommand;
    }

    private static final Map<String, SecondaryCommand> commands = new HashMap<>();
    private static final Map<String, SecondaryCommand> commandsUsable = new HashMap<>();

    /**
     * Registers a secondary command.
     *
     * @param command The secondary command to be registered.
     */
    public static void registerCommand(SecondaryCommand command) {
        commands.put(command.getCommand(), command);
        if (!command.isDynamic()) {
            commandsUsable.put(command.getCommand(), command);
        }
    }

    /**
     * Unregisters a secondary command.
     *
     * @param command The secondary command to be unregistered.
     */
    public static void unregisterCommand(SecondaryCommand command) {
        commands.remove(command.getCommand());
        commandsUsable.remove(command.getCommand());
    }

    /**
     * Unregisters a secondary command by its name.
     *
     * @param command The name of the secondary command to be unregistered.
     */
    public static void unregisterCommand(String command) {
        commands.remove(command);
        commandsUsable.remove(command);
    }

    /**
     * Retrieves a secondary command by its name.
     *
     * @param command The name of the secondary command.
     * @return The corresponding SecondaryCommand object, or null if not found.
     */
    public static SecondaryCommand getCommand(String command) {
        return commands.get(command);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            if (rootCommandConsumer != null) {
                try {
                    rootCommandConsumer.accept(commandSender);
                } catch (Exception e) {
                    Notification.error(commandSender, e.getMessage());
                }
            }
            return true;
        }
        SecondaryCommand cmd = getCommand(strings[0]);
        if (cmd == null) {
            return true;
        }
        try {
            cmd.run(commandSender, strings);
        } catch (Exception e) {
            Notification.error(commandSender, e.getMessage());
            XLogger.error(e);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return new ArrayList<>(commandsUsable.keySet().stream()
                    .filter(cmd -> cmd.startsWith(strings[0]))
                    .toList());
        }
        SecondaryCommand cmd = getCommand(strings[0]);
        if (cmd == null) {
            return null;
        }
        List<Argument> args = cmd.getArguments();
        if (strings.length - 1 > args.size()) {
            return null;
        }
        for (int i = 1; i < strings.length - 1; i++) {
            args.get(i - 1).setValue(strings[i]);
        }
        for (Argument arg : args) {
            if (arg instanceof ConditionalArgument cond) {
                for (Integer key : cond.getConditionArguments().keySet()) {
                    if (key < strings.length - 2) {
                        cond.setConditionArguments(key, strings[key + 1]);
                    }
                }
            }
        }
        return args.get(strings.length - 2).getSuggestion().get(commandSender);
    }

    private void printHelp(CommandSender sender, String pageStr) {
        if (commands.isEmpty()) {
            return;
        }
        int pageSize = sender instanceof Player ? 10 : 25; // Number of commands per page
        int page;
        try {
            page = Integer.parseInt(pageStr);
        } catch (NumberFormatException e) {
            // If pageStr is not a valid number, default to page 1
            page = 1;
        }
        int totalPages = (int) Math.ceil((double) commandsUsable.size() / pageSize);
        if (page < 1) {
            page = 1;
        }
        if (page > totalPages) {
            page = totalPages;
        }

        String header = "------------ [" + pageStr + "/" + totalPages + "] ------------";
        Notification.info(sender, header);

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, commandsUsable.size());
        int index = 0;
        for (String cmd : commandsUsable.keySet()) {
            if (index >= start && index < end) {
                String line = commandsUsable.get(cmd).getUsage();
                if (!commandsUsable.get(cmd).getDescription().isEmpty()) {
                    line += " - " + commandsUsable.get(cmd).getDescription();
                }
                Notification.info(sender, line);
            }
            index++;
        }
    }
}

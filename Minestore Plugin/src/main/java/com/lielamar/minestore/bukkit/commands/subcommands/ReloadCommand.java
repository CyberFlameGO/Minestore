package com.lielamar.minestore.bukkit.commands.subcommands;

import com.lielamar.lielsutils.commands.Command;
import com.lielamar.minestore.bukkit.Minestore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends Command {

    /**
     * /Minestore Reload command
     * Reloads the Minestore plugin
     */

    private final Minestore plugin;

    public ReloadCommand(String name, Minestore plugin) {
        super(name);

        this.plugin = plugin;
    }

    @Override
    public String getDescription() {
        return "Reloads the Minestore plugin";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "rl" };
    }

    @Override
    public String[] getPermissions() {
        return new String[] { "minestore.commands.reload" };
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!hasPermissions(commandSender)) {
            commandSender.sendMessage(ChatColor.RED + "You don't have enough permissions to do that!");
            return;
        }

        commandSender.sendMessage(ChatColor.AQUA + "Reloading the Minestore plugin!");
        plugin.reloadConfig();
        plugin.onDisable();
        plugin.onEnable();
    }
}

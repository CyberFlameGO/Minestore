package com.lielamar.minestore.bukkit.commands.subcommands;

import com.lielamar.lielsutils.commands.Command;
import com.lielamar.minestore.bukkit.Minestore;
import com.lielamar.minestore.shared.modules.CustomPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AuthCommand extends Command {

    /**
     * /Minestore Authentication command
     * Authenticates a player's purchase and responds to the website
     */

    private final Minestore plugin;

    public AuthCommand(String name, Minestore plugin) {
        super(name);

        this.plugin = plugin;
    }

    @Override
    public String getDescription() {
        return "Authenticates a store purchase";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "authenticate" };
    }

    @Override
    public String[] getPermissions() {
        return new String[] { "minestore.commands.auth" };
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!hasPermissions(commandSender)) {
            commandSender.sendMessage(ChatColor.RED + "You don't have enough permissions to do that!");
            return;
        }

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "You must be a player to do that!");
            return;
        }

        CustomPlayer customPlayer = plugin.getPlayerHandler().getPlayer(((Player) commandSender).getUniqueId());
        if(customPlayer != null) {
            customPlayer.setAuthenticated(true);
            commandSender.sendMessage(ChatColor.AQUA + "You have authenticated your current store purchase!");
        } else {
            commandSender.sendMessage(ChatColor.RED + "Could not authenticate your purchase!");
        }
    }
}

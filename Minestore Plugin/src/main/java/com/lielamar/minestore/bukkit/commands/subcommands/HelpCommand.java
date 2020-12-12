package com.lielamar.minestore.bukkit.commands.subcommands;

import com.lielamar.lielsutils.commands.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Set;

public class HelpCommand extends Command {

    /**
     * /Minestore Help command
     * Displays the the help message of the plugin
     */

    private final Set<Command> commands;

    public HelpCommand(String name, Set<Command> commands) {
        super(name);

        this.commands = commands;
    }

    @Override
    public String getDescription() {
        return "Displays the Minestore help page";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "hlp" };
    }

    @Override
    public String[] getPermissions() {
        return new String[] { "minestore.commands.help" };
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!hasPermissions(commandSender)) {
            commandSender.sendMessage(ChatColor.RED + "You don't have enough permissions to do that!");
            return;
        }

        commandSender.sendMessage(ChatColor.GRAY + "----- " + ChatColor.AQUA + "Minestore" + ChatColor.GRAY + " -----");
        this.commands.forEach(cmd -> {
            if(cmd.hasPermissions(commandSender)) {
                commandSender.sendMessage(ChatColor.AQUA + "• " + cmd.getName() + ChatColor.GRAY + ": " + cmd.getDescription());
                commandSender.sendMessage(ChatColor.AQUA + "Aliases " + Arrays.toString(cmd.getAliases()));
            }
        });
        commandSender.sendMessage(ChatColor.GRAY + "----- --------- -----");
    }
}

package com.lielamar.minestore.bukkit.commands;

import com.lielamar.lielsutils.commands.Command;
import com.lielamar.minestore.bukkit.Minestore;
import com.lielamar.minestore.bukkit.commands.subcommands.AuthCommand;
import com.lielamar.minestore.bukkit.commands.subcommands.HelpCommand;
import com.lielamar.minestore.bukkit.commands.subcommands.ReloadCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MinestoreCommand implements CommandExecutor {

    /**
     * Handles the main /Minestore command and its subcommands
     */

    private final Minestore plugin;
    private final Set<Command> commands;

    private final String MAIN = "minestore";
    private final String AUTH = "auth";
    private final String RELOAD = "reload";
    private final String HELP = "help";

    public MinestoreCommand(Minestore plugin) {
        this.plugin = plugin;
        this.commands = new HashSet<>();

        this.setupCommands();
    }

    /**
     * Sets up all subcommands
     */
    private void setupCommands() {
        Objects.requireNonNull(this.plugin.getCommand(MAIN)).setExecutor(this);

        this.commands.add(new AuthCommand(AUTH, this.plugin));
        this.commands.add(new ReloadCommand(RELOAD, this.plugin));
        this.commands.add(new HelpCommand(HELP, this.commands));
    }

    /**
     * Returns a {@link Command} object related to the given name
     *
     * @param name   Name/Alias of the command to return
     * @return       The command object from the commands set
     */
    public Command getCommand(String name) {
        for(Command cmd : this.commands) {
            if(cmd.getName().equalsIgnoreCase(name))
                return cmd;

            for(String s : cmd.getAliases()) {
                if(s.equalsIgnoreCase(name))
                    return cmd;
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender cs, @Nonnull org.bukkit.command.Command cmd, @Nonnull String cmdLabel, @Nonnull String[] args) {
        if(cmd.getName().equalsIgnoreCase(MAIN)) {
            if(args.length == 0) {
                getCommand(HELP).execute(cs, null);
                return false;
            }

            Command command = getCommand(args[0]);

            if(command == null) {
                getCommand(HELP).execute(cs, null);
                return false;
            }

            String[] newArguments = new String[args.length - 1];
            System.arraycopy(args, 1, newArguments, 0, newArguments.length);

            command.execute(cs, newArguments);
            return true;
        }
        return false;
    }
}
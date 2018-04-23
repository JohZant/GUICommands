package com.arch.guicommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {

    private GUICommands plugin;

    public CommandListener(GUICommands plugin){
        this.plugin = plugin;
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //test if there are any arguments
        //if there are none, just give back the version
        if (/*args.length == 0*/true){
            sender.sendMessage(plugin.prefix + ChatColor.GREEN + "Version " + plugin.getDescription().getVersion());
            return true;
        }

        Player player = null;
        Boolean isPlayer = false;
        if (sender instanceof Player){
            player = (Player) sender;
            isPlayer = true;
        }

        //test if we are reloading
        if (args[0].equalsIgnoreCase("reload")){
            //trying to reload
            //test for permissions
            if (isPlayer){
                if (!player.hasPermission("guicommands.reload")){
                    sender.sendMessage(plugin.prefix + ChatColor.RED + "You don't have permission to use that command.");
                    return true;
                }
            }

            plugin.ConfigLoad();

            sender.sendMessage(plugin.prefix + ChatColor.GREEN + "Reloaded");
        }
        else{
            sender.sendMessage(plugin.prefix + ChatColor.RESET + "That is an unknown command.");

        }
        return true;
    }
}

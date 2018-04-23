package com.arch.guicommands;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class Logger {

    private GUICommands plugin;

    /*Constructor*/
    public Logger(GUICommands plugin) {
        this.plugin = plugin;
    }

    public Logger(GUICommands plugin, String message){
        this.plugin = plugin;
        log(message);
    }

    /*logger for GUICommands*/
    public void log(String message) {
        String logMessage = ChatColor.GOLD + "[GUICommands] " + ChatColor.RESET + message;//adds plugin name to front of log
        /*Sends message to log */
        plugin.getServer().getConsoleSender().sendMessage(logMessage);

    }
}

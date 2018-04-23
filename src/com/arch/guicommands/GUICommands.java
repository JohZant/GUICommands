package com.arch.guicommands;


import com.arch.guicommands.Menu.Item;
import com.arch.guicommands.Menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for GUICommands Plugin
 */
public class GUICommands extends JavaPlugin {
    Config config;//config file
    Logger console;//logging object
    List<Menu> menuList;//list of menus
    List<CommandMemory> cmdMemoryList;//list of args and commands to store

    final String prefix = ChatColor.GOLD + "[GUICommands]";

    /*Fired when the server loads up*/
    @Override
    public void onEnable() {
        //initialise logger
        console = new Logger(this);
        menuList = new ArrayList<Menu>();
        cmdMemoryList = new ArrayList<CommandMemory>();

        if (!ConfigLoad()) {
            //config did not load correctly
            console.log("Config was not able to be loaded.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        /**/

        /*register Listener */
        getServer().getPluginManager().registerEvents(new GUICommandsListener(this), this);

        /*Register command */
        this.getCommand("guicommands").setExecutor(new CommandListener(this));
    }

    /*Fired when the server stops and disables plugins*/
    @Override
    public void onDisable() {
        /*  */
    }

    /*Loads Config */
    public boolean ConfigLoad() {
        //create config file if not already created
        this.saveDefaultConfig();

        //load config
        config = new Config(this);
        config.loadConfig();
        return true;
    }

    /* Finds the menu that we should be using */
    protected Menu findMenuByCommand(String[] cmdArray) {

        //var to hold whether or not the menu matches
        boolean menuMatches;

        //go through each menu to find the right command
        for (Menu m : menuList) {
            String[] tempMArray = m.getUsage().split(" ");

            //reset menuMatches
            menuMatches = true;

            //test if the first command is the same
            if (tempMArray[0].toLowerCase().equals(cmdArray[0].toLowerCase())) {
                //this could be the one, boiseses (Yes, I am a nerd, i am sorry.)

                //test if the commands both have the same amount of arguments
                if (tempMArray.length == cmdArray.length) {
                    //GETTING CLOSER
                    //last test is to make sure that we have the same non-arg params in the same order
                    for (int i = 0; i < cmdArray.length; i++) {
                        //if the tempMArray param starts with "$", it is an argument and will not match
                        if (tempMArray[i].charAt(0) != '$') {
                            //not an arg, test if they are the same
                            if (!tempMArray[i].equalsIgnoreCase(cmdArray[i])){
                                //not the same
                                menuMatches = false;
                                break;
                            }
                        }
                    }

                    //if menuMatches is still true, after this, we ahve found out menu
                    if (menuMatches){
                        return m;
                    }
                }

            }
        }
        return null;
    }

    public void MessagePlayer(Menu m, Player p, String msg) {
        p.sendMessage(m.getPrefix() + ChatColor.RESET + msg);
    }
}

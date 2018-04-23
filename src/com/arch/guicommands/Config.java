package com.arch.guicommands;

import com.arch.guicommands.Menu.Argument;
import com.arch.guicommands.Menu.Item;
import com.arch.guicommands.Menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Config {
    private GUICommands plugin;
    private boolean debug;//whether to debug verbose
    //private boolean bungeemode;//whether or not server is using bungeecord

    public Config(GUICommands plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        FileConfiguration localConfigFile = plugin.getConfig();//load up config file


        debug = localConfigFile.getBoolean("debug");//get debug value and hope to the lord all-mighty that they have chosen false
        if (debug) {
            plugin.console.log("Debug Mode is on. Goodluck!");
        }
        /*bungeemode = localConfigFile.getBoolean("bungeemode");//assign bungeemode. Used to see if we need to communicate to bungee for anything like player list
        if (bungeemode) {
            plugin.console.log("Bungeemode was set to true. Opening Channels.");
        }*/

        //get menus
        Set<String> keys = localConfigFile.getConfigurationSection("menus").getKeys(false);
        for (String key : keys) {
            if (loadGUI(localConfigFile, key)) {
                plugin.console.log("Loaded Menu: " + ChatColor.GREEN + key);
            } else {
                plugin.console.log(ChatColor.DARK_RED + "Failed to load menu: " + ChatColor.GRAY + key);
            }
        }


    }

    /* Load GUI Menu into list */
    private boolean loadGUI(FileConfiguration localConfigFile, String key) {
        Menu tempMenu = new Menu();//object to add to menuList

        //set path
        String configPath = "menus." + key + ".";

        tempMenu.setMenuName(key);//sets a menu name

        //set prefix
        tempMenu.setPrefix(localConfigFile.getString(configPath + "prefix"));

        //set permission
        tempMenu.setPermission(localConfigFile.getString(configPath + "permission"));

        //set usage for usage warning
        tempMenu.setUsage(localConfigFile.getString(configPath + "usage"));

        //set rows in GUI
        tempMenu.setRows(localConfigFile.getInt(configPath + "rows"));

        //set arguments
        List<Argument> argsList = new ArrayList<Argument>();
        String argPath = configPath + "arguments.";//yml path for args
        //get the section of all the args that are for this menu
        Set<String> args = localConfigFile
                .getConfigurationSection(configPath + "arguments")
                .getKeys(false);
        //for each argument found in config, put the details into an array
        for (String arg : args) {
            Argument tempArg = new Argument();
            tempArg.setArgNum(Integer.parseInt(arg.substring(arg.length() - 1)));
            tempArg.setName(localConfigFile.getString(argPath + arg + ".name"));
            argsList.add(tempArg);
        }
        tempMenu.setArguments(argsList);//add arguments for menu to the menu object


        //set Items
        List<Item> itemList = new ArrayList<Item>();
        String itemsPath = configPath + "Items";
        Set<String> menuItems = localConfigFile
                .getConfigurationSection(itemsPath)
                .getKeys(false);
        for (String item : menuItems) {
            Item tempItem = new Item();
            tempItem.formatItem(localConfigFile, itemsPath + "." + item);
            itemList.add(tempItem);
        }
        tempMenu.setItems(itemList);

        plugin.menuList.add(tempMenu);//add menu to list
        return true;//nothing has gone wrong, so return true
    }
}

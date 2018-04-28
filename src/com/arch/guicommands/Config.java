package com.arch.guicommands;

import com.arch.guicommands.Menu.Argument;
import com.arch.guicommands.Menu.Item;
import com.arch.guicommands.Menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Config {
    private GUICommands plugin;
    private boolean debug;//whether to debug verbose
    //private boolean bungeemode;//whether or not server is using bungeecord

    public Config(GUICommands plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.reloadConfig();
        FileConfiguration localConfigFile = plugin.getConfig();//load up config file


        debug = localConfigFile.getBoolean("debug");//get debug value and hope to the lord all-mighty that they have chosen false
        if (debug) {
            plugin.console.log("Debug Mode is on. Goodluck!");
        }

        //get menus
        plugin.menuList = new ArrayList<Menu>();//initialise object list
        FileConfiguration tempConfig = null;//var to hold config
        boolean loadSuccess;
        boolean usingFile;


        Set<String> keys = localConfigFile.getConfigurationSection("menus").getKeys(false);
        for (String key : keys) {
            tempConfig = null;//null for testing
            usingFile = false;
            //test if we are looking for a file or reading menu form config
            if (localConfigFile.contains("menus." + key + ".file")){
                //test that file is true
                usingFile = localConfigFile.getBoolean("menus." + key + ".file");
                if(usingFile) {
                    //get file config
                    tempConfig = getYAML(key);
                }
            }

            //test if we got YAML back from file
            if (!usingFile){
                //not using a file, load menu form config
                loadSuccess = loadGUI(localConfigFile, key);
            }
            else if (tempConfig != null){
                //using file, load menu from file
                loadSuccess = loadGUI(tempConfig, key);
            }
            else{
                //config set to using file,
                // but the file was either null or invalid
                loadSuccess = false;
            }

            if (loadSuccess) {
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
        tempMenu.setPrefix(ChatColor.translateAlternateColorCodes('&',localConfigFile.getString(configPath + "prefix")));

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
            if(tempItem.formatItem(localConfigFile, itemsPath + "." + item)) {
                //item was successfully created, add to list
                itemList.add(tempItem);
            }
            else{
                //there was an issue with the item. Let the console know
                plugin.console.log("There was an issue with menu "
                        + ChatColor.YELLOW + key
                        + ChatColor.RESET + ". Item "
                        + ChatColor.LIGHT_PURPLE + item
                        + ChatColor.RESET + " was not able to be loaded."
                );
            }
        }
        tempMenu.setItems(itemList);

        plugin.menuList.add(tempMenu);//add menu to list
        return true;//nothing has gone wrong, so return true
    }

    private FileConfiguration getYAML(String key){
        FileConfiguration c = new YamlConfiguration();
        try {
            File f = new File(plugin.getDataFolder(), "menu_" + key + ".yml");
            c.load(f);
            return c;
        }
        catch (IOException ex){
            plugin.console.log(ex.getMessage());
            plugin.console.log(ChatColor.RED + "IOError while reading menu_" + key.toLowerCase());
        }
        catch (InvalidConfigurationException ex){
            plugin.console.log(ex.getMessage());
            plugin.console.log("menu_" + key.toLowerCase() + " has issues with its configuration.");
        }

        return null;
    }
}

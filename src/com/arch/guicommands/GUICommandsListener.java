package com.arch.guicommands;

import com.arch.guicommands.Menu.Argument;
import com.arch.guicommands.Menu.Item;
import com.arch.guicommands.Menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Listener For Custom Gui Commands
 */
public class GUICommandsListener implements Listener {

    GUICommands plugin;

    public GUICommandsListener(GUICommands plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCommandExecute(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().substring(1);//get raw command
        //split into array
        String[] cmdarray = cmd.split(" ");//splits command into array for comparing to menu usage

        Menu menu = plugin.findMenuByCommand(cmdarray);
        if (menu == null) {
            //menu is null so not a command for us
            return;
        }
        //this is our command
        event.setCancelled(true);

        Player player = event.getPlayer();//get player info

        //make sure that player has permission
        if (!player.hasPermission(menu.getPermission())) {
            //player does not have permission
            //promptly let them know
            //the dumbdawgs
            plugin.MessagePlayer(menu, player, ChatColor.RED + "You do not have permission to use that command.");
            return;
        }

        //get the args and put into an array
        List<Argument> argsArray = new ArrayList<Argument>();
        String[] usageArray = menu.getUsage().split(" ");

        /*Go through the entered command and pick out the args to put into argsArray*/
        for (int i = 0; i < usageArray.length; i++) {
            if (usageArray[i].charAt(0) == '$') {
                Argument tempArg = new Argument();//set up a temp object

                tempArg.setName(cmdarray[i]);//put the entered value into he object
                tempArg.setArgNum(Integer.parseInt(usageArray[i].substring(usageArray[i].length() - 1)));//add arg number to object

                argsArray.add(tempArg);
            }
        }

        //set up memory for user command
        CommandMemory cMemory = new CommandMemory();
        cMemory.uuid = player.getUniqueId().toString();
        cMemory.args = argsArray.toArray(new Argument[0]);

        //test if user already ahs something logged
        Collection<CommandMemory> userCmdMemory = plugin.cmdMemoryList.stream().filter((cm) -> cm.uuid.equalsIgnoreCase(cMemory.uuid)).collect(Collectors.toList());
        //if there is an existing record with the users name.... remove them from the list
        if (userCmdMemory.size() > 0) {
            plugin.cmdMemoryList.removeAll(userCmdMemory);
        }

        plugin.cmdMemoryList.add(cMemory);//add our new memory to list


        menu.openMenu(player, plugin);

    }

    /* This is the start of the running of commands */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //test if this is one of our menus
        Menu menu = plugin.menuList.stream()
                .filter((m) -> m.getMenuName().equalsIgnoreCase(event.getClickedInventory().getName()))
                .findFirst()
                .orElse(null);

        if (menu == null) {
            //not our menu
            return;
        }

        //it is our GUI
        //stop moving item straight away
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        //get item through slot that was clicked
        int clickedSlot = event.getSlot();
        Item item = menu.getItems().stream().filter((i) -> i.getSlot() == clickedSlot).findFirst().orElse(null);


        //if item clicked is null..... something stuffed up... that should not happen, but return and put an error into console
        if (item == null) {
            //this would happen most likely because someone was trying to put something into the gui.
            player.closeInventory();
            return;
        }
        //get stored arrays
        CommandMemory savedCmds = plugin.cmdMemoryList.stream()
                .filter((cm) -> cm.uuid.equalsIgnoreCase(player.getUniqueId().toString()))
                .findFirst()
                .orElse(null);

        //check that we actually have the command saved
        if (savedCmds == null) {
            //command has not been saved
            plugin.console.log("Unable to find saved ARgs");
            return;
        }

        String CommandToRun;//this string will hold every command

        //execute console commands
        for (String consoleCMD : item.getCommands()) {
            CommandToRun = consoleCMD.replace("{player}", player.getName());//puts this player in the command
            //add in args
            for (Argument a : savedCmds.args) {
                CommandToRun = CommandToRun.replace("$arg" + a.getArgNum(), a.getName());
            }

            //run command
            if (CommandToRun.contains("[console]")) {
                //run as console
                plugin.getServer()
                        .dispatchCommand(
                                plugin.getServer().getConsoleSender(),
                                CommandToRun.replace("[console]", "").trim());
            } else if (CommandToRun.contains("[player]")) {
                //run as player
                player.performCommand(CommandToRun.replace("[player]", "").trim());
            } else if (CommandToRun.contains("[message]")) {
                //message caller
                player.sendMessage(menu.getPrefix()
                        + ChatColor.RESET
                        + CommandToRun.replace("[message]", "").trim());
            } else if (CommandToRun.contains("[close]")) {
                //close menu
                player.closeInventory();//close invent for player
                plugin.cmdMemoryList.remove(savedCmds);
            } else if (CommandToRun.contains("[refresh]")) {
                //refresh item that was clicked
                menu.refreshMenu(event);
            } else {
                //don't know how to handle.... let console know
                plugin.console.log("Unknown command: " + CommandToRun);
                //let player know
                player.sendMessage(menu.getPrefix() + ChatColor.RED + "There was a problem with running your command. Please report this.");
            }
        }


    }
}

package com.arch.guicommands.Menu;

import com.arch.guicommands.GUICommands;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private String MenuName;//name of the menu
    private String Prefix;//prefix when sending messages to player
    private String Permission;
    private String Usage;
    private int Rows;//number of rows in GUI
    private List<Argument> Arguments;//object of arguments
    private List<Item> Items;//object of items to show in GUI

    public String getMenuName() {
        return MenuName;
    }

    public String getPermission() {
        return Permission;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }

    public void setPermission(String permission) {
        Permission = permission;
    }

    public String getUsage() {
        return Usage;
    }

    public List<Argument> getArguments() {
        return Arguments;
    }

    public List<Item> getItems() {
        return Items;
    }

    public int getRows() {
        return Rows;
    }

    public void setArguments(List<Argument> arguments) {
        Arguments = arguments;
    }

    public void setUsage(String usage) {
        Usage = usage;
    }

    public void setItems(List<Item> items) {
        Items = items;
    }

    public void setRows(int rows) {
        Rows = rows;
    }

    private GUICommands plugin;
    private Player player;

    public void openMenu(Player player, GUICommands plugin) {
        this.plugin = plugin;
        this.player = player;

        //set number of slots for menu
        int guiSlots = getRows() * 9 > 54 ? 54 : getRows() * 9;//if the slots is greater than 54, just make the slots to be 54

        // Here we create our "inventory"
        Inventory gui = Bukkit.getServer().createInventory(player, guiSlots, MenuName);

        //go through menu's items and populate grid
        for (Item item : getItems()) {

            ItemStack guiStack = new ItemStack(item.getMaterial(), item.getAmount(), item.getData());//make stack of item

            //set lore
            ItemMeta meta = guiStack.getItemMeta();
            List<String> lore = new ArrayList<String>();
            //placeholder-ize lore
            for (String l : item.getLore()) {
                lore.add(getPlaceholderString(l));
            }
            meta.setLore(lore);

            //set name
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getPlaceholderString(item.getDisplayName())));

            guiStack.setItemMeta(meta);

            gui.setItem(item.getSlot(), guiStack);
        }

        //Here opens the inventory
        player.openInventory(gui);
    }

    public void refreshMenu(InventoryClickEvent event) {

        Inventory gui = event.getClickedInventory();

        for (Item item : getItems()) {

            ItemStack guiStack = new ItemStack(item.getMaterial(), item.getAmount(), item.getData());//make stack of item

            //set lore
            ItemMeta meta = guiStack.getItemMeta();
            List<String> lore = new ArrayList<String>();
            //placeholder-ize lore
            for (String l : item.getLore()) {
                lore.add(getPlaceholderString(l));
            }
            meta.setLore(lore);

            //set name
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getPlaceholderString(item.getDisplayName())));

            guiStack.setItemMeta(meta);

            gui.setItem(item.getSlot(), guiStack);
        }
    }

    private String getPlaceholderString(String str) {
        //check if we are using placeholder api
        if (plugin.placeholderAPIEnabled) {
            str = PlaceholderAPI.setPlaceholders(player, str);
        }
        return str;
    }

}

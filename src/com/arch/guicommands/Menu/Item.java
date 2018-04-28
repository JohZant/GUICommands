package com.arch.guicommands.Menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private Material _Material;//Mc Mat
    private short Data;//data of Mat
    private int Amount;//number of mats to show
    private int Slot;//which GUI slot to put item in
    private String DisplayName;//display name of item in GUI
    private List<String> Lore;//lore of item in GUI.
    private List<String> Commands;//which commands to run on item click

    public int getAmount() {
        return Amount;
    }

    public short getData() {
        return Data;
    }

    public Material getMaterial() {
        return _Material;
    }

    public void setData(short data) {
        Data = data;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public void setMaterial(Material material) {
        _Material = material;
    }

    public List<String> getCommands() {
        return Commands;
    }

    public int getSlot() {
        return Slot;
    }

    public List<String> getLore() {
        return Lore;
    }

    public void setSlot(int slot) {
        Slot = slot;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setCommands(List<String> commands) {
        Commands = commands;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public void setLore(List<String> lore) {
        Lore = lore;
    }


    /* populates this from config*/
    public boolean formatItem(FileConfiguration localConfig, String key) {

        //populate material
        String matName = localConfig.getString(key + ".material").toUpperCase();
        _Material = Material.matchMaterial(matName);
        if (_Material == null) {
            //material does not exist
            return false;
        }

        //set material data
        Data = (short) localConfig.getInt(key + ".data");

        //get amount
        Amount = localConfig.getInt(key + ".amount");

        //get slot number
        Slot = localConfig.getInt(key + ".slot");

        //get display name
        DisplayName = ChatColor.translateAlternateColorCodes(
                '&',
                localConfig.getString(key + ".display_name")
        );

        //get lore
        Lore = new ArrayList<String>();
        String[] loreList = localConfig.getStringList(key + ".lore").toArray(new String[0]);
        for (int i = 0; i < loreList.length; i++) {
            Lore.add(ChatColor.translateAlternateColorCodes('&', loreList[i]));
        }


        //get commands - player
        Commands = localConfig.getStringList(key + ".commands");

        return true;
    }
}

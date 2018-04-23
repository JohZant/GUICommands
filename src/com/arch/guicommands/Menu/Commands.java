package com.arch.guicommands.Menu;

import java.util.List;

public class Commands {
    private List<String> Console;//commands run by console
    private List<String> Player;//commands run by player

    public List<String> getConsole() {
        return Console;
    }

    public List<String> getPlayer() {
        return Player;
    }

    public void setConsole(List<String> console) {
        Console = console;
    }

    public void setPlayer(List<String> player) {
        Player = player;
    }
}

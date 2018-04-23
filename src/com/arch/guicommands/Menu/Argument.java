package com.arch.guicommands.Menu;

public class Argument {
    private int ArgNum;
    private String Name;

    private boolean Required;//NOT IMPLEMENTED

    public int getArgNum() {
        return ArgNum;
    }

    private boolean isRequired() {
        return Required;
    }

    public void setArgNum(int argNum) {
        ArgNum = argNum;
    }

    private void setRequired(boolean required) {
        Required = required;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

package com.nastalgua.storage;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.nastalgua.storage.commands.StorageCommand;
import com.nastalgua.storage.events.GUIBlocker;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        System.out.println("ChestDepth is enabled!");

        getCommand("storage").setExecutor(new StorageCommand());

        Bukkit.getPluginManager().registerEvents(new GUIBlocker(), this);

    }

    @Override
    public void onDisable() {
        System.out.println("ChestDepth is disabled...");
    }

}

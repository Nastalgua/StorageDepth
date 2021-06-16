package com.nastalgua.storage;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.nastalgua.storage.commands.StorageCommand;
import com.nastalgua.storage.events.GUIListener;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        System.out.println("StorageDepth is enabled!");

        getCommand("storage").setExecutor(new StorageCommand());

        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);

    }

    @Override
    public void onDisable() {
        System.out.println("StorageDepth is disabled...");
    }

}

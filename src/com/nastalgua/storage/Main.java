package com.nastalgua.storage;

import com.nastalgua.storage.events.Open;
import com.nastalgua.storage.events.Placement;
import com.nastalgua.storage.helpers.Pagination;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.nastalgua.storage.commands.StorageCommand;
import com.nastalgua.storage.events.GUIListener;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    public static List<OfflinePlayer> testPlayers = new ArrayList<OfflinePlayer>();

    @Override
    public void onEnable() {

        System.out.println("StorageDepth is enabled!");

        getCommand("storage").setExecutor(new StorageCommand());

        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
        Bukkit.getPluginManager().registerEvents(new Open(), this);
        Bukkit.getPluginManager().registerEvents(new Placement(), this);

        testPlayers.add(Bukkit.getOfflinePlayer("T3Rex"));
        testPlayers.add(Bukkit.getOfflinePlayer("Kauo"));
        testPlayers.add(Bukkit.getOfflinePlayer("DudkeL"));
        testPlayers.add(Bukkit.getOfflinePlayer("Holji"));
        testPlayers.add(Bukkit.getOfflinePlayer("Sadrake"));
        testPlayers.add(Bukkit.getOfflinePlayer("b1ui"));
        testPlayers.add(Bukkit.getOfflinePlayer("UwUDucky_"));
        testPlayers.add(Bukkit.getOfflinePlayer("FullpowerBoy"));
        testPlayers.add(Bukkit.getOfflinePlayer("yaazy"));
        testPlayers.add(Bukkit.getOfflinePlayer("UggaMcBugga"));

    }

    @Override
    public void onDisable() {
        System.out.println("StorageDepth is disabled...");
    }

    public static List<OfflinePlayer> getOnlinePlayers() {
        List<OfflinePlayer> players = new ArrayList<>();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            players.add(player);
        }

        return players;
    }
}

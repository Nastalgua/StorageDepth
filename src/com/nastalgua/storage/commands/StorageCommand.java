package com.nastalgua.storage.commands;

import com.nastalgua.storage.Main;
import com.nastalgua.storage.events.Open;
import com.nastalgua.storage.events.Placement;
import com.nastalgua.storage.helpers.GUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StorageCommand implements CommandExecutor {

    public static List<String> checkingStorage = new ArrayList<>();
    public static Block currentBlock = null;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        Player player = (Player) commandSender;

        // check if looking at storage block
        Block cBlock = player.getTargetBlock((Set<Material>) null, 10);
        currentBlock = cBlock;

        if (!isStorageBlock(cBlock.getType())) {
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Please look at a storage block...");
            return false;
        }

        TileState state = (TileState) cBlock.getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), Placement.KEY_NAME);

        if (!container.has(key, PersistentDataType.STRING)) return false;
        if (!container.get(key, PersistentDataType.STRING).contains(player.getUniqueId().toString())) {
            player.sendMessage(Open.blockMsg);
            return false;
        }

        String subCmd;

        if (args.length > 0) {
            subCmd = args[0].toLowerCase();
        } else {
            subCmd = "";
            GUI.openChestGUI(player);
        }

        switch (subCmd) {
            case "add":
                break;
            case "remove":
                break;
        }

        return false;
    }

    public static boolean isStorageBlock(Material block) {
        return  block == Material.CHEST ||
                (block.ordinal() >= Material.WHITE_SHULKER_BOX.ordinal() && block.ordinal() <= Material.BLACK_SHULKER_BOX.ordinal()) ||
                block == Material.BARREL ||
                block == Material.TRAPPED_CHEST ||
                block == Material.CHEST_MINECART;
    }

    public static boolean alreadyAdded(String playerUUID) {
        TileState state = (TileState) currentBlock.getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), Placement.KEY_NAME);

        if (!container.has(key, PersistentDataType.STRING)) return false;

        String str = container.get(key, PersistentDataType.STRING);

        return str.contains(playerUUID);
    }

    public static void addPlayer(Player fromPlayer, String toPlayerName, String toPlayerUUID) {
        TileState state = (TileState) StorageCommand.currentBlock.getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), Placement.KEY_NAME);

        String str = container.get(key, PersistentDataType.STRING) + " " + toPlayerUUID;

        container.set(key, PersistentDataType.STRING, str);
        state.update();

        fromPlayer.sendMessage("! Added " + toPlayerName + " !");
        fromPlayer.closeInventory();
    }

    public static void removePlayer(Player fromPlayer, String toPlayerName, String toPlayerUUID) {
        TileState state = (TileState) StorageCommand.currentBlock.getState();

        PersistentDataContainer container = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), Placement.KEY_NAME);

        String str = container.get(key, PersistentDataType.STRING).replace(toPlayerUUID, "");

        container.set(key, PersistentDataType.STRING, str);
        state.update();

        fromPlayer.sendMessage("! Removed " + toPlayerName + " !");
        fromPlayer.closeInventory();

    }

}

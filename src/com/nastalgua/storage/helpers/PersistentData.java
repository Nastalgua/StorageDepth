package com.nastalgua.storage.helpers;

import com.nastalgua.storage.Main;
import com.nastalgua.storage.commands.StorageCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataContainer;

public class PersistentData {

    public PersistentDataContainer container;
    public NamespacedKey key;
    public TileState state;

    public PersistentData(String keyName) {
        this.state = (TileState) StorageCommand.currentBlock.getState();
        this.container = this.state.getPersistentDataContainer();
        this.key = new NamespacedKey(Main.getPlugin(Main.class), keyName);

    }

}

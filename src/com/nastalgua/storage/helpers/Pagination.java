package com.nastalgua.storage.helpers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> {

    static final String IGNRight = "MHF_ArrowRight";
    static final String IGNLeft = "MHF_ArrowLeft";

    static final int PAGE_SLOT_COUNT = 6;

    private List<T> list;
    public int currentPage = 1;

    public static ItemStack arrowRight = new ItemStack(Material.PLAYER_HEAD, 1);
    public static ItemStack arrowLeft = new ItemStack(Material.PLAYER_HEAD, 1);

    public Pagination(List<T> list) {
        this.list = list;
    }

    public int pageCount() {
        return (this.list.size() / 7) + ((this.list.size() % 7 != 0) ? 1 : 0);
    }

    public void updateList(List<T> list) {
        this.list = list;
    }

    public <T> void loadPage(Inventory inv, Material material, String displayName, List<String> lore, Player player) {
        int slot = 10, count = 0;

        // right
        if (currentPage < pageCount()) {
            List<String> rightLore = new ArrayList<>();
            rightLore.add("View right page...");

            SkullMeta rightMeta = (SkullMeta) arrowRight.getItemMeta();

            assert rightMeta != null;
            rightMeta.setOwningPlayer(Bukkit.getOfflinePlayer(IGNRight)); // is deprecated, but simple solution

            rightMeta.setDisplayName("Next Page");
            rightMeta.setLore(rightLore);
            arrowRight.setItemMeta(rightMeta);

            inv.setItem(inv.getSize() - 1, arrowRight);
        }

        // left
        if (currentPage > 1) {
            List<String> leftLore = new ArrayList<>();
            leftLore.add("View left page...");

            SkullMeta leftMeta = (SkullMeta) arrowLeft.getItemMeta();

            assert leftMeta != null;
            leftMeta.setOwningPlayer(Bukkit.getOfflinePlayer(IGNLeft)); // is deprecated, but simple solution

            leftMeta.setDisplayName("Previous Page");
            leftMeta.setLore(leftLore);

            arrowLeft.setItemMeta(leftMeta);

            inv.setItem(inv.getSize() - 9, arrowLeft);
        }

        int startIndex = (this.currentPage - 1) * PAGE_SLOT_COUNT;
        int endIndex = Math.min(this.currentPage * 7, this.list.size());

        System.out.println(startIndex);
        System.out.println(endIndex);

        for (Object obj : this.list.subList(startIndex, endIndex)) {

            ItemStack stack = new ItemStack(material, 1);

            ItemMeta meta = stack.getItemMeta();

            if (player != null) {
                ((SkullMeta) meta).setOwningPlayer((OfflinePlayer) obj);
            }

            meta.setDisplayName(displayName);
            meta.setLore(lore);

            stack.setItemMeta(meta);

            inv.setItem(slot, stack);

            slot++;
            count++;

            if (count == 7) {
                slot += 2;
                count = 0;
            }

        }
    }

}

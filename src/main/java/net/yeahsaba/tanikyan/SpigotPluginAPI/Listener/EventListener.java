package net.yeahsaba.tanikyan.SpigotPluginAPI.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.Plugin;

import net.yeahsaba.tanikyan.SpigotPluginAPI.Gui.GuiMenuManager;

public class EventListener implements Listener {
	public EventListener(Plugin plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	//メニュー制御 ここから
	//メニューのクリック
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(GuiMenuManager.isOpenMenu((Player) e.getWhoClicked())){
			e.setCancelled(true);
			if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null && e.getWhoClicked() instanceof Player &&
					!e.getClickedInventory().getType().equals(InventoryType.PLAYER)){
				GuiMenuManager.ClickMenu((Player) e.getWhoClicked(), e.getClickedInventory().getItem(e.getSlot()));
			}
		}
	}

	//メニューのクローズ
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		if(e.getPlayer() instanceof Player){
			Player p = (Player) e.getPlayer();
			//メニューを開いていた場合リストから削除
			if(GuiMenuManager.isOpenMenu(p)) GuiMenuManager.RemoveOpenMenu(p);
		}
	}

	//アイテム投げ捨てをキャンセル
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e){
		if(GuiMenuManager.isOpenMenu(e.getPlayer())) e.setCancelled(true);
	}
}

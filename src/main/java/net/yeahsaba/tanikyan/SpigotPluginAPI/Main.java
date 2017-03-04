package net.yeahsaba.tanikyan.SpigotPluginAPI;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.yeahsaba.tanikyan.SpigotPluginAPI.Listener.EventListener;

public class Main extends JavaPlugin {
	@Override
	public void onEnable(){
		//バージョンチェック
		String ver = getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		if(!ver.equalsIgnoreCase("v1_11_R1")){
			//1.11 R1以外は使用不可
			Bukkit.getServer().getLogger().warning("[SpigotPluginAPI] Version " + ver + " is not supported.");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		//イベントリスナーを登録
		new EventListener(this);
	}

	@Override
	public void onDisable(){
	}
}

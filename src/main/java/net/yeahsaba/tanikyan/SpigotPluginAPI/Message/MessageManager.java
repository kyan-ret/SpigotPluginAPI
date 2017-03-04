package net.yeahsaba.tanikyan.SpigotPluginAPI.Message;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessageManager {
	//インスタンス
	private static boolean init = false;

	//メッセージファイル
	private static File m_f = null;
	private static FileConfiguration m_fc = null;

	/**
	 * @param file Message file
	 * @param alt Alternative message type
	 */
	public static void Init(File file, int alt){
		//ファイルをロード
		if(!LoadMessageFile(file)){
			Bukkit.getServer().getLogger().warning("[SpigotPluginAPI] Failed to load message file.");
			return;
		}
		//初期化完了
		if(m_fc != null) init = true;
	}

	/**
	 * @param file Message file
	 * @return
	 */
	public static boolean LoadMessageFile(File file){
		if(file.exists() && file.getName().contains(".")){
			//メッセージファイルが存在した場合のみ処理
			String ft = file.getName().substring(file.getName().lastIndexOf(".") + 1);	//拡張子
			//拡張子を確認
			if(ft != null && ft.equalsIgnoreCase("yml")){
				//ymlだった場合ロード
				m_f = file;
				m_fc = YamlConfiguration.loadConfiguration(file);
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public static boolean ReloadMessageFile(){
		//初期化チェック
		if(!init || m_fc == null) return false;
		File file = new File(m_f.getAbsolutePath());
		if(file.exists()){
			//再ロード
			return LoadMessageFile(file);
		}
		return false;
	}

	/**
	 * @param path Message path in file
	 * @return Get message from file
	 */
	public static String getMessage(String path){
		//初期化チェック
		if(!init || m_fc == null) return null;
		//パスチェック
		if(m_fc.getString(path) == null) return null;
		//メッセージを返す
		return m_fc.getString(path);
	}
}

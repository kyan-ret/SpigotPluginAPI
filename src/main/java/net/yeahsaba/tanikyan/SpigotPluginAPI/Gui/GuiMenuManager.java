package net.yeahsaba.tanikyan.SpigotPluginAPI.Gui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.yeahsaba.tanikyan.SpigotPluginAPI.nms.NBT.NBTManager;

public class GuiMenuManager {
	//メニューを開いているプレイヤーの一覧 (Player p, String MenuName.toUpperCase())
	private static Map<Player, String> open_menu_pls = new HashMap<Player, String>();
	//メニューインベントリ一覧 (String MenuName.toUpperCase(), Inventory menu_inv)
	private static Map<String, Inventory> menu_list = new HashMap<String, Inventory>();

	/**
	 * Cleanup gui menu function
	 */
	public static void CleanUp(){
		//メニューのリロード処理前、プラグインの無効化時
		//プレイヤー一覧を取得、全プレイヤーのメニューを閉じる
		if(open_menu_pls != null && open_menu_pls.keySet().size() >= 1){
			//メニューを閉じる
			for(Player p : open_menu_pls.keySet()){
				p.closeInventory();
				RemoveOpenMenu(p);
			}
		}
		//メニューを全てアンロード
		if(menu_list != null && menu_list.keySet().size() >= 1){
			for(String menu : menu_list.keySet()){
				UnloadMenu(menu, true);
			}
		}
	}

	/**
	 * @param p Target player
	 * @param menu Menu name
	 */
	public static void OpenMenu(Player p, String menu){
		Inventory menu_inv = getMenu(menu);
		if(menu_inv == null){
			//メニューが見つからなかった
			p.sendMessage("Menu not found.");
			return;
		}
		//メニューを開く
		p.openInventory(menu_inv);
		//リストへ登録
		setOpenMenu(p, menu);
		//メッセージを流す場合下の行のコメントアウトを外して編集すべし
		//p.sendMessage("");
	}

	/**
	 * @param p Target player
	 * @param is Clicked item stack
	 */
	public static void ClickMenu(Player p, ItemStack is){
		//メニューアイテムがnullだった場合、または判別用NBTタグが存在しなかった場合リターン
		if(is == null || !NBTManager.hasNBT(is, "SpigotPluginAPI_Menu")) return;
		//TODO 動作分離 クリックアクションを追加した場合ここにも追記すること
		if(NBTManager.hasNBT(is, "QUEST_ACCEPT")){
			//TODO クエスト受注処理をここに
		}
		if(NBTManager.hasNBT(is, "RUN_COMMAND")){
			//コマンド実行
			p.performCommand(NBTManager.getString(is, "RUN_COMMAND"));
		}
		if(NBTManager.hasNBT(is, "OPEN_MENU")){
			//メニューを開く
			OpenMenu(p, NBTManager.getString(is, "OPEN_MENU"));
		}
		//TODO 追加ここから



		//TODO 追加ここまで
		if(NBTManager.hasNBT(is, "CLOSE")){
			if(NBTManager.getBoolean(is, "CLOSE")){
				p.closeInventory();
			}
		}
	}

	/**
	 * @param p Target player
	 * @param menu Menu name
	 */
	public static void setOpenMenu(Player p, String menu){
		if(open_menu_pls == null) open_menu_pls = new HashMap<Player, String>();
		open_menu_pls.put(p, menu.toUpperCase());
	}

	/**
	 * @param p Target player
	 */
	public static void RemoveOpenMenu(Player p){
		if(open_menu_pls != null && open_menu_pls.keySet().size() >= 1 && open_menu_pls.keySet().contains(p)) open_menu_pls.remove(p);
	}

	/**
	 * @param p Target player
	 * @return open menu
	 */
	public static boolean isOpenMenu(Player p){
		if(open_menu_pls != null && open_menu_pls.keySet().size() >= 1 && open_menu_pls.keySet().contains(p)) return true;
		return false;
	}

	/**
	 * @param p Target player
	 * @return Menu name
	 */
	public static String getOpenMenu(Player p){
		if(!isOpenMenu(p)) return null;
		return open_menu_pls.get(p);
	}

	//メニュー本体の設定などなど
	//メニューのロード
	/**
	 * @param file Menu file
	 * @return load
	 */
	public static boolean LoadMenu(File file){
		if(file.exists() && file.getName().contains(".")){
			//メッセージファイルが存在した場合のみ処理
			String ft = file.getName().substring(file.getName().lastIndexOf(".") + 1);	//拡張子
			//拡張子を確認
			if(ft != null && ft.equalsIgnoreCase("yml")){
				//ymlだった場合ロード
				FileConfiguration menu_cf = YamlConfiguration.loadConfiguration(file);
				if(menu_cf.getConfigurationSection("Meu") == null){
					//設定が存在しない
					return false;
				}
				String menu_name = file.getName().substring(0, file.getName().lastIndexOf(".")).toUpperCase();	//メニュー名
				String title = "Menu";		//文字数制限あったはず、詳細は覚えてないです(
				int size = 54;				//Max 54
				//メニュータイトルを設定
				if(menu_cf.getString("Menu.Title") != null) title = ChatColor.translateAlternateColorCodes('&', menu_cf.getString("Menu.Title"));
				//メニューサイズを設定
				if(menu_cf.getString("Menu.Size") != null) size = menu_cf.getInt("Menu.Size");
				//メニューインベントリを作成
				Inventory menu = Bukkit.createInventory(null, size, title);
				//メニューの中身を設定
				if(menu_cf.getConfigurationSection("Menu.Slot") != null && menu_cf.getConfigurationSection("Menu.Slot").getKeys(false).size() >= 1){
					for(int slot = 0; slot < size; slot++){
						if(menu_cf.getConfigurationSection("Menu.Slot." + slot) != null){
							//Materialをロード
							Material ma = Material.STONE;
							if(menu_cf.getString("Menu.Slot." + slot + ".Icon.Material") != null){
								if(Material.getMaterial(menu_cf.getString("Menu.Slot." + slot + ".Icon.Material")) != null){
									ma = Material.getMaterial(menu_cf.getString("Menu.Slot." + slot + ".Icon.Material"));
								}else {
									Bukkit.getServer().getLogger().warning("[SpigotPluginAPI] Failed to load material. (" + menu_name + ":" + slot + ")");
									Bukkit.getServer().getLogger().warning("[SpigotPluginAPI] Material '" + menu_cf.getString("Menu.Slot." + slot + ".Icon.Material") + "' is not found.");
								}
							}
							//Durabilityを設定
							short db = 0;
							if(menu_cf.getString("Menu.Slot." + slot + ".Icon.Durability") != null){
								db = (short) menu_cf.getInt("Menu.Slot." + slot + ".Icon.Durability");
							}
							//Amountを設定
							int amount = 1;
							if(menu_cf.getString("Menu.Slot." + slot + ".Icon.Amount") != null){
								amount = menu_cf.getInt("Menu.Slot." + slot + ".Icon.Amount");
							}
							//アイテム生成
							ItemStack is = new ItemStack(ma, amount, db);
							ItemMeta im = is.getItemMeta();
							//各種設定を適用
							//アイテム名を設定
							if(menu_cf.getString("Menu.Slot." + slot + ".Icon.Display") != null)
								im.setDisplayName(ChatColor.translateAlternateColorCodes('&', menu_cf.getString("Menu.Slot." + slot + ".Icon.Display")));
							//説明欄を設定
							if(menu_cf.getStringList("Menu.Slot." + slot + ".Icon.Lore") != null)
								im.setLore(menu_cf.getStringList("Menu.Slot." + slot + ".Icon.Lore"));
							//エンチャントを適用
							if(menu_cf.getConfigurationSection("Menu.Slot." + slot + ".Icon.Enchant") != null &&
									menu_cf.getConfigurationSection("Menu.Slot." + slot + ".Icon.Enchant").getKeys(false).size() >= 1){
								for(String ench : menu_cf.getConfigurationSection("Menu.Slot." + slot + ".Icon.Enchant").getKeys(false)){
									Enchantment enchant = Enchantment.getByName(ench);
									if(enchant == null){
										//エンチャントのタイプ不一致
										Bukkit.getServer().getLogger().warning("[SpigotPluginAPI] Failed to load enchantment type. (" + menu_name + ":" + slot + ")");
										Bukkit.getServer().getLogger().warning("[SpigotPluginAPI] Type '" + ench + "' is not found.");
										continue;
									}
									int level = menu_cf.getInt("Menu.Slot." + slot + ".Icon.Enchant." + ench);
									im.addEnchant(enchant, level, true);
								}
							}
							is.setItemMeta(im);
							//NBTタグを設定
							is = NBTManager.addNBT(is, "SpigotPluginAPI_Menu", menu_name);
							//クリックアクションを設定
							if(menu_cf.getConfigurationSection("Menu.Slot." + slot + ".Action") != null){
								//TODO クリックアクションを追加した場合ここにも追記すること
								//クエスト受注
								if(menu_cf.getString("Menu.Slot." + slot + ".Action.QUEST_ACCEPT") != null){
									is = NBTManager.addNBT(is, "QUEST_ACCEPT", menu_cf.getString("Menu.Slot." + slot + ".Action.QUEST_ACCEPT"));
								}
								//コマンド実行
								if(menu_cf.getString("Menu.Slot." + slot + ".Action.RUN_COMMAND") != null){
									is = NBTManager.addNBT(is, "RUN_COMMAND", menu_cf.getString("Menu.Slot." + slot + ".Action.RUN_COMMAND"));
								}
								//他メニューを開く
								if(menu_cf.getString("Menu.Slot." + slot + ".Action.OPEN_MENU") != null){
									is = NBTManager.addNBT(is, "OPEN_MENU", menu_cf.getString("Menu.Slot." + slot + ".Action.OPEN_MENU"));
								}
								//TODO 追加ここから



								//TODO 追加ここまで
								//メニューを閉じる
								if(menu_cf.getString("Menu.Slot." + slot + ".Action.CLOSE") != null){
									is = NBTManager.addNBT(is, "CLOSE", menu_cf.getBoolean("Menu.Slot." + slot + ".Action.CLOSE"));
								}
							}
							//アイテムをメニューに設定
							menu.setItem(slot, is);
						}
					}
					//メニューのロード完了
					Bukkit.getServer().getLogger().info("[SpigotPluginAPI] Menu loaded: " + menu_name);
					return true;
				}
			}
		}
		return false;
	}

	//メニューのアンロード
	/**
	 * @param menu Menu name
	 * @param force Force unload
	 * @return unload
	 */
	public static boolean UnloadMenu(String menu, boolean force){
		//メニューの存在チェック
		if(!existsMenu(menu)){
			//メニューが存在しない
			Bukkit.getServer().getLogger().warning("[SpigotPluginAPI] Menu not found.");
			return false;
		}
		//メニューの使用チェック
		if(!force && open_menu_pls != null && open_menu_pls.keySet().size() >= 1 && open_menu_pls.values().contains(menu.toUpperCase())){
			//強制アンロードオフでメニューが使用中だった場合
			Bukkit.getServer().getLogger().warning("[SpigotPluginAPI] Menu is now opened.");
			return false;
		}
		//アンロード
		menu_list.remove(menu.toUpperCase());
		Bukkit.getServer().getLogger().info("[SpigotPluginAPI] Menu unloaded: " + menu.toUpperCase());
		return true;
	}

	//メニューの取得
	/**
	 * @param menu Menu name
	 * @return Menu inventory
	 */
	public static Inventory getMenu(String menu){
		if(!existsMenu(menu)) return null;
		return menu_list.get(menu.toUpperCase());
	}

	/**
	 * @return Menu list
	 */
	public static Set<String> getMenuList(){
		if(menu_list != null && menu_list.keySet().size() >= 1) return menu_list.keySet();
		else return null;
	}

	//メニューが存在するかどうか
	/**
	 * @param menu Menu name
	 * @return exists
	 */
	public static boolean existsMenu(String menu){
		if(menu_list == null || menu_list.size() <= 0 || !menu_list.keySet().contains(menu.toUpperCase())) return false;
		return true;
	}
}

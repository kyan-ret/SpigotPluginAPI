package net.yeahsaba.tanikyan.SpigotPluginAPI.nms;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;

public class JsonChatManager {
	/**
	 * @param p Target player
	 * @param msg Json chat message
	 */
	public static void sendJsonChat(Player p, String msg){
		IChatBaseComponent chat_c = ChatSerializer.a(msg);
		PacketPlayOutChat chat_p = new PacketPlayOutChat(chat_c);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat_p);;
	}
}

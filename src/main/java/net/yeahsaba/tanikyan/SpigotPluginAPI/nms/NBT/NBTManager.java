package net.yeahsaba.tanikyan.SpigotPluginAPI.nms.NBT;

import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_11_R1.NBTTagCompound;

public class NBTManager {
	//設定 ここから
	/**
	 * add NBT Tag (String)
	 * @param is Target ItemStack
	 * @param key NBT Tag key
	 * @param value NBT Tag value
	 * @return ItemStack
	 */
	public static ItemStack addNBT(ItemStack is, String key, String value){
		net.minecraft.server.v1_11_R1.ItemStack nis = CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbt = new NBTTagCompound();
		if(nis.getTag() != null) nbt = nis.getTag();
		nbt.setString(key, value);
		nis.setTag(nbt);
		return CraftItemStack.asCraftMirror(nis);
	}

	/**
	 * add NBT Tag (int)
	 * @param is Target ItemStack
	 * @param key NBT Tag key
	 * @param value NBT Tag value
	 * @return ItemStack
	 */
	public static ItemStack addNBT(ItemStack is, String key, int value){
		net.minecraft.server.v1_11_R1.ItemStack nis = CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbt = new NBTTagCompound();
		if(nis.getTag() != null) nbt = nis.getTag();
		nbt.setInt(key, value);
		nis.setTag(nbt);
		return CraftItemStack.asCraftMirror(nis);
	}

	/**
	 * add NBT Tag (byte)
	 * @param is Target ItemStack
	 * @param key NBT Tag key
	 * @param value NBT Tag value
	 * @return ItemStack
	 */
	public static ItemStack addNBT(ItemStack is, String key, byte value){
		net.minecraft.server.v1_11_R1.ItemStack nis = CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbt = new NBTTagCompound();
		if(nis.getTag() != null) nbt = nis.getTag();
		nbt.setByte(key, value);
		nis.setTag(nbt);
		return CraftItemStack.asCraftMirror(nis);
	}

	/**
	 * @param is Target ItemStack
	 * @param key NBT Tag key
	 * @param value NBT Tag value
	 * @return ItemStack
	 */
	public static ItemStack addNBT(ItemStack is, String key, boolean value){
		net.minecraft.server.v1_11_R1.ItemStack nis = CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbt = new NBTTagCompound();
		if(nis.getTag() != null) nbt = nis.getTag();
		nbt.setBoolean(key, value);
		nis.setTag(nbt);
		return CraftItemStack.asCraftMirror(nis);
	}

	//チェックここから
	/**
	 * has NBT Tag
	 * @param is Target ItemStack
	 * @param key NBT Tag key
	 * @return boolean
	 */
	public static boolean hasNBT(ItemStack is, String key){
		net.minecraft.server.v1_11_R1.ItemStack nis = CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbt = new NBTTagCompound();
		if(nis.getTag() != null) nbt = nis.getTag();
		return nbt.hasKey(key);
	}

	//取得 ここから
	/**
	 * Get string value from NBT tag
	 * @param is Target ItemStack
	 * @param key NBT Tag key
	 * @return String
	 */
	public static String getString(ItemStack is, String key){
		if(!hasNBT(is, key)) return null;
		net.minecraft.server.v1_11_R1.ItemStack nis = CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbt = new NBTTagCompound();
		if(nis.getTag() != null) nbt = nis.getTag();
		return nbt.getString(key);
	}

	/**
	 * Get int value from NBT tag
	 * @param is Target ItemStack
	 * @param key NBT Tag key
	 * @return int
	 */
	public static int getInt(ItemStack is, String key){
		if(!hasNBT(is, key)) return -1;
		net.minecraft.server.v1_11_R1.ItemStack nis = CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbt = new NBTTagCompound();
		if(nis.getTag() != null) nbt = nis.getTag();
		return nbt.getInt(key);
	}

	/**
	 * Get byte value from NBT tag
	 * @param is Target ItemStack
	 * @param key NBT Tag key
	 * @return byte
	 */
	public static byte getbyte(ItemStack is, String key){
		if(!hasNBT(is, key)) return -1;
		net.minecraft.server.v1_11_R1.ItemStack nis = CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbt = new NBTTagCompound();
		if(nis.getTag() != null) nbt = nis.getTag();
		return nbt.getByte(key);
	}

	/**
	 * Get boolean value from NBT tag
	 * @param is Target ItemStack
	 * @param key NBT Tag key
	 * @return boolean
	 */
	public static boolean getBoolean(ItemStack is, String key){
		if(!hasNBT(is, key)) return false;
		net.minecraft.server.v1_11_R1.ItemStack nis = CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbt = new NBTTagCompound();
		if(nis.getTag() != null) nbt = nis.getTag();
		return nbt.getBoolean(key);
	}
}

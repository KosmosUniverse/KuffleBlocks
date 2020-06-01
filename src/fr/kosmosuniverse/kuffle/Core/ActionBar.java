package fr.kosmosuniverse.kuffle.Core;

import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.ChatMessageType;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import net.minecraft.server.v1_15_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_15_R1.PacketPlayOutTitle.EnumTitleAction;

public class ActionBar {
	public static void sendMessage(String msg, Player player) {
		IChatBaseComponent text = ChatSerializer.a("{\"text\": \"" + msg + "\"}");
		PacketPlayOutChat bar = new PacketPlayOutChat(text, ChatMessageType.GAME_INFO);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(bar);
	}
	
	public static void sendTitle(String msg, Player player) {
		IChatBaseComponent text = ChatSerializer.a("{\"text\": \"" + msg + "\"}");
		PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, text);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
	}
}

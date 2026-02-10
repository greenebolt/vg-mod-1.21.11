package com.vgmod;

import com.vgmod.action.VGModAction;
import com.vgmod.handler.CommandHandler;
import com.vgmod.handler.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.io.File;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class VGModClient implements ClientModInitializer {

	public static Config config;
	private static Minecraft mc;
	public static boolean swb = false;
	public static String friendMessagingMode = "subtitle";

	@Override
	public void onInitializeClient() {
		// Register stuff
		KeyInputHandler.register();
		CommandHandler.register();

		// Initialize config
		mc = Minecraft.getInstance();
		config = new Config(mc.gameDirectory.getAbsolutePath() + File.separator + "config" + File.separator + "VGMod.cfg");
		config.read();

		// Code to execute when joining a server
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if (client.getCurrentServer().name.equals("VG")) {
				// Joined VG
				// TODO - maybe disable commands if not vg?
			}

			Component msg = Component.translatable("VGMod: WB Messages Are: "+ Config.wbMessages)
					.withStyle(ChatFormatting.DARK_GREEN);
			client.player.displayClientMessage(msg, false);
		});

		// Code to execute when disconnecting from server
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			VGMod.LOGGER.info("Saving Config...");
			Config.save();
		});

		// Detects messages sent by the GAME for auto wb
		ClientReceiveMessageEvents.GAME.register((message, timestamp) -> {
			try {
				Minecraft client = Minecraft.getInstance();
				String text = message.getString();
				if (text.contains("VGMod")) return;
				VGMod.LOGGER.info("Detected GAME message: " + text);

				if (text.contains("Welcome to ")) {
					VGModAction.newPlayers.add(VGModAction.mostRecentPlayerJoin);
					return;
				}

				if (text.contains("left the game")) {
					int time = (int) (Instant.now().toEpochMilli() / 60000);
					//Component msg = Component.translatable("VGMod detected: " + text + " at time: " + time);
					//client.player.displayClientMessage(msg, false);
					VGModAction.recentlyLeft.put(getPlayer(text), time);
					return;
				}

				if (!text.contains("joined the game")) return;
				String player = getPlayer(text);
				if (Config.friends.contains(player)) {
					Component msg = Component.translatable("VGMod: Your friend, \"%s\" has joined the game!", player)
							.withStyle(ChatFormatting.DARK_GREEN);
					if (friendMessagingMode.equals("subtitle"))
					{client.player.displayClientMessage(msg, true);}
					else if (friendMessagingMode.equals("chat"))
					{client.player.displayClientMessage(msg, false);}
				}
				if (!Config.wbMessages && !swb) return;
				VGModAction.mostRecentPlayerJoin = player;
				CompletableFuture.supplyAsync(() -> VGModAction.sendWbMessage(player));
			} catch (Exception e) {
				VGMod.LOGGER.error("Error in chat listener", e);
			}
		});
	}

	// Gets player username detected in "joined the game" chat message
	private static String getPlayer(String text) {
		String[] textComponents = text.split(" ");
		String player = textComponents[1];
		if (player.equals("joined")) player = textComponents[0];
		if (player.equals("Miner]")) player = textComponents[2];
		if (player.equals("Gamer]")) player = textComponents[2];
		return player;
	}
}
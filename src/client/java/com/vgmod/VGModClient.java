package com.vgmod;

import com.vgmod.action.VGModAction;
import com.vgmod.handler.CommandHandler;
import com.vgmod.handler.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.io.File;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class VGModClient implements ClientModInitializer {

	public static Config config;
	private static Minecraft mc;
	public static boolean swb = false;

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		KeyInputHandler.register();
		CommandHandler.register();

		// Initialize config
		mc = Minecraft.getInstance();
		config = new Config(mc.gameDirectory.getAbsolutePath() + File.separator + "config" + File.separator + "VGMod.cfg");
		config.read();

		// Detects messages sent by the GAME for auto wb
		ClientReceiveMessageEvents.GAME.register((message, timestamp) -> {
			Minecraft client = Minecraft.getInstance();
			String text = message.getString();
			if (text.contains("VGMod")) return;
			VGMod.LOGGER.info(text);
			if (text.contains("Welcome to ")) {
				Component msg = Component.translatable("VGMod detected new player: " + text);
				client.player.displayClientMessage(msg, false);
				VGModAction.newPlayers.add(VGModAction.mostRecentPlayerJoin);
				return;
			}
			if (text.contains("left the game")) {
				Component msg = Component.translatable("VGMod detected: " + text + " left!");
				client.player.displayClientMessage(msg, false);
				int time = (int)(Instant.now().toEpochMilli() / 60000);;
				VGModAction.recentJoins.put(getPlayer(text), time);
				return;
			}
			if (!text.contains("joined the game")) return;
			if (!Config.wbMessages && !swb) return;
			String player = getPlayer(text);
			Component msg = Component.translatable("VGMod detected: " + player + "!");
			client.player.displayClientMessage(msg, false);
			VGModAction.mostRecentPlayerJoin = player;
			CompletableFuture.supplyAsync(() -> VGModAction.sendWbMessage(player));
		});

		ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, timestamp) -> {
			Minecraft client = Minecraft.getInstance();
			if (client.player == null) return;

			if (sender != null) {
				String from = sender.name().toString();
				VGMod.LOGGER.info("VGMod recieved message from: " + from);
				//Component msg2 = Component.translatable("VGMod recieved from: " + from);
				//client.player.displayClientMessage(msg2, false);
			}

			String text = message.getString();
			VGMod.LOGGER.info(text);
			//Component msg = Component.translatable("VGMod recieved: " + text);
			//client.player.displayClientMessage(msg, false);
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
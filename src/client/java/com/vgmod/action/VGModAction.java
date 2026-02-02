package com.vgmod.action;

import com.vgmod.Config;
import com.vgmod.Constants;
import com.vgmod.VGMod;
import com.vgmod.VGModClient;
import it.unimi.dsi.fastutil.Hash;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.time.Instant;
import java.util.*;

public class VGModAction {
    // Variable Declaration for auto WB
    public static Map<String, Integer> recentlyLeft = new HashMap<>();
    public static List<String> newPlayers = new ArrayList<>();
    public static String mostRecentPlayerJoin;

    public static void GoToHub(){
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;
        Component msg = Component.translatable("VG MOD: Sending you to lobby...")
                .withStyle(ChatFormatting.DARK_GREEN);
        client.player.displayClientMessage(msg, false);
        //client.player.connection.sendCommand("summon firework_rocket ~ ~1 ~ {Life:1,FireworksItem:{id:firework_rocket,components:{fireworks:{flight_duration:1,explosions:[{shape:star,has_twinkle:1b,has_trail:1b,colors:[I;8439583,16383998,1481884,6192150],fade_colors:[I;8439583]}]}}}}");
        client.player.connection.sendCommand("trigger cmd set 1");
    }
    public static String lobby() {
        GoToHub();
        return "done";
    }
    public static String displayHelp(){
        Minecraft client = Minecraft.getInstance();
        Component msg = Component.translatable("VGMOD: List of commands:")
                .withStyle(ChatFormatting.DARK_GREEN);
        client.player.displayClientMessage(msg, false);
        List<String> commands = new ArrayList<>(Arrays.asList(
           "/help-VGMod\n/lobby\n/stats\n/rules",
           "/ranks\n/info\n/beans",
           "/hats\n/particles\n/titles",
           "/sbinv\n/warp [game]\n/toggle-wb-messages [true/false]"
        ));
        sendSlow(commands);
        return "done";
    }
    public static void  sendSlow(List<String> message) {
        Iterator<String> it = message.iterator();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || !it.hasNext()) return;
            Component msg = Component.translatable(it.next());
            client.player.displayClientMessage(msg, false);
                }

        );
    }
    public static String displayStats(){
        try {
            Minecraft client = Minecraft.getInstance();
            client.player.connection.sendCommand("trigger cmd set 10");
            Thread.sleep(100);
            client.player.connection.sendCommand("trigger cmd set 11");
            Thread.sleep(100);
            client.player.connection.sendCommand("trigger cmd set 12");
            Thread.sleep(100);
            client.player.connection.sendCommand("trigger cmd set 13");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "done";
    }
    public static String displayRules(){
        Minecraft client = Minecraft.getInstance();
        client.player.connection.sendCommand("trigger cmd set 3");
        return "done";
    }
    public static String displayRanks(){
        Minecraft client = Minecraft.getInstance();
        client.player.connection.sendCommand("trigger cmd set 4");
        return "done";
    }
    public static String displayInfo(){
        Minecraft client = Minecraft.getInstance();
        client.player.connection.sendCommand("trigger cmd set 5");
        return "done";
    }
    public static String displayBeans(){
        Minecraft client = Minecraft.getInstance();
        client.player.connection.sendCommand("trigger cmd set 10");
        return "done";
    }
    public static String hats(){
        Minecraft client = Minecraft.getInstance();
        client.player.connection.sendCommand("trigger cmd set 521");
        return "done";
    }
    public static String particles(){
        Minecraft client = Minecraft.getInstance();
        client.player.connection.sendCommand("trigger cmd set 522");
        return "done";
    }
    public static String titles(){
        Minecraft client = Minecraft.getInstance();
        client.player.connection.sendCommand("trigger cmd set 524");
        return "done";
    }
    public static String sendInvInf(){
        Minecraft client = Minecraft.getInstance();
        client.player.connection.sendChat("Type \".invite [username]\" and don't use a \"/\"");
        return "done";
    }
    public static String toggleWbMessages(){
        Minecraft client = Minecraft.getInstance();
        Component msg = Component.translatable("VGMOD: toggling wb messages...")
                .withStyle(ChatFormatting.DARK_GREEN);
        client.player.displayClientMessage(msg, false);
        if (VGModClient.swb) {
            msg = Component.translatable("wb messages are now inactive.")
                    .withStyle(ChatFormatting.DARK_GREEN);
            client.player.displayClientMessage(msg, false);
            VGModClient.swb = false;
            Config.wbMessages = false;
            return "done";
        }
        Config.wbMessages = !Config.wbMessages;
        if (Config.wbMessages) {
            msg = Component.translatable("wb messages are now active!")
                    .withStyle(ChatFormatting.DARK_GREEN);
            client.player.displayClientMessage(msg, false);
        } else {
            msg = Component.translatable("wb messages are now inactive.")
                    .withStyle(ChatFormatting.DARK_GREEN);
            client.player.displayClientMessage(msg, false);
        }
        return "done";
    }
    public static String setWbMessages(boolean arg){
        Minecraft client = Minecraft.getInstance();
        Component msg = Component.translatable("VGMOD: toggling wb messages...")
                .withStyle(ChatFormatting.DARK_GREEN);
        VGModClient.swb = false;
        client.player.displayClientMessage(msg, false);
        if (arg) {
            Config.wbMessages = true;
            msg = Component.translatable("wb messages are now active!")
                    .withStyle(ChatFormatting.DARK_GREEN);
            client.player.displayClientMessage(msg, false);
        } else {
            Config.wbMessages = false;
            msg = Component.translatable("wb messages are now inactive.")
                    .withStyle(ChatFormatting.DARK_GREEN);
            client.player.displayClientMessage(msg, false);
        }
        return "done";
    }
    public static String toggleSecretWbMessages(){
        Minecraft client = Minecraft.getInstance();
        Component msg = Component.translatable("VGMOD: toggling secret wb messages...")
                .withStyle(ChatFormatting.DARK_GREEN);
        client.player.displayClientMessage(msg, false);
        Config.wbMessages = false;
        VGModClient.swb = !VGModClient.swb;
        if (VGModClient.swb) {
            msg = Component.translatable("secret wb messages are now active!")
                    .withStyle(ChatFormatting.GOLD);
            client.player.displayClientMessage(msg, false);
        } else {
            msg = Component.translatable("secret wb messages are now inactive.")
                    .withStyle(ChatFormatting.GOLD);
            client.player.displayClientMessage(msg, false);
        }
        return "done";
    }
    public static String sendWbMessage(String player){
        try {
            // Has the player recently left the game?
            int time = (int)(Instant.now().toEpochMilli() / 60000);
            for (Map.Entry<String, Integer> i : recentlyLeft.entrySet()) {
                if ((time - i.getValue()) < 1) {
                    recentlyLeft.remove(i.getKey());
                }
            }
            if (recentlyLeft.containsKey(player)) {
                return "done";
            }

            Minecraft client = Minecraft.getInstance();
            Random random = new Random();
            String msg = "wb " + player;

            if (random.nextInt(1,3) == 1){
                msg = "wb";
            }

            if (VGModClient.swb) {
                msg = Constants.secretWBMessages[random.nextInt(0, Constants.secretWBMessages.length)];
                msg.replaceAll("##", player);
            }
            // Delay and send message
            int delay = random.nextInt(2000, 5000);
            Thread.sleep(delay);
            // Check to make sure player is not new
            if (newPlayers.contains(player)) {
                newPlayers.remove(player);
                msg = "Welcome " + player + "!";
            }
            client.player.connection.sendChat(msg);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "done";
    }
}

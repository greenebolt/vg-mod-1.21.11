package com.vgmod.handler;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.vgmod.Config;
import com.vgmod.Constants;
import com.vgmod.action.VGModAction;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class CommandHandler {
    private static final SuggestionProvider<FabricClientCommandSource> WB_SUGGESTIONS = (context, builder) -> {
        // Example list of custom suggestions
        String[] suggestions = {"true", "false"};
        // Filter suggestions based on what the user has already typed
        Stream<String> stream = Stream.of(suggestions).filter(s -> s.startsWith(builder.getRemaining()));
        // Add each suggestion to the builder
        for (String suggestion : (Iterable<String>) stream::iterator) {
            builder.suggest(suggestion);
        }
        // Build and return the suggestions future
        return builder.buildFuture();
    };
    private static final SuggestionProvider<FabricClientCommandSource> SBINFO_SUGGESTIONS = (context, builder) -> {
        // Example list of custom suggestions
        String[] suggestions = {"invite","nether", "end","wither","animals","trader"};
        // Filter suggestions based on what the user has already typed
        Stream<String> stream = Stream.of(suggestions).filter(s -> s.startsWith(builder.getRemaining()));
        // Add each suggestion to the builder
        for (String suggestion : (Iterable<String>) stream::iterator) {
            builder.suggest(suggestion);
        }
        // Build and return the suggestions future
        return builder.buildFuture();
    };
    private static final SuggestionProvider<FabricClientCommandSource> FRIEND_SUGGESTIONS = (context, builder) -> {
        // Example list of custom suggestions
        String[] suggestions = {"add", "remove", "list"};
        // Filter suggestions based on what the user has already typed
        Stream<String> stream = Stream.of(suggestions).filter(s -> s.startsWith(builder.getRemaining()));
        // Add each suggestion to the builder
        for (String suggestion : (Iterable<String>) stream::iterator) {
            builder.suggest(suggestion);
        }
        // Build and return the suggestions future
        return builder.buildFuture();
    };
    private static final SuggestionProvider<FabricClientCommandSource> FRIEND_DISPLAY_SUGGESTIONS = (context, builder) -> {
        // Example list of custom suggestions
        String[] suggestions = {"subtitle", "chat", "none"};
        // Filter suggestions based on what the user has already typed
        Stream<String> stream = Stream.of(suggestions).filter(s -> s.startsWith(builder.getRemaining()));
        // Add each suggestion to the builder
        for (String suggestion : (Iterable<String>) stream::iterator) {
            builder.suggest(suggestion);
        }
        // Build and return the suggestions future
        return builder.buildFuture();
    };
    private static final SuggestionProvider<FabricClientCommandSource> GAME_SUGGESTIONS = (context, builder) -> {
        // Example list of custom suggestions
        String[] suggestions = Constants.games.keySet().toArray(new String[0]);
        // Filter suggestions based on what the user has already typed
        Stream<String> stream = Stream.of(suggestions).filter(s -> s.startsWith(builder.getRemaining()));
        // Add each suggestion to the builder
        for (String suggestion : (Iterable<String>) stream::iterator) {
            builder.suggest(suggestion);
        }
        // Build and return the suggestions future
        return builder.buildFuture();
    };

    public static void register(){
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vghelp").executes(
                                CommandHandler::help
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vglobby").executes(
                                CommandHandler::lobby
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vgstats").executes(
                                CommandHandler::stats
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vgrules").executes(
                                CommandHandler::rules
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vgranks").executes(
                                CommandHandler::ranks
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vginfo").executes(
                                CommandHandler::info
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vgbeans").executes(
                                CommandHandler::beans
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vghats").executes(
                                CommandHandler::hats
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vgparticles").executes(
                                CommandHandler::particles
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vgtitles").executes(
                                CommandHandler::titles
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vgsbinfo")
                                .executes(CommandHandler::sbinfoSelf)
                                .then(
                                        ClientCommands.argument("value", StringArgumentType.string())
                                                .suggests(SBINFO_SUGGESTIONS)
                                                .executes(CommandHandler::sbinfo)
                                )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vgfriend")
                                .then(
                                        ClientCommands.argument("value", StringArgumentType.string())
                                                .suggests(FRIEND_SUGGESTIONS)
                                                .executes(CommandHandler::friends)
                                                .then (
                                                        ClientCommands.argument("player", StringArgumentType.string())
                                                                .executes(CommandHandler::friends)
                                                )
                                )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vgfriendsetdisplay")
                                .then(
                                        ClientCommands.argument("value", StringArgumentType.string())
                                                .suggests(FRIEND_DISPLAY_SUGGESTIONS)
                                                .executes(CommandHandler::friendsSetDisplay)
                                )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vgtogglewbmessages")
                                .executes(CommandHandler::toggleWbMessagesNoArg)
                                .then(
                                        ClientCommands.argument("value", StringArgumentType.string())
                                                .suggests(WB_SUGGESTIONS)
                                                .executes(CommandHandler::toggleWbMessages)
                                )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("vgjoin")
                                .then(
                                        ClientCommands.argument("game", StringArgumentType.string())
                                                .suggests(GAME_SUGGESTIONS)
                                                .executes(CommandHandler::joinGame)
                                )
                )
        );
    }
    private static int help(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.displayHelp());
        return 1;
    }
    private static int lobby(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.lobby());
        return 1;
    }
    private static int stats(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.displayStats());
        return 1;
    }
    private static int rules(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.displayRules());
        return 1;
    }
    private static int ranks(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.displayRanks());
        return 1;
    }
    private static int info(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.displayInfo());
        return 1;
    }
    private static int beans(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.displayBeans());
        return 1;
    }
    private static int hats(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.hats());
        return 1;
    }
    private static int particles(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.particles());
        return 1;
    }
    private static int titles(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.titles());
        return 1;
    }
    private static int sbinfoSelf(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.sbInfoSelf());
        return 1;
    }
    private static int sbinfo(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        String arg = StringArgumentType.getString(context, "value");
            CompletableFuture.runAsync(() -> VGModAction.sbInfo(arg));
        return 1;
    }
    private static int friends(CommandContext<FabricClientCommandSource> context) {
        assert Minecraft.getInstance().player != null;
        Minecraft client = Minecraft.getInstance();
        String arg = StringArgumentType.getString(context, "value");
        Component msg = Component.translatable("Unknown value: \"%s\" Please use add/remove/list", arg)
                .withStyle(ChatFormatting.RED);
        if (arg.equals("add")) {
            String username = StringArgumentType.getString(context, "player");
            if (Config.friends.contains(username)) {
                msg = Component.translatable("You are already friends with: \"%s\" ", username)
                        .withStyle(ChatFormatting.RED);
            } else {
                Config.friendList = Config.friendList + "," + username;
                Config.friends.add(username);
                msg = Component.translatable("VGMod added friend: \"%s\" ", username)
                        .withStyle(ChatFormatting.DARK_GREEN);
            }
        } else if (arg.equals("remove")) {
            String username = StringArgumentType.getString(context, "player");
            if (Config.friends.contains(username)) {
                Config.friendList = Config.friendList.replaceAll("," + username, "");
                Config.friends.remove(username);
                msg = Component.translatable("VGMod removed friend: \"%s\" ", username)
                        .withStyle(ChatFormatting.DARK_GREEN);
            } else {
                msg = Component.translatable("Could not remove friend:: \"%s\". You are not friends with this player.", username)
                        .withStyle(ChatFormatting.RED);
            }
        } else if (arg.equals("list")) {
            msg = Component.translatable("VGMod: Friends: "+Config.friendList)
                    .withStyle(ChatFormatting.DARK_GREEN);
        }
        client.player.sendSystemMessage(msg);
        return 1;
    }
    private static int friendsSetDisplay(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        String arg = StringArgumentType.getString(context, "value");
        CompletableFuture.runAsync(() -> VGModAction.friendsSetDisplay(arg));
        return 1;
    }
    private static int toggleWbMessages(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        String arg = StringArgumentType.getString(context, "value");
        if (arg.equals("egg")) {
            CompletableFuture.runAsync(() -> VGModAction.toggleSecretWbMessages());
        } else if (arg.equals("true")) {
            CompletableFuture.runAsync(() -> VGModAction.setWbMessages(true));
        } else if (arg.equals("false")) {
            CompletableFuture.runAsync(() -> VGModAction.setWbMessages(false));
        } else {
            Minecraft client = Minecraft.getInstance();
            Component msg = Component.translatable("Unknown value: \"%s\" Please use true/false", arg)
                    .withStyle(ChatFormatting.RED);
            client.player.sendSystemMessage(msg);
        }
        return 1;
    }
    private static int toggleWbMessagesNoArg(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.runAsync(() -> VGModAction.toggleWbMessages());
        return 1;
    }
    private static int joinGame(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        String arg = StringArgumentType.getString(context, "game");
        VGModAction.joinGame(arg);
        return 1;
    }
}

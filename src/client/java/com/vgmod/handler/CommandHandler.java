package com.vgmod.handler;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.vgmod.Config;
import com.vgmod.Constants;
import com.vgmod.action.VGModAction;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.List;
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

                        ClientCommandManager.literal("help-VGMod").executes(
                                CommandHandler::help
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("lobby").executes(
                                CommandHandler::lobby
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("stats").executes(
                                CommandHandler::stats
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("rules").executes(
                                CommandHandler::rules
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("ranks").executes(
                                CommandHandler::ranks
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("info").executes(
                                CommandHandler::info
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("beans").executes(
                                CommandHandler::beans
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("sbinv").executes(
                                CommandHandler::sbinv
                        )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("toggle-wb-messages")
                                .executes(CommandHandler::toggleWbMessagesNoArg)
                                .then(
                                        ClientCommandManager.argument("value", StringArgumentType.string())
                                                .suggests(WB_SUGGESTIONS)
                                                .executes(CommandHandler::toggleWbMessages)
                                )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("warp")
                                .then(
                                        ClientCommandManager.argument("game", StringArgumentType.string())
                                                .suggests(GAME_SUGGESTIONS)
                                                .executes(CommandHandler::joinGame)
                                )
                )
        );
    }
    private static int help(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.supplyAsync(() -> VGModAction.displayHelp());
        return 1;
    }
    private static int lobby(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.supplyAsync(() -> VGModAction.lobby());
        return 1;
    }
    private static int stats(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.supplyAsync(() -> VGModAction.displayStats());
        return 1;
    }
    private static int rules(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.supplyAsync(() -> VGModAction.displayRules());
        return 1;
    }
    private static int ranks(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.supplyAsync(() -> VGModAction.displayRanks());
        return 1;
    }
    private static int info(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.supplyAsync(() -> VGModAction.displayInfo());
        return 1;
    }
    private static int beans(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.supplyAsync(() -> VGModAction.displayBeans());
        return 1;
    }
    private static int sbinv(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.supplyAsync(() -> VGModAction.sendInvInf());
        return 1;
    }
    private static int toggleWbMessages(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        String arg = StringArgumentType.getString(context, "value");
        if (arg.equals("egg")) {
            CompletableFuture.supplyAsync(() -> VGModAction.toggleSecretWbMessages());
        } else if (arg.equals("true")) {
            CompletableFuture.supplyAsync(() -> VGModAction.setWbMessages(true));
        } else if (arg.equals("false")) {
            CompletableFuture.supplyAsync(() -> VGModAction.setWbMessages(false));
        } else {
            Minecraft client = Minecraft.getInstance();
            Component msg = Component.translatable("Unknown value: \"%s\" Please use true/false", arg)
                    .withStyle(ChatFormatting.RED);
            client.player.displayClientMessage(msg, false);
        }
        return 1;
    }
    private static int toggleWbMessagesNoArg(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        CompletableFuture.supplyAsync(() -> VGModAction.toggleWbMessages());
        return 1;
    }
    private static int joinGame(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        String arg = StringArgumentType.getString(context, "game");
        Minecraft client = Minecraft.getInstance();
        if (Constants.games.containsKey(arg)) {
            Component msg = Component.translatable("VGMOD: Joining game: \"%s\".", arg)
                    .withStyle(ChatFormatting.DARK_GREEN);
            client.player.displayClientMessage(msg, false);
            client.player.connection.sendCommand("trigger cmd set " + (Constants.games.get(arg)+170000));
        } else {
            Component msg = Component.translatable("Unknown game: \"%s\".", arg)
                    .withStyle(ChatFormatting.RED);
            client.player.displayClientMessage(msg, false);
        }
        return 1;
    }
}

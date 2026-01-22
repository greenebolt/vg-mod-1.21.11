package com.vgmod.handler;

import com.mojang.blaze3d.platform.InputConstants;
import com.vgmod.action.VGModAction;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final KeyMapping.Category VG_MOD_CATEGORY = KeyMapping.Category.register(Identifier.parse("vg_mod"));
    public static final String KEY_GO_TO_HUB = "key.vg_mod.go_to_hub";

    public static KeyMapping goToHubKey;

    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (goToHubKey.consumeClick()) {
                VGModAction.GoToHub();
            }
        });
    }

    public static void register(){
        goToHubKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KEY_GO_TO_HUB,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                VG_MOD_CATEGORY
        ));

        registerKeyInputs();
    }
}

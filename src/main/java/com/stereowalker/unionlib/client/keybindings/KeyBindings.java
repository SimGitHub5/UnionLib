package com.stereowalker.unionlib.client.keybindings;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

/**
 * Registers this mod's {@link KeyMapping}s.
 *
 * @author Stereowalker
 */
public class KeyBindings {

	private static final String CATEGORY = "key.category.unionlib.general";
	public static final KeyMapping OPEN_UNION_INVENTORY = new KeyMapping("key.unionlib.open_union_inventory", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U, CATEGORY);

	@OnlyIn(Dist.CLIENT)
	public static void registerKeyBindings() {
		ClientRegistry.registerKeyBinding(OPEN_UNION_INVENTORY);
	}
}

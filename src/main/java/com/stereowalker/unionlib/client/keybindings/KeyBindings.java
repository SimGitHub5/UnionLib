package com.stereowalker.unionlib.client.keybindings;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

/**
 * Registers this mod's {@link KeyMapping}s.
 *
 * @author Stereowalker
 */
public class KeyBindings {

	private static final String CATEGORY = "key.category.unionlib.general";
	public static final KeyMapping OPEN_UNION_INVENTORY = new KeyMapping("key.unionlib.open_union_inventory", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U, CATEGORY);
}

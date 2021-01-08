package com.stereowalker.unionlib.client.keybindings;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Registers this mod's {@link KeyBinding}s.
 *
 * @author Choonster
 */
public class KeyBindings {

	private static final String CATEGORY = "key.category.unionlib.general";
	public static final KeyBinding OPEN_UNION_INVENTORY = new KeyBinding("key.unionlib.open_union_inventory", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_U, CATEGORY);

	@OnlyIn(Dist.CLIENT)
	public static void registerKeyBindings() {
		ClientRegistry.registerKeyBinding(OPEN_UNION_INVENTORY);
	}
}

package com.stereowalker.unionlib.client.gui.screen.inventory;

import com.stereowalker.unionlib.inventory.container.UContainerType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;

@Environment(EnvType.CLIENT)
public class UScreens {
	public static final void registerScreens() {
		MenuScreens.register(UContainerType.UNION, UnionInventoryScreen::new);
	}
}

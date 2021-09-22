package com.stereowalker.unionlib.client.gui.screen.inventory;

import com.stereowalker.unionlib.inventory.container.UContainerType;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UScreens {
	public static final void registerScreens() {
		MenuScreens.register(UContainerType.UNION, UnionInventoryScreen::new);
	}
}

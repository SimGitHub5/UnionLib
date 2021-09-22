package com.stereowalker.unionlib.client.keybindings;

import com.stereowalker.unionlib.network.client.play.CUnionInventoryPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class KeybindingEvents {
	static Minecraft mc = Minecraft.getInstance();
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void keybindTick(ClientTickEvent playerEvent) {
		if(mc.screen==null) {
			LocalPlayer clientPlayer = mc.player;
			
			if (KeyBindings.OPEN_UNION_INVENTORY.consumeClick()) {
				new CUnionInventoryPacket(clientPlayer.getUUID()).send();
			}
		}
	}
}

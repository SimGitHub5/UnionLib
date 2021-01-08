package com.stereowalker.unionlib.client.keybindings;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.network.client.CUnionInventoryPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.NetworkDirection;

@EventBusSubscriber(value = Dist.CLIENT)
public class KeybindingEvents {
	static int fire = 0;
	static int fireR = 0;
	static Minecraft mc = Minecraft.getInstance();
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void keybindTick(ClientTickEvent playerEvent) {
		if(mc.currentScreen==null) {
			ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
			
			if (KeyBindings.OPEN_UNION_INVENTORY.isPressed()) {
				UnionLib.CHANNEL.sendTo(new CUnionInventoryPacket(clientPlayer.getUniqueID()), clientPlayer.connection.getNetworkManager(), NetworkDirection.PLAY_TO_SERVER);
			}
		}
	}
}

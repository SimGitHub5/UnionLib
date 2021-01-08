package com.stereowalker.unionlib.events;

import com.stereowalker.unionlib.Cape;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PatronCapeEvents {

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void entityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof AbstractClientPlayerEntity) {
			if(Cape.doesPlayerNeedCape((AbstractClientPlayerEntity) event.getEntity())) {
				//EntityJoinWorldEvent fires before the player's NetworkPlayerInfo is populated,
				//so we delay replacing the cape by at least 100 milliseconds.
				Cape.queuePlayerCapeReplacement((AbstractClientPlayerEntity) event.getEntity());
			}
		}
	}
}

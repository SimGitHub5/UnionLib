package com.stereowalker.unionlib.event;

import com.stereowalker.unionlib.ClientCape;
import com.stereowalker.unionlib.config.Config;
import com.stereowalker.unionlib.network.client.play.CUpdateCapeListPacket;
import com.stereowalker.unionlib.network.client.play.CQueueCapePacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
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
			if(ClientCape.doesPlayerNeedCapeClient((AbstractClientPlayerEntity) event.getEntity()) && ((AbstractClientPlayerEntity) event.getEntity()) == Minecraft.getInstance().player) {
				//Update the server on who would prefer to display their cape or not
				new CUpdateCapeListPacket(PlayerEntity.getUUID(((AbstractClientPlayerEntity) event.getEntity()).getGameProfile()), Config.display_cape).send();
			}
			new CQueueCapePacket().send();
		}
	}
}

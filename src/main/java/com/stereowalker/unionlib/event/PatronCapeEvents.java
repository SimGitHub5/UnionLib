package com.stereowalker.unionlib.event;

import com.stereowalker.unionlib.ClientCape;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.network.client.play.CQueueCapePacket;
import com.stereowalker.unionlib.network.client.play.CUpdateCapeListPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
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
		if (event.getEntity() instanceof AbstractClientPlayer) {
			if(ClientCape.doesPlayerNeedCapeClient((AbstractClientPlayer) event.getEntity()) && ((AbstractClientPlayer) event.getEntity()) == Minecraft.getInstance().player) {
				//Update the server on who would prefer to display their cape or not
				new CUpdateCapeListPacket(Player.createPlayerUUID(((AbstractClientPlayer) event.getEntity()).getGameProfile()), UnionLib.CONFIG.display_cape).send();
			}
			new CQueueCapePacket().send();
		}
	}
}

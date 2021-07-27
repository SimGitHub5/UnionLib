package com.stereowalker.unionlib.network.client.play;

import com.stereowalker.unionlib.ServerCape;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.network.client.CUnionPacket;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

public class CQueueCapePacket extends CUnionPacket {

	public CQueueCapePacket() {
		super(UnionLib.CHANNEL);
	}

	public CQueueCapePacket (final PacketBuffer packetBuffer) {
		super(packetBuffer, UnionLib.CHANNEL);
	}

	@Override
	public void encode(final PacketBuffer packetBuffer) {
	}

	@Override
	public boolean handleOnServer(ServerPlayerEntity sender) {
		//EntityJoinWorldEvent fires before the player's NetworkPlayerInfo is populated,
		//We also need to wait for the client to send the list of who decided to wear their capes
		//so we delay replacing the cape by at least 100 milliseconds.
		ServerCape.queuePlayerCapeReplacementServer(sender);
		return true;
	}
}

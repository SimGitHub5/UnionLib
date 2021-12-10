package com.stereowalker.unionlib.network.protocol.game;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.stereowalker.unionlib.ClientCape;
import com.stereowalker.unionlib.UnionLib;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SCapePacket extends SUnionPacket {
	private Map<UUID, Boolean> capes;
	private int size;

	public SCapePacket(final Map<UUID, Boolean> capes) {
		super(UnionLib.CHANNEL);
		this.capes = capes;
		this.size = capes.size();
	}

	public SCapePacket (final FriendlyByteBuf packetBuffer) {
		super(packetBuffer, UnionLib.CHANNEL);
		this.capes = Maps.newHashMap();
		this.size = packetBuffer.readInt();
		for (int i = 0; i < this.size; i++) {
			this.capes.put(new UUID(packetBuffer.readLong(), packetBuffer.readLong()), packetBuffer.readBoolean());
		}
	}

	@Override
	public void encode(final FriendlyByteBuf packetBuffer) {
		packetBuffer.writeInt(this.size);
		for (UUID uuid : this.capes.keySet()) {
			packetBuffer.writeLong(uuid.getMostSignificantBits());
			packetBuffer.writeLong(uuid.getLeastSignificantBits());
			packetBuffer.writeBoolean(this.capes.get(uuid));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean handleOnClient(LocalPlayer player) {
		for (UUID uuid : this.capes.keySet()) {
			if (this.capes.get(uuid))
				ClientCape.queuePlayerCapeReplacement((AbstractClientPlayer) player.clientLevel.getPlayerByUUID(uuid));
		}
		return true;
	}
}

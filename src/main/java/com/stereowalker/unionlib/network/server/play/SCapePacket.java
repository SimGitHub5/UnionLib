package com.stereowalker.unionlib.network.server.play;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.stereowalker.unionlib.ClientCape;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.network.server.SUnionPacket;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
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

	public SCapePacket (final PacketBuffer packetBuffer) {
		super(packetBuffer, UnionLib.CHANNEL);
		this.capes = Maps.newHashMap();
		this.size = packetBuffer.readInt();
		for (int i = 0; i < this.size; i++) {
			this.capes.put(new UUID(packetBuffer.readLong(), packetBuffer.readLong()), packetBuffer.readBoolean());
		}
	}

	@Override
	public void encode(final PacketBuffer packetBuffer) {
		packetBuffer.writeInt(this.size);
		for (UUID uuid : this.capes.keySet()) {
			packetBuffer.writeLong(uuid.getMostSignificantBits());
			packetBuffer.writeLong(uuid.getLeastSignificantBits());
			packetBuffer.writeBoolean(this.capes.get(uuid));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean handleOnClient(ClientPlayerEntity player) {
		for (UUID uuid : this.capes.keySet()) {
			if (this.capes.get(uuid))
				ClientCape.queuePlayerCapeReplacement((AbstractClientPlayerEntity) player.worldClient.getPlayerByUuid(uuid));
		}
		return true;
	}
}

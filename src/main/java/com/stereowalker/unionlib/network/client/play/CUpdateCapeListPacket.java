package com.stereowalker.unionlib.network.client.play;

import java.util.UUID;

import com.stereowalker.unionlib.ServerCape;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.network.client.CUnionPacket;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

public class CUpdateCapeListPacket extends CUnionPacket {
	private UUID uuid;
	private boolean isCapeActive;

	public CUpdateCapeListPacket(final UUID uuid, final boolean isCapeActive) {
		super(UnionLib.CHANNEL);
		this.uuid = uuid;
		this.isCapeActive = isCapeActive;
	}

	public CUpdateCapeListPacket (final PacketBuffer packetBuffer) {
		super(packetBuffer, UnionLib.CHANNEL);
		this.uuid = (new UUID(packetBuffer.readLong(), packetBuffer.readLong()));
		this.isCapeActive = packetBuffer.readBoolean();
	}

	@Override
	public void encode(final PacketBuffer packetBuffer) {
		packetBuffer.writeLong(this.uuid.getMostSignificantBits());
		packetBuffer.writeLong(this.uuid.getLeastSignificantBits());
		packetBuffer.writeBoolean(this.isCapeActive);
	}

	@Override
	public boolean handleOnServer(ServerPlayerEntity sender) {
		ServerCape.sendCapeDataToServer(this.uuid, this.isCapeActive);
		return true;
	}
}

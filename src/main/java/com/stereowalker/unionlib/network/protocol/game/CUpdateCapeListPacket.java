package com.stereowalker.unionlib.network.protocol.game;

import java.util.UUID;

import com.stereowalker.unionlib.ServerCape;
import com.stereowalker.unionlib.UnionLib;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class CUpdateCapeListPacket extends CUnionPacket {
	private UUID uuid;
	private boolean isCapeActive;

	public CUpdateCapeListPacket(final UUID uuid, final boolean isCapeActive) {
		super(UnionLib.CHANNEL);
		this.uuid = uuid;
		this.isCapeActive = isCapeActive;
	}

	public CUpdateCapeListPacket (final FriendlyByteBuf packetBuffer) {
		super(packetBuffer, UnionLib.CHANNEL);
		this.uuid = (new UUID(packetBuffer.readLong(), packetBuffer.readLong()));
		this.isCapeActive = packetBuffer.readBoolean();
	}

	@Override
	public void encode(final FriendlyByteBuf packetBuffer) {
		packetBuffer.writeLong(this.uuid.getMostSignificantBits());
		packetBuffer.writeLong(this.uuid.getLeastSignificantBits());
		packetBuffer.writeBoolean(this.isCapeActive);
	}

	@Override
	public boolean handleOnServer(ServerPlayer sender) {
		ServerCape.sendCapeDataToServer(this.uuid, this.isCapeActive);
		return true;
	}
}

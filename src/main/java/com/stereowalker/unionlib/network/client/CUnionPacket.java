package com.stereowalker.unionlib.network.client;

import java.util.function.Supplier;

import com.stereowalker.unionlib.network.BasePacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public abstract class CUnionPacket extends BasePacket{

	public CUnionPacket(SimpleChannel channel) {
		super(channel);
	}
	
	public CUnionPacket(FriendlyByteBuf packetBuffer, SimpleChannel channel) {
		super(packetBuffer, channel);
	}

	@Override
	public void message(final Supplier<NetworkEvent.Context> contextSupplier) {
		final NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			final ServerPlayer sender = context.getSender();
			if (sender == null) {
				return;
			}
			context.setPacketHandled(handleOnServer(sender));
		});
	}
	
	public abstract boolean handleOnServer(ServerPlayer sender);

	public void send() {
		this.channel.sendToServer(this);
	}
}

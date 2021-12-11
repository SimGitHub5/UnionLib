package com.stereowalker.unionlib.network.protocol.game;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public abstract class ServerboundUnionPacket extends BasePacket{

	public ServerboundUnionPacket(SimpleChannel channel) {
		super(channel);
	}
	
	public ServerboundUnionPacket(FriendlyByteBuf packetBuffer, SimpleChannel channel) {
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

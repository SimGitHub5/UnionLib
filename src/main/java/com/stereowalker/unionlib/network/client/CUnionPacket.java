package com.stereowalker.unionlib.network.client;

import java.util.function.Supplier;

import com.stereowalker.unionlib.network.BasePacket;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public abstract class CUnionPacket extends BasePacket{

	public CUnionPacket(SimpleChannel channel) {
		super(channel);
	}
	
	public CUnionPacket(PacketBuffer packetBuffer, SimpleChannel channel) {
		super(packetBuffer, channel);
	}

	@Override
	public void message(final Supplier<NetworkEvent.Context> contextSupplier) {
		final NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			final ServerPlayerEntity sender = context.getSender();
			if (sender == null) {
				return;
			}
			context.setPacketHandled(handleOnServer(sender));
		});
	}
	
	public abstract boolean handleOnServer(ServerPlayerEntity sender);

	public void send() {
		this.channel.sendToServer(this);
	}
}

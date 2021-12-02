package com.stereowalker.unionlib.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public abstract class BasePacket {
	protected SimpleChannel channel;
	
	public BasePacket(SimpleChannel channel) {
		this.channel = channel;
	}
	
	public BasePacket(FriendlyByteBuf packetBuffer, SimpleChannel channel) {
		this.channel = channel;
	}
	
	public abstract void encode(final FriendlyByteBuf packetBuffer);
	public abstract void message(final Supplier<NetworkEvent.Context> contextSupplier);
}

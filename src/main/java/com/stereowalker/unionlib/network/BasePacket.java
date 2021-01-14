package com.stereowalker.unionlib.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public abstract class BasePacket {
	protected SimpleChannel channel;
	
	public BasePacket(SimpleChannel channel) {
		this.channel = channel;
	}
	
	public BasePacket(PacketBuffer packetBuffer, SimpleChannel channel) {
		this.channel = channel;
	}
	
	public abstract void encode(final PacketBuffer packetBuffer);
	public abstract void message(final Supplier<NetworkEvent.Context> contextSupplier);
}

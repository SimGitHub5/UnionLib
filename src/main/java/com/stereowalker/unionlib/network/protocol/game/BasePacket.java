package com.stereowalker.unionlib.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public abstract class BasePacket {
	
	public BasePacket(FriendlyByteBuf packetBuffer) {
	}
	
	public abstract void encode(final FriendlyByteBuf packetBuffer);
	public abstract ResourceLocation getId();
}

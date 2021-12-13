package com.stereowalker.unionlib.network.protocol.game;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public abstract class ServerboundUnionPacket extends BasePacket {

	public ServerboundUnionPacket(FriendlyByteBuf packetBuffer) {
		super(packetBuffer);
	}

	public void message(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, PacketSender responseSender) {
		server.execute(() -> {
			handleOnServer(player);
		});
	}
	
	public abstract boolean handleOnServer(ServerPlayer sender);

	public void send() {
		FriendlyByteBuf buff = new FriendlyByteBuf(Unpooled.buffer());
		this.encode(buff);
		ClientPlayNetworking.send(this.getId(), buff);
	}
}

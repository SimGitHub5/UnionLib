package com.stereowalker.unionlib.network.protocol.game;


import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public abstract class ClientboundUnionPacket extends BasePacket {
	
	public ClientboundUnionPacket(FriendlyByteBuf packetBuffer) {
		super(packetBuffer);
	}

	@Environment(EnvType.CLIENT)
	public void message(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
		client.execute(() -> {
			handleOnClient(client.player);
		});
	}

	public boolean shouldRun() {
		return true;
	}

	@Environment(EnvType.CLIENT)
	public abstract boolean handleOnClient(LocalPlayer player);

	public void send(ServerPlayer playerEntity) {
		FriendlyByteBuf buff = new FriendlyByteBuf(Unpooled.buffer());
		this.encode(buff);
		ServerPlayNetworking.send(playerEntity, this.getId(), buff);
	}

	public void send(ServerLevel world) {
		for (ServerPlayer playerEntity : world.players()) {
			send(playerEntity);
		}
	}

	public void send(MinecraftServer server) {
		for (ServerPlayer playerEntity : server.getPlayerList().getPlayers()) {
			send(playerEntity);
		}
	}
}

package com.stereowalker.unionlib.network.protocol.game;


import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public abstract class ClientboundUnionPacket extends BasePacket {
	
	public ClientboundUnionPacket(SimpleChannel channel) {
		super(channel);
	}
	
	public ClientboundUnionPacket(FriendlyByteBuf packetBuffer, SimpleChannel channel) {
		super(packetBuffer, channel);
	}

	@SuppressWarnings({ "deprecation", "resource" })
	public void message(final Supplier<NetworkEvent.Context> contextSupplier) {
		final NetworkEvent.Context context = contextSupplier.get();
		if (shouldRun()) {
			context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
				context.setPacketHandled(handleOnClient(Minecraft.getInstance().player));
			}));
		}
	}

	public boolean shouldRun() {
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	public abstract boolean handleOnClient(LocalPlayer player);

	public void send(ServerPlayer playerEntity) {
		this.channel.sendTo(this, playerEntity.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
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

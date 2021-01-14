package com.stereowalker.unionlib.network.server;


import java.util.function.Supplier;

import com.stereowalker.unionlib.network.BasePacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public abstract class SUnionPacket extends BasePacket {
	
	public SUnionPacket(SimpleChannel channel) {
		super(channel);
	}
	
	public SUnionPacket(PacketBuffer packetBuffer, SimpleChannel channel) {
		super(packetBuffer, channel);
	}

	@SuppressWarnings("deprecation")
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
	public abstract boolean handleOnClient(ClientPlayerEntity player);

	public void send(ServerPlayerEntity playerEntity) {
		this.channel.sendTo(this, playerEntity.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
	}

	public void send(ServerWorld world) {
		for (ServerPlayerEntity playerEntity : world.getPlayers()) {
			send(playerEntity);
		}
	}

	public void send(MinecraftServer server) {
		for (ServerPlayerEntity playerEntity : server.getPlayerList().getPlayers()) {
			send(playerEntity);
		}
	}
}

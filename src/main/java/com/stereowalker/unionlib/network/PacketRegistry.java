package com.stereowalker.unionlib.network;

import com.stereowalker.unionlib.network.protocol.game.ServerboundUnionInventoryPacket;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class PacketRegistry {
	@Environment(EnvType.CLIENT)
	public static void registerClientboundListeners() {
//		ClientPlayNetworking.registerGlobalReceiver(TileGroupsS2CPacket.ID, TileGroupsS2CPacket::apply);
	}

	public static void registerServerboundListeners() {
		ServerPlayNetworking.registerGlobalReceiver(ServerboundUnionInventoryPacket.id, (server, player, handler, buf, responseSender) -> new ServerboundUnionInventoryPacket(buf).message(server, player, handler, responseSender));
	}
}

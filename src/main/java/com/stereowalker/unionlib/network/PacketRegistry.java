package com.stereowalker.unionlib.network;

import java.util.function.Function;

import com.stereowalker.unionlib.network.protocol.game.BasePacket;
import com.stereowalker.unionlib.network.protocol.game.ServerboundUnionInventoryPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.simple.SimpleChannel;



public class PacketRegistry {
	static int netID = -1;
	public static void registerMessages(SimpleChannel channel) {
		registerMessage(channel, netID++, ServerboundUnionInventoryPacket.class, (packetBuffer) -> {return new ServerboundUnionInventoryPacket(packetBuffer);});
	}

    public static <T extends BasePacket> void registerMessage(SimpleChannel channel, int index, Class<T> messageType, Function<FriendlyByteBuf, T> decoder) {
        channel.registerMessage(index, messageType, (packet,buffer) -> { packet.encode(buffer); }, decoder, (packet,context) -> { packet.message(context);});
    }
}

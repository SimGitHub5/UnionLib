package com.stereowalker.unionlib.network;

import java.util.function.Function;

import com.stereowalker.unionlib.network.client.play.CUnionInventoryPacket;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.simple.SimpleChannel;


public class PacketRegistry {
	static int netID = -1;
	public static void registerMessages(SimpleChannel channel) {
		registerMessage(channel, netID++, CUnionInventoryPacket.class, (packetBuffer) -> {return new CUnionInventoryPacket(packetBuffer);});
	}

    public static <T extends BasePacket> void registerMessage(SimpleChannel channel, int index, Class<T> messageType, Function<PacketBuffer, T> decoder) {
        channel.registerMessage(index, messageType, (packet,buffer) -> { packet.encode(buffer); }, decoder, (packet,context) -> { packet.message(context);});
    }
}

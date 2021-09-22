package com.stereowalker.unionlib.network;

import java.util.function.Function;

import com.stereowalker.unionlib.network.client.play.CQueueCapePacket;
import com.stereowalker.unionlib.network.client.play.CUnionInventoryPacket;
import com.stereowalker.unionlib.network.client.play.CUpdateCapeListPacket;
import com.stereowalker.unionlib.network.server.play.SCapePacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;



public class PacketRegistry {
	static int netID = -1;
	public static void registerMessages(SimpleChannel channel) {
		registerMessage(channel, netID++, CUnionInventoryPacket.class, (packetBuffer) -> {return new CUnionInventoryPacket(packetBuffer);});
		registerMessage(channel, netID++, CQueueCapePacket.class, (packetBuffer) -> {return new CQueueCapePacket(packetBuffer);});
		registerMessage(channel, netID++, CUpdateCapeListPacket.class, (packetBuffer) -> {return new CUpdateCapeListPacket(packetBuffer);});
		registerMessage(channel, netID++, SCapePacket.class, (packetBuffer) -> {return new SCapePacket(packetBuffer);});
	}

    public static <T extends BasePacket> void registerMessage(SimpleChannel channel, int index, Class<T> messageType, Function<FriendlyByteBuf, T> decoder) {
        channel.registerMessage(index, messageType, (packet,buffer) -> { packet.encode(buffer); }, decoder, (packet,context) -> { packet.message(context);});
    }
}

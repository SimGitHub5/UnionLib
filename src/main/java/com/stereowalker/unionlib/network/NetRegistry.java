package com.stereowalker.unionlib.network;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.network.client.CUnionInventoryPacket;


public class NetRegistry {
	public static void registerMessages() {
		int netID = -1;
		UnionLib.CHANNEL.registerMessage(netID++, CUnionInventoryPacket.class, CUnionInventoryPacket::encode, CUnionInventoryPacket::decode, CUnionInventoryPacket::handle);
	}
}

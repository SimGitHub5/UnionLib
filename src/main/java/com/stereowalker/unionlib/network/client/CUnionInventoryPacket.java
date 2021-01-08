package com.stereowalker.unionlib.network.client;

import java.util.UUID;
import java.util.function.Supplier;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.inventory.container.UnionContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class CUnionInventoryPacket {
	private UUID uuid;

	public CUnionInventoryPacket(final UUID uuid) {
		this.uuid = uuid;
	}

	public static void encode(final CUnionInventoryPacket msg, final PacketBuffer packetBuffer) {
		packetBuffer.writeLong(msg.uuid.getMostSignificantBits());
		packetBuffer.writeLong(msg.uuid.getLeastSignificantBits());
	}

	public static CUnionInventoryPacket decode(final PacketBuffer packetBuffer) {
		return new CUnionInventoryPacket(new UUID(packetBuffer.readLong(), packetBuffer.readLong()));
	}

	public static void handle(final CUnionInventoryPacket msg, final Supplier<NetworkEvent.Context> contextSupplier) {
		final NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			final ServerPlayerEntity sender = context.getSender();
			if (sender == null) {
				return;
			}
			final UUID uuid = msg.uuid;
			if (uuid.equals(PlayerEntity.getUUID(sender.getGameProfile()))) {
				sender.openContainer(new SimpleNamedContainerProvider((p_220270_2_, p_220270_3_, p_220270_4_) -> {
					return new UnionContainer(p_220270_2_, p_220270_3_, UnionLib.getAccessoryInventory(sender), sender.world.isRemote, p_220270_4_);
				}, new TranslationTextComponent("")));
			}
		});
		context.setPacketHandled(true);
	}
}

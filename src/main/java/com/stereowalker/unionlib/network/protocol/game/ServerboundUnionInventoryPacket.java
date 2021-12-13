package com.stereowalker.unionlib.network.protocol.game;

import java.util.UUID;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.entity.player.CustomInventoryGetter;
import com.stereowalker.unionlib.inventory.container.UnionContainer;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;

public class ServerboundUnionInventoryPacket extends ServerboundUnionPacket {
	private UUID uuid;

	public ServerboundUnionInventoryPacket(final UUID uuid) {
		super(null);
		this.uuid = uuid;
	}

	public ServerboundUnionInventoryPacket (final FriendlyByteBuf packetBuffer) {
		super(packetBuffer);
		this.uuid = (new UUID(packetBuffer.readLong(), packetBuffer.readLong()));
	}

	@Override
	public void encode(final FriendlyByteBuf packetBuffer) {
		packetBuffer.writeLong(this.uuid.getMostSignificantBits());
		packetBuffer.writeLong(this.uuid.getLeastSignificantBits());
	}

	@Override
	public boolean handleOnServer(ServerPlayer sender) {
		final UUID uuid = this.uuid;
		if (uuid.equals(Player.createPlayerUUID(sender.getGameProfile()))) {
			sender.openMenu(new SimpleMenuProvider((p_220270_2_, p_220270_3_, p_220270_4_) -> {
				return new UnionContainer(p_220270_2_, p_220270_3_, ((CustomInventoryGetter)sender).getUnionInventory(), sender.level.isClientSide, p_220270_4_);
			}, new TranslatableComponent("")));
		}
		return true;
	}

	public static ResourceLocation id = UnionLib.instance.location("serverbound_union_inventory_packet");
	@Override
	public ResourceLocation getId() {
		return id;
	}
}

package com.stereowalker.unionlib.network.client.play;

import java.util.UUID;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.entity.player.CustomInventoryGetter;
import com.stereowalker.unionlib.inventory.container.UnionContainer;
import com.stereowalker.unionlib.network.client.CUnionPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;

public class CUnionInventoryPacket extends CUnionPacket {
	private UUID uuid;

	public CUnionInventoryPacket(final UUID uuid) {
		super(UnionLib.CHANNEL);
		this.uuid = uuid;
	}

	public CUnionInventoryPacket (final FriendlyByteBuf packetBuffer) {
		super(packetBuffer, UnionLib.CHANNEL);
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
}

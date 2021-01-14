package com.stereowalker.unionlib.network.client.play;

import java.util.UUID;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.inventory.container.UnionContainer;
import com.stereowalker.unionlib.network.client.CUnionPacket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;

public class CUnionInventoryPacket extends CUnionPacket {
	private UUID uuid;

	public CUnionInventoryPacket(final UUID uuid) {
		super(UnionLib.CHANNEL);
		this.uuid = uuid;
	}

	public CUnionInventoryPacket (final PacketBuffer packetBuffer) {
		super(packetBuffer, UnionLib.CHANNEL);
		this.uuid = (new UUID(packetBuffer.readLong(), packetBuffer.readLong()));
	}

	@Override
	public void encode(final PacketBuffer packetBuffer) {
		packetBuffer.writeLong(this.uuid.getMostSignificantBits());
		packetBuffer.writeLong(this.uuid.getLeastSignificantBits());
	}

	@Override
	public boolean handleOnServer(ServerPlayerEntity sender) {
		final UUID uuid = this.uuid;
		if (uuid.equals(PlayerEntity.getUUID(sender.getGameProfile()))) {
			sender.openContainer(new SimpleNamedContainerProvider((p_220270_2_, p_220270_3_, p_220270_4_) -> {
				return new UnionContainer(p_220270_2_, p_220270_3_, UnionLib.getAccessoryInventory(sender), sender.world.isRemote, p_220270_4_);
			}, new TranslationTextComponent("")));
		}
		return true;
	}
}

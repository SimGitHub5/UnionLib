package com.stereowalker.unionlib.hook;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.stereowalker.unionlib.event.StructureAddedEvent;
import com.stereowalker.unionlib.event.StructurePieceAddedEvent;
import com.stereowalker.unionlib.event.item.ItemAttributeEvent;
import com.stereowalker.unionlib.event.item.ItemCraftedEvent;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraftforge.common.MinecraftForge;

public class UnionHooks {

	public static void firePlayerCraftingEvent(CraftingContainer craftMatrix, ResultSlot slot, Player player, ItemStack crafted)
    {
        MinecraftForge.EVENT_BUS.post(new ItemCraftedEvent(player, crafted, craftMatrix, slot));
    }

	public static Multimap<Attribute, AttributeModifier> adjustAttributeMap(Multimap<Attribute, AttributeModifier> original, EquipmentSlot equipmentSlot, ItemStack stack) {
		HashMultimap<Attribute, AttributeModifier> modifiedMap = HashMultimap.create(original);
		
		ItemAttributeEvent event = new ItemAttributeEvent(modifiedMap, equipmentSlot, stack);
		
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		if (!MinecraftForge.EVENT_BUS.post(event)) {
			builder.putAll(modifiedMap);
		}
		return builder.build();
	}
}

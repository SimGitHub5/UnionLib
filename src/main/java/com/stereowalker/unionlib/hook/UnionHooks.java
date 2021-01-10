package com.stereowalker.unionlib.hook;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.stereowalker.unionlib.event.StructureAddedEvent;
import com.stereowalker.unionlib.event.StructurePieceAddedEvent;
import com.stereowalker.unionlib.event.item.ItemAttributeEvent;
import com.stereowalker.unionlib.event.item.ItemCraftedEvent;
import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;

public class UnionHooks {

	public static void fireStructureAdded(StructureStart<?> structureStart, ServerWorld world)
	{
		MinecraftForge.EVENT_BUS.post(new StructureAddedEvent(structureStart, world));
	}

	public static void fireStructurePieceAdded(StructurePiece structurePiece, ServerWorld world)
	{
		MinecraftForge.EVENT_BUS.post(new StructurePieceAddedEvent(structurePiece, world));
	}
	
	public static void firePlayerCraftingEvent(CraftingInventory craftMatrix, CraftingResultSlot slot, PlayerEntity player, ItemStack crafted)
    {
        MinecraftForge.EVENT_BUS.post(new ItemCraftedEvent(player, crafted, craftMatrix, slot));
    }

	public static void onStructureAddedHook(StructureStart<?> structureStart, ISeedReader reader) {
		ServerWorld world;

		if (reader instanceof ServerWorld) {
			world = (ServerWorld) reader;
		} else {
			world = ((WorldGenRegion) reader).getWorld();
		}

		fireStructureAdded(structureStart, world);
	}
	
	public static boolean onStructurePieceAddedHook(boolean get, StructurePiece structurePiece, ISeedReader reader) {
		ServerWorld world;

		if (reader instanceof ServerWorld) {
			world = (ServerWorld) reader;
		} else {
			world = ((WorldGenRegion) reader).getWorld();
		}
		
		fireStructurePieceAdded(structurePiece, world);
		
		return get;
	}
	
	public static Multimap<Attribute, AttributeModifier> adjustAttributeMap(Multimap<Attribute, AttributeModifier> original, EquipmentSlotType equipmentSlot, ItemStack stack) {
		HashMultimap<Attribute, AttributeModifier> modifiedMap = HashMultimap.create(original);
		
		ItemAttributeEvent event = new ItemAttributeEvent(modifiedMap, equipmentSlot, stack);
		
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		if (!MinecraftForge.EVENT_BUS.post(event)) {
			builder.putAll(modifiedMap);
		}
		return builder.build();
	}
}

package com.stereowalker.unionlib.event.item;

import com.google.common.collect.HashMultimap;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event is fired whenever an items attribute modifier is being retrieved <br>
 * This allows mod authors to add or remove attribute modifiers from vanilla or other modded items <br>
 * If this event is cancelled, it will give the item an empty attribute map <br>
 * NOTE: Attributes added via this event do not persist. 
 * @author Stereowalker
 *
 */
@Cancelable
public class ItemAttributeEvent extends Event {
	private final ItemStack stack;
	private final HashMultimap<String, AttributeModifier> attributeMap;
	private final EquipmentSlotType equipmentSlot;
	
	public ItemAttributeEvent(HashMultimap<String, AttributeModifier> attributeMap, EquipmentSlotType equipmentSlot, ItemStack stack) {
		this.stack = stack;
		this.equipmentSlot = equipmentSlot;
		this.attributeMap = attributeMap;
	}
	
	public EquipmentSlotType getEquipmentSlot() {
		return equipmentSlot;
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	public HashMultimap<String, AttributeModifier> getAttributeMap() {
		return attributeMap;
	}
}

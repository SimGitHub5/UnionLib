package com.stereowalker.unionlib.event.item;

import com.google.common.collect.HashMultimap;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
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
	private final HashMultimap<Attribute, AttributeModifier> attributeMap;
	private final EquipmentSlot equipmentSlot;
	
	public ItemAttributeEvent(HashMultimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot equipmentSlot, ItemStack stack) {
		this.stack = stack;
		this.equipmentSlot = equipmentSlot;
		this.attributeMap = attributeMap;
	}
	
	public EquipmentSlot getEquipmentSlot() {
		return equipmentSlot;
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	public HashMultimap<Attribute, AttributeModifier> getAttributeMap() {
		return attributeMap;
	}
}

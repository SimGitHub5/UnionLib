package com.stereowalker.unionlib.event;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.entity.ai.UAttributes;
import com.stereowalker.unionlib.inventory.UnionInventory;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BaseGameEvents {

	@SubscribeEvent
	public static void setupDigSpeedAttribute(BreakSpeed event) {
		double attribute = event.getEntityLiving().getAttribute(UAttributes.DIG_SPEED).getValue();
		event.setNewSpeed((float) (event.getOriginalSpeed() * attribute));
	}

	@SubscribeEvent
	public static void loadInventory(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
			UnionInventory unionInventory = UnionLib.getAccessoryInventory(player);
			unionInventory.tick();
			if (!player.getPersistentData().contains(UnionLib.INVENTORY_KEY)) {
				UnionLib.saveInventory(player, unionInventory);
			}
		}
	}

//	public static final Map<EntityType<? extends LivingEntity>,List<IAttribute>> REGISTERED_ATTRIBUTES = Maps.newHashMap();
//	public static final Map<EntityType<? extends LivingEntity>,List<Double>> REGISTERED_BASES = Maps.newHashMap();
//	@SubscribeEvent
//	public static void registerAttributes(EntityConstructing event) {
//		if (event.getEntity() instanceof LivingEntity && REGISTERED_ATTRIBUTES_1.containsKey(((LivingEntity)event.getEntity()).getType())) {
//			for (Pair<IAttribute,Double> ins : REGISTERED_ATTRIBUTES_1.get(((LivingEntity)event.getEntity()).getType())) {
//				((PlayerEntity)event.getEntity()).getAttributes().registerAttribute(ins.getFirst()).setBaseValue(ins.getSecond());;
//			}
//		}
//	}
	
	@SubscribeEvent
	public static void registerAttributes(EntityConstructing event) {
		if (event.getEntity() instanceof PlayerEntity) {
			((PlayerEntity)event.getEntity()).getAttributes().registerAttribute(UAttributes.DIG_SPEED);
		}
	}

	@SubscribeEvent
	public static void dropInventoryItems(LootingLevelEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			dropInventory((PlayerEntity) event.getEntityLiving());
		}
	}

	@SubscribeEvent
	public static void clonePlayer(Clone event) {
		UnionInventory newUnionInventory = UnionLib.getAccessoryInventory(event.getPlayer());
		UnionInventory originalUnionInventory = UnionLib.getAccessoryInventory(event.getOriginal());
		if (!event.isWasDeath()) {
			newUnionInventory.copyInventory(originalUnionInventory);
		} else if (event.getPlayer().world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY) || event.getOriginal().isSpectator()) {
			newUnionInventory.copyInventory(originalUnionInventory);
		}
		UnionLib.saveInventory(event.getPlayer(), newUnionInventory);
		UnionLib.saveInventory(event.getOriginal(), originalUnionInventory);
	}

	protected static void dropInventory(PlayerEntity player) {
		UnionInventory unionInventory = UnionLib.getAccessoryInventory(player);
		if (!player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
			destroyVanishingCursedItems(player);
			unionInventory.dropAllItems();
		}

	}

	protected static void destroyVanishingCursedItems(PlayerEntity player) {
		UnionInventory unionInventory = UnionLib.getAccessoryInventory(player);
		for(int i = 0; i < unionInventory.getSizeInventory(); ++i) {
			ItemStack itemstack = unionInventory.getStackInSlot(i);
			if (!itemstack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemstack)) {
				unionInventory.removeStackFromSlot(i);
			}
		}

	}
}

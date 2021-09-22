package com.stereowalker.unionlib.event;

import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.inventory.UnionInventory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BaseGameEvents {

	@SubscribeEvent
	public static void loadInventory(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) event.getEntityLiving();
			UnionInventory unionInventory = UnionLib.getAccessoryInventory(player);
			unionInventory.tick();
			if (!player.getPersistentData().contains(UnionLib.INVENTORY_KEY)) {
				UnionLib.saveInventory(player, unionInventory);
			}
		}
	}

	@SubscribeEvent
	public static void dropInventoryItems(LootingLevelEvent event) {
		if (event.getEntityLiving() instanceof Player) {
			dropInventory((Player) event.getEntityLiving());
		}
	}

	@SubscribeEvent
	public static void clonePlayer(Clone event) {
		UnionInventory newUnionInventory = UnionLib.getAccessoryInventory(event.getPlayer());
		UnionInventory originalUnionInventory = UnionLib.getAccessoryInventory(event.getOriginal());
		if (!event.isWasDeath()) {
			newUnionInventory.copyInventory(originalUnionInventory);
		} else if (event.getPlayer().level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) || event.getOriginal().isSpectator()) {
			newUnionInventory.copyInventory(originalUnionInventory);
		}
		UnionLib.saveInventory(event.getPlayer(), newUnionInventory);
		UnionLib.saveInventory(event.getOriginal(), originalUnionInventory);
	}

	protected static void dropInventory(Player player) {
		UnionInventory unionInventory = UnionLib.getAccessoryInventory(player);
		if (!player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
			destroyVanishingCursedItems(player);
			unionInventory.dropAllItems();
		}

	}

	protected static void destroyVanishingCursedItems(Player player) {
		UnionInventory unionInventory = UnionLib.getAccessoryInventory(player);
		for(int i = 0; i < unionInventory.getContainerSize(); ++i) {
			ItemStack itemstack = unionInventory.getItem(i);
			if (!itemstack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemstack)) {
				unionInventory.removeItemNoUpdate(i);
			}
		}

	}
}

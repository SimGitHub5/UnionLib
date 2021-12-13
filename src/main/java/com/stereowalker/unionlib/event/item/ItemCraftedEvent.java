//package com.stereowalker.unionlib.event.item;
//
//import javax.annotation.Nonnull;
//
//import net.minecraft.world.Container;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.ResultSlot;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.event.entity.player.PlayerEvent;
//
///**
// * This Event also takes the ResultSlot
// * @author Stereowalker
// *
// */
//public class ItemCraftedEvent extends PlayerEvent {
//    @Nonnull
//    private final ItemStack craftingStack;
//    private final Container craftMatrix;
//    private final ResultSlot resultSlot;
//    public ItemCraftedEvent(Player player, @Nonnull ItemStack crafting, Container craftMatrix, ResultSlot slot)
//    {
//        super(player);
//        this.craftingStack = crafting;
//        this.craftMatrix = craftMatrix;
//        this.resultSlot = slot;
//    }
//
//    @Nonnull
//    public ItemStack getCraftedStack()
//    {
//        return this.craftingStack;
//    }
//
//    public Container getCraftingMatrix()
//    {
//        return this.craftMatrix;
//    }
//
//	public ResultSlot getResultSlot() {
//		return resultSlot;
//	}
//}

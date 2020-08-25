package com.shynieke.portables.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Inventory.class)
public class InventoryDistanceMixin {

  //i think this doesnt even get used at all im not sure
  @Overwrite
  public boolean isUsableByPlayer(PlayerEntity playerIn) {
    //chests and stuff?
    System.out.println("Inventory : isUseableByPlayer " + playerIn.getHeldItem(Hand.MAIN_HAND));
    return true;
  }
}

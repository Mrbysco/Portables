package com.shynieke.portables.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.util.Hand;

@Mixin(ChestContainer.class)
public class ContainerChestDistanceMixin {

  @Overwrite
  public boolean canInteractWith(PlayerEntity playerIn) {
    System.out.println("single target  chest only chest? " + playerIn.getHeldItem(Hand.MAIN_HAND));
    return true;
  }
}

package com.shynieke.portables.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;

@Mixin(Container.class)
public class ContainerDistanceMixin {

  @Overwrite
  protected static boolean isWithinUsableDistance(IWorldPosCallable worldPos, PlayerEntity playerIn, Block targetBlock) {
    System.out.println("This works on grindstone and Loom and workbench and stonecutter but not chest? " + playerIn.getHeldItem(Hand.MAIN_HAND));
    ChestContainer fun;
    return true;
  }
}

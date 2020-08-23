package com.shynieke.portables.registry;

import com.shynieke.portables.Portables;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PortableTab {
    public static final ItemGroup PORTABLE = new ItemGroup(Portables.MOD_ID + ":portable_tab") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(Items.STICK);
        }
    };
}

package com.shynieke.portables.registry;

import com.shynieke.portables.Portables;
import com.shynieke.portables.items.PortableItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PortableRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Portables.MOD_ID);

    public static final RegistryObject<Item> PORTABILITY_STICK = ITEMS.register("portability_stick" , () -> new PortableItem(itemBuilder()));

    private static Item.Properties itemBuilder() {
        return new Item.Properties().group(PortableTab.PORTABLE);
    }
}

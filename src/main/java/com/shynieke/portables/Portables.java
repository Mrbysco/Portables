package com.shynieke.portables;

import com.shynieke.portables.registry.PortableRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Portables.MOD_ID)
public class Portables {
    public static final String MOD_ID = "portables";
    public static final Logger LOGGER = LogManager.getLogger();

    public Portables() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PortableRegistry.ITEMS.register(eventBus);
    }
}

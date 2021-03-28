package com.shynieke.portables.fake;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public class FakePlayerHelper {
    private static GameProfile IE_PROFILE = new GameProfile(UUID.fromString("c7cefe9a-1569-11eb-adc1-0242ac120002"), "[Portables]");
    private static Map<IWorld, PortableFakePlayer> fakePlayerInstances = new HashMap<>();

    public static PortableFakePlayer getFakePlayer(World world) {
        return getFakePlayer(world, IE_PROFILE);
    }

    public static PortableFakePlayer getFakePlayer(World world, GameProfile userProfile) {
        return fakePlayerInstances.computeIfAbsent(world, w -> {
            if(w instanceof ServerWorld)
                return new PortableFakePlayer(FakePlayerFactory.get((ServerWorld)w, userProfile));
            else
                return null;
        });
    }

    @SubscribeEvent
    public static void onUnload(WorldEvent.Unload event) {
        IWorld world = event.getWorld();
        if(world instanceof ServerWorld)
            fakePlayerInstances.remove(world);
    }

}

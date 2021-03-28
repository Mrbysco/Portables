package com.shynieke.portables.fake;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;

public class PortableFakePlayer extends FakePlayer {
    public PortableFakePlayer(ServerWorld world, GameProfile name) {
        super(world, name);
    }

    public PortableFakePlayer(FakePlayer player) {
        super((ServerWorld)player.world, player.getGameProfile());
    }

    @Override
    public Vector3d getPositionVec() {
        return this.positionVec;
    }

    @Override
    public BlockPos getPosition() {
        return this.position;
    }
}

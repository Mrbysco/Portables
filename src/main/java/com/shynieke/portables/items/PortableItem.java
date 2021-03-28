package com.shynieke.portables.items;

import com.shynieke.portables.Portables;
import com.shynieke.portables.fake.FakePlayerHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SOpenWindowPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Open;

import javax.annotation.Nullable;
import java.util.OptionalInt;

public class PortableItem extends Item {
    public PortableItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        BlockRayTraceResult traceResult = rayTrace(worldIn, playerIn, FluidMode.NONE);
        if(!worldIn.isRemote) {
            if(traceResult.getType() == Type.BLOCK) {
                CompoundNBT newTag = stack.hasTag() ? stack.getTag() : new CompoundNBT();
                Vector3d vec = traceResult.getHitVec();

                if(newTag != null) {
                    if(playerIn.isSneaking()) {
                        newTag.putLong("BoundPosition", traceResult.getPos().toLong());
                        newTag.putInt("BoundFace", traceResult.getFace().getIndex());

                        newTag.putDouble("BoundVecX", vec.getX());
                        newTag.putDouble("BoundVecY", vec.getY());
                        newTag.putDouble("BoundVecZ", vec.getZ());
                        stack.setTag(newTag);
                    }

                    if(newTag != null && newTag.contains("BoundPosition") &&
                            newTag.contains("BoundFace") && newTag.contains("BoundVecX") &&
                            newTag.contains("BoundVecY") && newTag.contains("BoundVecZ") ) {
                        executeDistantRightClick(worldIn, playerIn, handIn, stack, newTag);
                    }
                }

                return ActionResult.resultFail(stack);
            } else {
                CompoundNBT tag = stack.hasTag() ? stack.getTag() : new CompoundNBT();
                if(tag != null && tag.contains("BoundPosition") &&
                        tag.contains("BoundFace") && tag.contains("BoundVecX") &&
                        tag.contains("BoundVecY") && tag.contains("BoundVecZ") ) {
                    executeDistantRightClick(worldIn, playerIn, handIn, stack, tag);

                    return ActionResult.resultPass(stack);
                } else {
                    playerIn.sendStatusMessage(new StringTextComponent("Item has not been bound"), true);
                    return ActionResult.resultFail(stack);
                }
            }
        }
        else {
            return ActionResult.resultPass(stack);
        }
    }

    public void executeDistantRightClick(World worldIn, PlayerEntity playerIn, Hand handIn, ItemStack stack, CompoundNBT tag) {
        BlockPos pos = BlockPos.fromLong(tag.getLong("BoundPosition"));
        Direction face = Direction.byIndex(tag.getInt("BoundFace"));
        Vector3d hitVec = new Vector3d(tag.getDouble("BoundVecX"), tag.getDouble("BoundVecY"), tag.getDouble("BoundVecZ"));

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)playerIn;
        BlockRayTraceResult fakeRayTrace = new BlockRayTraceResult(hitVec, face, pos, false);
        BlockState state = worldIn.getBlockState(pos);
        if(state.getBlock() != Blocks.AIR) {
            INamedContainerProvider provider = state.getBlock().getContainer(state, worldIn, pos);
            if(provider != null) {
                ServerPlayerEntity fakePlayer = serverPlayer instanceof FakePlayer ? serverPlayer : FakePlayerHelper.getFakePlayer(worldIn, serverPlayer.getGameProfile());
                fakePlayer.setRawPosition(pos.getX(), pos.getY() + 1, pos.getZ());
                openContainer(serverPlayer, fakePlayer, provider);
            } else {
                ActionResultType actionresulttype = state.onBlockActivated(worldIn, serverPlayer, handIn, fakeRayTrace);
                if (actionresulttype.isSuccessOrConsume()) {
                    CriteriaTriggers.RIGHT_CLICK_BLOCK_WITH_ITEM.test(serverPlayer, pos, stack);
                }
            }
        }
    }

    public OptionalInt openContainer(ServerPlayerEntity playerIn, ServerPlayerEntity fakePlayer, @Nullable INamedContainerProvider containerProvider) {
        if (containerProvider == null) {
            return OptionalInt.empty();
        } else {
            if (playerIn.openContainer != playerIn.container) {
                playerIn.closeScreen();
            }

            playerIn.getNextWindowId();

            Container container = containerProvider.createMenu(playerIn.currentWindowId, fakePlayer.inventory, fakePlayer);
            if (container == null) {
                if (playerIn.isSpectator()) {
                    playerIn.sendStatusMessage((new TranslationTextComponent("container.spectatorCantOpen")).mergeStyle(TextFormatting.RED), true);
                }

                return OptionalInt.empty();
            } else {
                playerIn.connection.sendPacket(new SOpenWindowPacket(container.windowId, container.getType(), containerProvider.getDisplayName()));
                container.addListener(playerIn);
                playerIn.openContainer = container;
                MinecraftForge.EVENT_BUS.post(new Open(fakePlayer, playerIn.openContainer));
                return OptionalInt.of(playerIn.currentWindowId);
            }
        }
    }
}

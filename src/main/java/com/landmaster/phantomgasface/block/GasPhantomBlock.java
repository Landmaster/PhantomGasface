package com.landmaster.phantomgasface.block;

import com.landmaster.phantomgasface.block.entity.GasPhantomBlockEntity;
import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import de.ellpeck.actuallyadditions.mod.blocks.IHudDisplay;
import de.ellpeck.actuallyadditions.mod.blocks.base.BlockContainerBase;
import de.ellpeck.actuallyadditions.mod.blocks.blockhuds.IBlockHud;
import de.ellpeck.actuallyadditions.mod.blocks.blockhuds.PhantomHud;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GasPhantomBlock extends BlockContainerBase implements IHudDisplay {
    private static final IBlockHud HUD = new PhantomHud();

    public GasPhantomBlock() {
        super(ActuallyBlocks.defaultPickProps(4.5f, 10.0f));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos blockPos, @Nonnull BlockState blockState) {
        return new GasPhantomBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? GasPhantomBlockEntity::clientTick : GasPhantomBlockEntity::serverTick;
    }

    @Nonnull
    @Override
    protected ItemInteractionResult useItemOn(@Nonnull ItemStack pStack, @Nonnull BlockState pState, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult pHitResult) {
        if (this.tryToggleRedstone(world, pos, player)) {
            return ItemInteractionResult.SUCCESS;
        }
        if (!world.isClientSide) {
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof MenuProvider menuProvider) {
                player.openMenu(menuProvider, pos);
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Nonnull
    @Override
    public IBlockHud getHud() {
        return HUD;
    }
}

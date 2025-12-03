package com.landmaster.phantomgasface.block.entity;

import com.landmaster.phantomgasface.PhantomGasfaces;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityPhantomface;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GasPhantomBlockEntity extends TileEntityPhantomface {
    public GasPhantomBlockEntity(BlockPos pos, BlockState state) {
        super(PhantomGasfaces.GAS_PHANTOM_TE.get(), pos, state);
    }

    public static <T extends BlockEntity> void clientTick(Level level, BlockPos pos, BlockState state, T t) {
        if (t instanceof GasPhantomBlockEntity tile) {
            tile.clientTick();
        }
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos pos, BlockState state, T t) {
        if (t instanceof GasPhantomBlockEntity tile) {
            tile.serverTick();
        }
    }

    @Override
    public boolean isBoundThingInRange() {
        if (super.isBoundThingInRange()) {
            BlockEntity tile = this.level.getBlockEntity(this.getBoundPosition());
            if (tile != null) {
                for (Direction facing : Direction.values()) {
                    if (this.level.getCapability(Capabilities.CHEMICAL.block(), this.getBoundPosition(), facing) != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

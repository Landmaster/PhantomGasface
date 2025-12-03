package com.landmaster.phantomgasface;

import com.landmaster.phantomgasface.block.GasPhantomBlock;
import com.landmaster.phantomgasface.block.entity.GasPhantomBlockEntity;
import de.ellpeck.actuallyadditions.mod.ActuallyTabs;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

import java.util.function.Supplier;

@Mod(PhantomGasfaces.MODID)
@EventBusSubscriber
public class PhantomGasfaces {
    public static final String MODID = "phantomgasface";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static final DeferredBlock<GasPhantomBlock> GAS_PHANTOM = BLOCKS.register("gas_phantom", GasPhantomBlock::new);
    public static final DeferredItem<?> GAS_PHANTOM_ITEM = ITEMS.registerSimpleBlockItem(GAS_PHANTOM);
    public static final Supplier<BlockEntityType<GasPhantomBlockEntity>> GAS_PHANTOM_TE = TILES.register("gas_phantom",
            () -> BlockEntityType.Builder.of(GasPhantomBlockEntity::new, GAS_PHANTOM.get()).build(null));

    public PhantomGasfaces(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        TILES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    private static void registerCapHandlers(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.CHEMICAL.block(), GAS_PHANTOM_TE.get(), (blockEntity, side) -> {
            if (blockEntity.isBoundThingInRange()) {
                var level = blockEntity.getLevel();
                var boundPos = blockEntity.getBoundPosition();
                BlockEntity tile = level.getBlockEntity(boundPos);
                if (tile != null) {
                    return level.getCapability(Capabilities.CHEMICAL.block(), boundPos, side);
                }
            }
            return null;
        });
    }

    @SubscribeEvent
    private static void registerCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().location().equals(ResourceLocation.fromNamespaceAndPath("actuallyadditions", "tab"))) {
            event.accept(GAS_PHANTOM);
        }
    }
}

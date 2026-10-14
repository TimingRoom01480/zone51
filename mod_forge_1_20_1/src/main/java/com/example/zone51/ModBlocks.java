package com.example.zone51;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Zone51.MODID);

    public static final RegistryObject<Block> ANCIENT_ALTAR = BLOCKS.register("ancient_altar",
            () -> new AncientAltarBlock(BlockBehaviour.Properties.of()
                    .strength(5.0f, 1200.0f).requiresCorrectToolForDrops()
                    .lightLevel(s -> 10).sound(SoundType.STONE)));

    public static final RegistryObject<Block> RADIOACTIVE_WASTE = BLOCKS.register("radioactive_waste",
            () -> new RadioactiveBlock(BlockBehaviour.Properties.of().strength(2.0f)
                    .noOcclusion().lightLevel(s -> 8).sound(SoundType.SLIME_BLOCK)));

    public static final RegistryObject<Block> KEYCARD_DOOR = BLOCKS.register("keycard_door",
            () -> new KeycardDoorBlock(BlockBehaviour.Properties.of().strength(50.0f, 3600000.0f)
                    .sound(SoundType.METAL)));
}

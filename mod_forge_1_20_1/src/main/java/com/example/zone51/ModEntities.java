package com.example.zone51;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Zone51.MODID);

    public static final RegistryObject<EntityType<ThrownCard>> THROWN_CARD = ENTITIES.register("thrown_card",
            () -> EntityType.Builder.<ThrownCard>of(ThrownCard::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("thrown_card"));

    public static final RegistryObject<EntityType<Boss51>> BOSS51 = ENTITIES.register("boss51",
            () -> EntityType.Builder.<Boss51>of(Boss51::new, MobCategory.MONSTER)
                    .sized(1.4f, 3.9f).fireImmune().clientTrackingRange(10).build("boss51"));

    public static final RegistryObject<EntityType<StoneMonster>> STONE_MONSTER = ENTITIES.register("stone_monster",
            () -> EntityType.Builder.<StoneMonster>of(StoneMonster::new, MobCategory.MONSTER)
                    .sized(1.8f, 4.0f).clientTrackingRange(10).build("stone_monster"));

    public static final RegistryObject<EntityType<CrystalMonster>> CRYSTAL_MONSTER = ENTITIES.register("crystal_monster",
            () -> EntityType.Builder.<CrystalMonster>of(CrystalMonster::new, MobCategory.MONSTER)
                    .sized(1.6f, 3.6f).clientTrackingRange(10).build("crystal_monster"));

    public static final RegistryObject<EntityType<CardTrader>> CARD_TRADER = ENTITIES.register("card_trader",
            () -> EntityType.Builder.<CardTrader>of(CardTrader::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.95f).clientTrackingRange(10).build("card_trader"));

    public static final RegistryObject<EntityType<CardsharpBoss>> CARDSHARP = ENTITIES.register("cardsharp",
            () -> EntityType.Builder.<CardsharpBoss>of(CardsharpBoss::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f).clientTrackingRange(10).build("cardsharp"));
}

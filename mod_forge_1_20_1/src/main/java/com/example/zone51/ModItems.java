package com.example.zone51;

import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Zone51.MODID);

    public static final RegistryObject<Item> ENERGY_SHARD = ITEMS.register("energy_shard", () -> new EnergyItem(100, new Item.Properties()));
    public static final RegistryObject<Item> STONE_CORE = simple("stone_core", Rarity.RARE);
    public static final RegistryObject<Item> LIGHTNING_CRYSTAL = ITEMS.register("lightning_crystal", () -> new EnergyItem(250, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> BOSS51_CORE = simple("boss51_core", Rarity.EPIC);
    public static final RegistryObject<Item> ZONE51_KEYCARD = simple("zone51_keycard", Rarity.COMMON);
    public static final RegistryObject<Item> GOLD_COIN = simple("gold_coin", Rarity.COMMON);
    public static final RegistryObject<Item> ANCIENT_HANDLE = simple("ancient_handle", Rarity.RARE);
    public static final RegistryObject<Item> ANCIENT_BLADE = simple("ancient_blade", Rarity.RARE);
    public static final RegistryObject<Item> ANCIENT_GUARD = simple("ancient_guard", Rarity.RARE);
    public static final RegistryObject<Item> ANCIENT_POMMEL = simple("ancient_pommel", Rarity.RARE);
    public static final RegistryObject<Item> ANCIENT_CRYSTAL = simple("ancient_crystal", Rarity.RARE);

    public static final RegistryObject<Item> BOSS51_HEART = ITEMS.register("boss51_heart",
            () -> new Boss51HeartItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> ANCIENT_ALTAR = ITEMS.register("ancient_altar",
            () -> new BlockItem(ModBlocks.ANCIENT_ALTAR.get(), new Item.Properties().rarity(Rarity.EPIC)));

    // Карточник
    public static final RegistryObject<Item> CARD_BELT = ITEMS.register("card_belt",
            () -> new CardBeltItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CARD_DECK = ITEMS.register("card_deck",
            () -> new CardDeckItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Map<CardType, RegistryObject<Item>> CARDS = new EnumMap<>(CardType.class);
    static {
        for (CardType t : CardType.values()) {
            CARDS.put(t, ITEMS.register("card_" + t.id,
                    () -> new CardItem(t, new Item.Properties().stacksTo(16).rarity(t.rare ? Rarity.RARE : Rarity.COMMON))));
        }
    }

    // Древнее Оружие: Shift+ПКМ переключает форму
    public static final RegistryObject<Item> ANCIENT_WEAPON_SWORD = ITEMS.register("ancient_weapon_sword",
            () -> new SwordItem(ModTiers.ANCIENT, 3, -2.4f, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> ANCIENT_WEAPON_PICKAXE = ITEMS.register("ancient_weapon_pickaxe",
            () -> new PickaxeItem(ModTiers.ANCIENT, 1, -2.8f, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> ANCIENT_WEAPON_AXE = ITEMS.register("ancient_weapon_axe",
            () -> new AxeItem(ModTiers.ANCIENT, 5.0f, -3.0f, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> ANCIENT_WEAPON_SHOVEL = ITEMS.register("ancient_weapon_shovel",
            () -> new ShovelItem(ModTiers.ANCIENT, 1.5f, -3.0f, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> ANCIENT_STAFF = ITEMS.register("ancient_staff",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final List<RegistryObject<Item>> WEAPON_FORMS = List.of(ANCIENT_WEAPON_SWORD,
            ANCIENT_WEAPON_PICKAXE, ANCIENT_WEAPON_AXE, ANCIENT_WEAPON_SHOVEL, ANCIENT_STAFF);

    public static final RegistryObject<Item> ENERGY_SWORD = ITEMS.register("energy_sword",
            () -> new SwordItem(ModTiers.ENERGY, 5, -2.2f, new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    // Костюмы
    public static final Map<SuitType, Map<ArmorItem.Type, RegistryObject<Item>>> SUITS = new EnumMap<>(SuitType.class);
    static {
        for (SuitType s : SuitType.values()) {
            Map<ArmorItem.Type, RegistryObject<Item>> m = new EnumMap<>(ArmorItem.Type.class);
            for (ArmorItem.Type t : ArmorItem.Type.values()) {
                m.put(t, ITEMS.register(s.id + "_" + t.getName(),
                        () -> new SuitItem(s, t, new Item.Properties().rarity(Rarity.EPIC))));
            }
            SUITS.put(s, m);
        }
    }

    // Оборудование Зоны 51
    public static final RegistryObject<Item> LASER_GUN = ITEMS.register("laser_gun",
            () -> new LaserGunItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> MEDKIT = ITEMS.register("medkit",
            () -> new MedkitItem(new Item.Properties().stacksTo(8)));
    public static final RegistryObject<Item> LAB_BLUEPRINT = ITEMS.register("lab_blueprint",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> STONE_DUNGEON_MAP = ITEMS.register("stone_dungeon_map",
            () -> new DungeonMapItem("stone_dungeon", new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CRYSTAL_DUNGEON_MAP = ITEMS.register("crystal_dungeon_map",
            () -> new DungeonMapItem("crystal_dungeon", new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ZONE51_MAP = ITEMS.register("zone51_map",
            () -> new DungeonMapItem("zone51", new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RADIOACTIVE_WASTE = ITEMS.register("radioactive_waste",
            () -> new BlockItem(ModBlocks.RADIOACTIVE_WASTE.get(), new Item.Properties()));
    public static final RegistryObject<Item> KEYCARD_DOOR = ITEMS.register("keycard_door",
            () -> new BlockItem(ModBlocks.KEYCARD_DOOR.get(), new Item.Properties()));

    // Яйца призыва
    public static final RegistryObject<Item> BOSS51_SPAWN_EGG = ITEMS.register("boss51_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BOSS51, 0x444455, 0xFF2222, new Item.Properties()));
    public static final RegistryObject<Item> STONE_MONSTER_SPAWN_EGG = ITEMS.register("stone_monster_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.STONE_MONSTER, 0x777780, 0x40C8FF, new Item.Properties()));
    public static final RegistryObject<Item> CRYSTAL_MONSTER_SPAWN_EGG = ITEMS.register("crystal_monster_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.CRYSTAL_MONSTER, 0x6428A0, 0xE0B0FF, new Item.Properties()));
    public static final RegistryObject<Item> CARD_TRADER_SPAWN_EGG = ITEMS.register("card_trader_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.CARD_TRADER, 0x1E1E28, 0xFFC828, new Item.Properties()));
    public static final RegistryObject<Item> CARDSHARP_SPAWN_EGG = ITEMS.register("cardsharp_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.CARDSHARP, 0x1E1E28, 0xE6E6E6, new Item.Properties()));

    private static RegistryObject<Item> simple(String name, Rarity rarity) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().rarity(rarity)));
    }
}

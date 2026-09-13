package com.spaceengineers.craft.registry;

import com.spaceengineers.craft.SpaceEngineersCraft;
import com.spaceengineers.craft.item.SpacesuitItem;
import com.spaceengineers.craft.item.WelderToolItem;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SpaceEngineersCraft.MOD_ID);

    // Tools
    public static final DeferredItem<WelderToolItem> WELDER_TOOL = ITEMS.register("welder_tool",
            () -> new WelderToolItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    // Resources & Bottles
    public static final DeferredItem<Item> OXYGEN_BOTTLE = ITEMS.register("oxygen_bottle",
            () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> HYDROGEN_BOTTLE = ITEMS.register("hydrogen_bottle",
            () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<SpacesuitItem> SPACESUIT_HELMET = ITEMS.register("spacesuit_helmet",
            () -> new SpacesuitItem(ModArmorMaterials.SPACESUIT, ArmorItem.Type.HELMET, new Item.Properties().durability(500).rarity(Rarity.RARE)));

    public static final DeferredItem<SpacesuitItem> SPACESUIT_CHESTPLATE = ITEMS.register("spacesuit_chestplate",
            () -> new SpacesuitItem(ModArmorMaterials.SPACESUIT, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(600).rarity(Rarity.RARE)));

    public static final DeferredItem<SpacesuitItem> SPACESUIT_LEGGINGS = ITEMS.register("spacesuit_leggings",
            () -> new SpacesuitItem(ModArmorMaterials.SPACESUIT, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(550).rarity(Rarity.RARE)));

    public static final DeferredItem<SpacesuitItem> SPACESUIT_BOOTS = ITEMS.register("spacesuit_boots",
            () -> new SpacesuitItem(ModArmorMaterials.SPACESUIT, ArmorItem.Type.BOOTS, new Item.Properties().durability(450).rarity(Rarity.RARE)));

    // Block Items
    public static final DeferredItem<BlockItem> SHIP_CORE_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.SHIP_CORE);
    public static final DeferredItem<BlockItem> STATION_CORE_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.STATION_CORE);
    public static final DeferredItem<BlockItem> COCKPIT_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.COCKPIT);
    public static final DeferredItem<BlockItem> ION_THRUSTER_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.ION_THRUSTER);
    public static final DeferredItem<BlockItem> HYDROGEN_THRUSTER_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.HYDROGEN_THRUSTER);
    public static final DeferredItem<BlockItem> ATMOSPHERIC_THRUSTER_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.ATMOSPHERIC_THRUSTER);
    public static final DeferredItem<BlockItem> RCS_THRUSTER_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.RCS_THRUSTER);
    public static final DeferredItem<BlockItem> GYROSCOPE_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.GYROSCOPE);
    public static final DeferredItem<BlockItem> GRAVITY_GENERATOR_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.GRAVITY_GENERATOR);
    public static final DeferredItem<BlockItem> AIR_VENT_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.AIR_VENT);
    public static final DeferredItem<BlockItem> HYDROGEN_ELECTROLYZER_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.HYDROGEN_ELECTROLYZER);
    public static final DeferredItem<BlockItem> SATELLITE_CORE_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.SATELLITE_CORE);
    public static final DeferredItem<BlockItem> SOLAR_ARRAY_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.SOLAR_ARRAY);
    public static final DeferredItem<BlockItem> SATELLITE_TERMINAL_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.SATELLITE_TERMINAL);

    public static final DeferredItem<BlockItem> LUNAR_REGOLITH_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.LUNAR_REGOLITH);
    public static final DeferredItem<BlockItem> LUNAR_STONE_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.LUNAR_STONE);
    public static final DeferredItem<BlockItem> ARES_SAND_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.ARES_SAND);
    public static final DeferredItem<BlockItem> ARES_ROCK_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.ARES_ROCK);
    public static final DeferredItem<BlockItem> ASTEROID_STONE_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.ASTEROID_STONE);
}

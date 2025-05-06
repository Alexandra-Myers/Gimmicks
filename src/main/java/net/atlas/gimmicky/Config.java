package net.atlas.gimmicky;

import java.io.File;
import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class Config {

    public static String[] blacklistedEffects = new String[0];
    public static String[] tools = new String[] { "wooden_sword", "wooden_axe", "wooden_pickaxe", "wooden_shovel",
        "wooden_hoe", "stone_sword", "stone_axe", "stone_pickaxe", "stone_shovel", "stone_hoe", "golden_sword",
        "golden_axe", "golden_pickaxe", "golden_shovel", "golden_hoe", "iron_sword", "iron_axe", "iron_pickaxe",
        "iron_shovel", "iron_hoe", "diamond_sword", "diamond_axe", "diamond_pickaxe", "diamond_shovel", "diamond_hoe" };
    public static boolean persistGimmickAcrossDeath = true;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        blacklistedEffects = configuration.getStringList(
            "blacklistedEffects",
            Configuration.CATEGORY_GENERAL,
            blacklistedEffects,
            "Effects to exclude from being associated with players...");
        tools = Arrays
            .stream(
                configuration.getStringList(
                    "tools",
                    Configuration.CATEGORY_GENERAL,
                    tools,
                    "Tools obtainable from certain gimmicks..."))
            .filter(Item.itemRegistry::containsKey)
            .toArray(String[]::new);
        persistGimmickAcrossDeath = configuration.getBoolean(
            "persistGimmickAcrossDeath",
            Configuration.CATEGORY_GENERAL,
            persistGimmickAcrossDeath,
            "Should a player's gimmick persist across each death...");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}

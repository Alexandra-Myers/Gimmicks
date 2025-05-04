package net.atlas.gimmicky;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

    public static String[] blacklistedEffects = new String[0];
    public static boolean persistGimmickAcrossDeath = true;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        blacklistedEffects = configuration.getStringList(
            "blacklistedEffects",
            Configuration.CATEGORY_GENERAL,
            blacklistedEffects,
            "Effects to exclude from being associated with players...");
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

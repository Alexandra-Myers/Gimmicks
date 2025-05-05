package net.atlas.gimmicky;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.atlas.gimmicky.gimmick.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Mod(modid = Gimmicky.MODID, version = Tags.VERSION, name = "Gimmicky", acceptedMinecraftVersions = "[1.7.10]")
public class Gimmicky {
    public static final Map<String, Gimmick> gimmicks = new HashMap<>();
    public static final String MODID = "gimmicky";
    public static final String GIMMICK_TAG_NAME = "PlayerGimmick";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "net.atlas.gimmicky.ClientProxy", serverSide = "net.atlas.gimmicky.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        gimmicks.put("nothing", new NothingGimmick());
        gimmicks.put("explodeOnDeath", new ExplosionOnDeathGimmick());
        gimmicks.put("teleportRandomlyOnHurt", new TeleportRandomlyOnHurtGimmick());
        gimmicks.put("stepHeightBoost", new StepHeightGimmick());
        gimmicks.put("dupeCraftedItem", new RandomDupeGimmick());
        gimmicks.put("glassCannon", new GlassCannonGimmick());
        gimmicks.put("invertLavaAndWater", new InvertLavaAndWaterGimmick());
        gimmicks.put("randomToolOnRespawn", new RandomToolUponRespawnGimmick());
        for (String s : Config.blacklistedEffects) {
            if (s.equals("nothing")) continue;
            gimmicks.remove(s);
        }
        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
    public static String getRandomGimmick(Random random) {
        List<String> gimmickNames = new ArrayList<>(gimmicks.keySet());
        return gimmickNames.get(random.nextInt(gimmickNames.size()));
    }
}

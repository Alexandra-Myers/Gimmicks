package net.atlas.gimmicky;

import java.util.*;

import net.atlas.gimmicky.gimmick.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

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
        gimmicks.put("creeperInstincts", new ExplosionOnDeathGimmick());
        gimmicks.put("endermanInstincts", new TeleportRandomlyOnHurtGimmick());
        gimmicks.put("acrobat", new StepHeightGimmick());
        gimmicks.put("efficientCraftsman", new RandomDupeGimmick());
        gimmicks.put("noPainNoGain", new GlassCannonGimmick());
        gimmicks.put("aquaphobia", new InvertLavaAndWaterGimmick());
        gimmicks.put("giftFromAbove", new RandomToolUponRespawnGimmick());
        gimmicks.put("unsteadyHands", new UnsteadyHandsGimmick());
        gimmicks.put("surefooted", new KnockbackResistanceBoostGimmick());
        proxy.preInit(event);
        for (String s : Config.blacklistedEffects) {
            gimmicks.remove(s);
        }
        if (gimmicks.isEmpty()) gimmicks.put("nothing", new NothingGimmick());
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

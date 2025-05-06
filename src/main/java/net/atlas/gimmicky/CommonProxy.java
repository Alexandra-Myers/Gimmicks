package net.atlas.gimmicky;

import static net.atlas.gimmicky.Gimmicky.MODID;

import net.atlas.gimmicky.command.CommandGimmickUpdate;
import net.atlas.gimmicky.event.ForgeEventBusHandler;
import net.atlas.gimmicky.event.ModEventBusHandler;
import net.atlas.gimmicky.gimmick.packet.PacketSyncGimmick;
import net.atlas.gimmicky.item.ItemTomeOfGimmicks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

    public static ItemTomeOfGimmicks tomeOfGimmicks;
    public SimpleNetworkWrapper simpleNetworkWrapper;

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance()
            .bus()
            .register(new ModEventBusHandler());
        MinecraftForge.EVENT_BUS.register(new ForgeEventBusHandler());
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        simpleNetworkWrapper = new SimpleNetworkWrapper(MODID);
        simpleNetworkWrapper.registerMessage(PacketSyncGimmick.Handler.class, PacketSyncGimmick.class, 0, Side.CLIENT);
        tomeOfGimmicks = new ItemTomeOfGimmicks();
        GameRegistry.registerItem(tomeOfGimmicks, "tome_of_gimmicks");
        CraftingManager.getInstance()
            .addRecipe(
                new ItemStack(tomeOfGimmicks, 1),
                " # ",
                "XBX",
                " # ",
                '#',
                Items.blaze_rod,
                'X',
                Items.ghast_tear,
                'B',
                Items.book);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandGimmickUpdate());
    }
}

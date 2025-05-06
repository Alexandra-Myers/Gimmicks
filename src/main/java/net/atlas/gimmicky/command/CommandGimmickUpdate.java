package net.atlas.gimmicky.command;

import static net.atlas.gimmicky.Gimmicky.getRandomGimmick;
import static net.atlas.gimmicky.Gimmicky.gimmicks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import net.atlas.gimmicky.Gimmicky;
import net.atlas.gimmicky.gimmick.GimmickExtendedEntityProperty;
import net.atlas.gimmicky.gimmick.packet.PacketSyncGimmick;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.IExtendedEntityProperties;

public class CommandGimmickUpdate extends CommandBase {

    @Override
    public String getCommandName() {
        return "gimmick";
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/gimmick [player=???] [gimmick=???] [retrieve=true|false]";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        Hashtable<String, String> arguments = new Hashtable<>();
        for (String arg : args) {
            int eqIndex = arg.indexOf('=');
            if (eqIndex > 0) arguments.put(arg.substring(0, eqIndex), arg.substring(eqIndex + 1));
        }

        EntityPlayerMP player = null;
        if (arguments.containsKey("player")) {
            try {
                player = getPlayer(commandSender, arguments.get("player"));
            } catch (Exception ignored) {

            }
        }

        if (player == null) player = getCommandSenderAsPlayer(commandSender);
        IExtendedEntityProperties gimmickProperties = player.getExtendedProperties("gimmicky:gimmick");
        if (!(gimmickProperties instanceof GimmickExtendedEntityProperty)) return;
        GimmickExtendedEntityProperty gimmickExtendedEntityProperty = (GimmickExtendedEntityProperty) gimmickProperties;

        if ((!arguments.containsKey("retrieve") && !arguments.containsKey("gimmick"))
            || "true".equals(arguments.get("retrieve"))) {
            String gimmick = gimmickExtendedEntityProperty.getGimmickName();
            func_152373_a(commandSender, this, "commands.gimmick.retrieve", player.getDisplayName(), gimmick);
            return;
        }

        String gimmick = arguments.get("gimmick");
        if (!gimmicks.containsKey(gimmick)) gimmick = null;
        if (gimmick != null) gimmickExtendedEntityProperty.setGimmick(gimmick);
        else gimmickExtendedEntityProperty.setGimmick(getRandomGimmick(gimmickExtendedEntityProperty.getRandom()));
        gimmickExtendedEntityProperty.saveToCustomData(player.getEntityData());
        Gimmicky.proxy.simpleNetworkWrapper
            .sendToAll(new PacketSyncGimmick(gimmickExtendedEntityProperty.getGimmickName(), player.getEntityId()));
        func_152373_a(
            commandSender,
            this,
            "commands.gimmick.success",
            player.getDisplayName(),
            gimmickExtendedEntityProperty.getGimmickName());
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
        if (par2ArrayOfStr.length == 0) return null;

        List<String> newArgs = new ArrayList<>();

        if (doesNotHaveArgument("player", par2ArrayOfStr))
            newArgs.addAll(Arrays.asList(prefixArgumentsWithKey("player", this.getPlayers())));

        if (doesNotHaveArgument("gimmick", par2ArrayOfStr)) newArgs.addAll(
            Arrays.asList(
                prefixArgumentsWithKey(
                    "gimmick",
                    gimmicks.keySet()
                        .toArray(new String[0]))));

        if (doesNotHaveArgument("retrieve", par2ArrayOfStr))
            newArgs.addAll(Arrays.asList(prefixArgumentsWithKey("retrieve", "true", "false")));

        return getListOfStringsMatchingLastWord(par2ArrayOfStr, newArgs.toArray(new String[0]));
    }

    private static boolean doesNotHaveArgument(String key, String[] arguments) {
        for (int i = 0; i < arguments.length - 1; i++) {
            if (arguments[i].startsWith(key + "=")) return false;
        }

        return true;
    }

    private static String[] prefixArgumentsWithKey(String key, String... currentArguments) {
        String[] prefixed = new String[currentArguments.length];
        for (int i = 0; i < prefixed.length; i++) prefixed[i] = key + "=" + currentArguments[i];
        return prefixed;
    }

    private static String[] getMissingArguments(String[] currentArguments, String... arguments) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (String s : arguments) {
            if (doesNotHaveArgument(s, currentArguments)) arrayList.add(s + "=");
        }

        return arrayList.toArray(new String[0]);
    }

    protected String[] getPlayers() {
        return MinecraftServer.getServer()
            .getAllUsernames();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {
        return par2 == 0;
    }
}

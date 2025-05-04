package net.atlas.gimmicky.gimmick;

import net.atlas.gimmicky.Gimmicky;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.Random;

import static net.atlas.gimmicky.Gimmicky.*;

public class GimmickExtendedEntityProperty implements IExtendedEntityProperties {
    private Random random;
    private String gimmick;
    @Override
    public void saveNBTData(NBTTagCompound compound) {
        if (!gimmick.isEmpty()) compound.setString(Gimmicky.GIMMICK_TAG_NAME, gimmick);
        else if (compound.hasKey(Gimmicky.GIMMICK_TAG_NAME)) compound.removeTag(GIMMICK_TAG_NAME);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (!compound.hasKey(Gimmicky.GIMMICK_TAG_NAME)) {
            gimmick = getRandomGimmick(random);
            return;
        } else if (!gimmicks.containsKey(compound.getString(Gimmicky.GIMMICK_TAG_NAME))) {
            compound.removeTag(Gimmicky.GIMMICK_TAG_NAME);
            gimmick = getRandomGimmick(random);
            return;
        }
        gimmick = compound.getString(Gimmicky.GIMMICK_TAG_NAME);
    }

    @Override
    public void init(Entity entity, World world) {
        random = new Random(world.rand.nextLong());
    }

    public Random getRandom() {
        return random;
    }

    public void setGimmick(String gimmick) {
        this.gimmick = gimmick;
    }

    public Gimmick getGimmick() {
        return gimmicks.get(gimmick);
    }

    public String getGimmickName() {
        return gimmick;
    }
}

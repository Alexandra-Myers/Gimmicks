package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.eventhandler.Event;

public class NothingGimmick implements Gimmick {
    @Override
    public boolean appliesOnEvent(Event event) {
        return false;
    }

    @Override
    public void handleEvent(Event event) {

    }
}

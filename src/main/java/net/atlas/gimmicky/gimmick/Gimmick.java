package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.eventhandler.Event;

public interface Gimmick {
    boolean appliesOnEvent(Event event);
    void handleEvent(Event event);
}

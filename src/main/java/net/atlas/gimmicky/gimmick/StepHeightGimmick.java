package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.TickEvent;

public class StepHeightGimmick implements Gimmick {
    @Override
    public boolean appliesOnEvent(Event event) {
        return event instanceof TickEvent.PlayerTickEvent;
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event instanceof TickEvent.PlayerTickEvent)) return;
        TickEvent.PlayerTickEvent playerTickEvent = (TickEvent.PlayerTickEvent) event;
        if (playerTickEvent.player.isSprinting()) playerTickEvent.player.stepHeight = 1;
        else playerTickEvent.player.stepHeight = 0.5F;
    }
}

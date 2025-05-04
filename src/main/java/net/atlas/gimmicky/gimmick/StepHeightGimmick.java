package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.TickEvent;

public class StepHeightGimmick extends GenericGimmick<TickEvent.PlayerTickEvent> {
    public StepHeightGimmick() {
        super(TickEvent.PlayerTickEvent.class);
    }

    @Override
    public boolean appliesOnEvent(Event event) {
        return event instanceof TickEvent.PlayerTickEvent;
    }

    @Override
    public void handleTypedEvent(TickEvent.PlayerTickEvent event) {
        if (event.player.isSprinting()) event.player.stepHeight = 1;
        else event.player.stepHeight = 0.5F;
    }

    @Override
    public void finaliseTypedEvent(TickEvent.PlayerTickEvent event) {
        event.player.stepHeight = 0.5F;
    }
}

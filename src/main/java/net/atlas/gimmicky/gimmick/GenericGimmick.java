package net.atlas.gimmicky.gimmick;

import cpw.mods.fml.common.eventhandler.Event;

public abstract class GenericGimmick<E extends Event> implements Gimmick {
    public final Class<E> clazz;

    protected GenericGimmick(Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean appliesOnEvent(Event event) {
        return clazz.isInstance(event);
    }

    @Override
    public final void handleEvent(Event event) {
        if (!clazz.isInstance(event)) return;
        handleTypedEvent(clazz.cast(event));
    }

    @Override
    public final void finaliseEvent(Event event) {
        if (!clazz.isInstance(event)) return;
        finaliseTypedEvent(clazz.cast(event));
    }
    public abstract void handleTypedEvent(E event);
    public void finaliseTypedEvent(E event) {

    }
}

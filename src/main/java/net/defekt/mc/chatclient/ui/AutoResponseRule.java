package net.defekt.mc.chatclient.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("javadoc")
public class AutoResponseRule implements Serializable {
    private static final long serialVersionUID = 2057896755819059984L;

    public static enum EffectType {
        RANDOM, ORDERED, ALL
    }

    public static enum TriggerType {
        AND, OR
    }

    @Override
    public String toString() {
        return getName();
    }

    private transient Random rand = new Random();

    private String name;
    private int interval;
    private List<String> triggers = new ArrayList<String>();
    private List<String> exceptions = new ArrayList<String>();
    private List<String> effects = new ArrayList<String>();
    private EffectType effectType;
    private TriggerType triggerType;

    private transient int index = 0;

    public String[] match(String message) {
        if (rand == null) {
            rand = new Random();
        }
        if (index < 0) {
            index = 0;
        }
        if (effects.size() == 0) return null;
        message = message.toLowerCase();
        boolean matches = false;
        for (final String trigger : triggers)
            if (message.contains(trigger.toLowerCase())) {
                matches = true;
                if (triggerType == TriggerType.OR) {
                    break;
                }
            } else if (triggerType == TriggerType.AND) return null;
        for (final String exception : exceptions)
            if (message.contains(exception.toLowerCase())) {
                matches = false;
                break;
            }

        if (matches) {
            switch (effectType) {
                case ALL: {
                    return effects.toArray(new String[effects.size()]);
                }
                case ORDERED: {
                    final String rt = effects.get(index);
                    index++;
                    index %= effects.size();
                    return new String[] { rt };
                }
                default: {
                    return new String[] { effects.get(rand.nextInt(effects.size())) };
                }
            }
        } else
            return null;
    }

    public AutoResponseRule(final String name, final EffectType effectType, final TriggerType triggerType,
            final int interval, final String[] triggers, final String[] exceptions, final String[] effects) {
        this.name = name;
        this.effectType = effectType;
        this.triggerType = triggerType;
        this.interval = interval;
        if (triggers != null) {
            Collections.addAll(this.triggers, triggers);
        }
        if (exceptions != null) {
            Collections.addAll(this.exceptions, exceptions);
        }
        if (effects != null) {
            Collections.addAll(this.effects, effects);
        }
    }

    public String getName() {
        return name;
    }

    public int getInterval() {
        return interval;
    }

    public List<String> getTriggers() {
        return new ArrayList<>(triggers);
    }

    public List<String> getExceptions() {
        return new ArrayList<String>(exceptions);
    }

    public List<String> getEffects() {
        return new ArrayList<String>(effects);
    }

    public EffectType getType() {
        return effectType;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setInterval(final int interval) {
        this.interval = interval;
    }

    public void setTriggers(final String[] triggers) {
        this.triggers = new ArrayList<String>();
        Collections.addAll(this.triggers, triggers);
    }

    public void setExceptions(final String[] exceptions) {
        this.exceptions = new ArrayList<String>();
        Collections.addAll(this.exceptions, exceptions);
    }

    public void setEffects(final String[] effects) {
        this.effects = new ArrayList<String>();
        Collections.addAll(this.effects, effects);
    }

    public void setType(final EffectType type) {
        this.effectType = type;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(final TriggerType triggerType) {
        this.triggerType = triggerType;
    }
}

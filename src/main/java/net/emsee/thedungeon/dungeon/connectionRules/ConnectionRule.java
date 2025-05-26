package net.emsee.thedungeon.dungeon.connectionRules;

import java.util.List;

public abstract class ConnectionRule {
    public static final String DEFAULT_CONNECTION_TAG = "default";

    protected final String from;
    protected final String to;

    public static boolean isValid(String fromTag, String toTag, List<ConnectionRule> rules) {
        boolean toReturn = fromTag.equals(toTag);
        for (ConnectionRule rule : rules) {
            if (rule.preventConnect(fromTag, toTag)) return false;
            toReturn = toReturn || rule.canConnect(fromTag, toTag);
        }
        return toReturn;
    }

    protected ConnectionRule(String fromTag, String toTag) {
        this.from = fromTag;
        this.to = toTag;
    }

    /**
     * if returned by any rule the connection is valid
     **/
    protected abstract boolean canConnect(String fromTag, String toTag);

    /**
     * if returned by any rule the connection is invalid, overrides canConnect
     **/
    protected abstract boolean preventConnect(String fromTag, String toTag);
}



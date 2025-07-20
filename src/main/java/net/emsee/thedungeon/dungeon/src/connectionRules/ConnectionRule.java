package net.emsee.thedungeon.dungeon.src.connectionRules;

import java.util.List;

/**
 * a connection rule for managing what rooms are and what rooms are not allowed to connect
 */
public abstract class ConnectionRule {
    public static final String DEFAULT_CONNECTION_TAG = "default";

    protected final String from;
    protected final String to;

    /**
     * loops through a list of ConnectionRules to see if 2 tags are valid or not
     */
    public static boolean isValid(String fromTag, String toTag, List<ConnectionRule> rules) {
        boolean toReturn = fromTag.equals(toTag);
        for (ConnectionRule rule : rules) {
            if (rule.preventConnect(fromTag, toTag)) return false;
            toReturn = toReturn || rule.canConnect(fromTag, toTag);
        }
        return toReturn;
    }

    /**
     * a connection rule for managing what rooms are and what rooms are not allowed to connect
     */
    protected ConnectionRule(String fromTag, String toTag) {
        this.from = fromTag;
        this.to = toTag;
    }

    /**
     * returns true if the two tags can connect according to the rule
     */
    protected abstract boolean canConnect(String fromTag, String toTag);

    /**
     * if returned by any rule the connection is invalid, overrides any canConnect
     **/
    protected abstract boolean preventConnect(String fromTag, String toTag);
}



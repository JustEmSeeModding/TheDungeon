package net.emsee.thedungeon.dungeon.connectionRules.connection;

import net.emsee.thedungeon.dungeon.connectionRules.ConnectionRule;

public class CanConnectBothWays extends ConnectionRule {
    public CanConnectBothWays(String fromTag, String toTag) {
        super(fromTag, toTag);
    }

    @Override
    protected boolean canConnect(String fromTag, String toTag) {
        return (fromTag.equals(from) && toTag.equals(to)) ||
                (fromTag.equals(to) && toTag.equals(from));
    }

    @Override
    public boolean preventConnect(String fromTag, String toTag) {
        return false;
    }
}
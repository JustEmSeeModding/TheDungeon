package net.emsee.thedungeon.dungeon.src.connectionRules.connection;

import net.emsee.thedungeon.dungeon.src.connectionRules.ConnectionRule;

public class CanConnectOneWay extends ConnectionRule {
    public CanConnectOneWay(String fromTag, String toTag) {
        super(fromTag, toTag);
    }

    @Override
    protected boolean canConnect(String fromTag, String toTag) {
        return fromTag.equals(from) && toTag.equals(to);
    }

    @Override
    protected boolean preventConnect(String fromTag, String toTag) {
        return false;
    }
}

package net.emsee.thedungeon.dungeon.connectionRules.connection;


import net.emsee.thedungeon.dungeon.connectionRules.ConnectionRule;

public class CantConnectToSelf extends ConnectionRule {
    public CantConnectToSelf(String tag) {
        super(tag, tag);
    }

    @Override
    protected boolean canConnect(String fromTag, String toTag) {
        return false;
    }

    @Override
    protected boolean preventConnect(String fromTag, String toTag) {
        return (fromTag.equals(from) && toTag.equals(to));
    }
}


package net.emsee.thedungeon.item.custom;

import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class TestBeltItem extends DungeonItem implements ICurioItem {
    public TestBeltItem(Properties properties) {
        super(properties.stacksTo(1));
    }
}

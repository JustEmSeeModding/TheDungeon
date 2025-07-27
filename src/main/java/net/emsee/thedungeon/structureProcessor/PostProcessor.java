package net.emsee.thedungeon.structureProcessor;

import net.emsee.thedungeon.utils.BlockUtils;

/**
 *  post processors are custom used in the dungeon generators.
 * <p>
 *  use these when you want to have the structure placed as this calculates.
 *  <p>
 *  can still be used as a normal processor in some cases but results are not guaranteed.
 */
public interface PostProcessor {
    BlockUtils.ForEachMethod getMethod();
}

package net.emsee.thedungeon.events;

//@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public class LevelEvents {
    /*
    TODO there is a lot of issues with this that need to be fixed (also might no longer be needed) or replace with something else
    the issue is that when the entity killing happens, if entities are in unloaded chunks they are ignored
    at least the items dropped from invalid placed blocks are ignored like that (check out goblin caves crystal biome, the amethyst clusters)
    */

    /*
    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if(!event.getLevel().isClientSide())
            GlobalDungeonManager.updateForcedChunks(Objects.requireNonNull(event.getLevel().getServer()));
    }
    */

}

package net.emsee.thedungeon.renderer;

import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.minecraft.client.renderer.RenderStateShard;

public class ModRenderStateShard {

    public static RenderStateShard.ShaderStateShard dungeonPortal(DungeonRank rank, boolean stable, boolean exit) {
        return new RenderStateShard.ShaderStateShard(() -> {
            if (!stable) {
                return ModShaders.DUNGEON_PORTAL_UNSTABLE;
            }
            return switch (rank) {
                case F -> ModShaders.DUNGEON_PORTAL_F;
                case E -> ModShaders.DUNGEON_PORTAL_E;
                case D -> ModShaders.DUNGEON_PORTAL_D;
                case C -> ModShaders.DUNGEON_PORTAL_C;
                case B -> ModShaders.DUNGEON_PORTAL_B;
                case A -> ModShaders.DUNGEON_PORTAL_A;
                case S -> ModShaders.DUNGEON_PORTAL_S;
                case SS -> ModShaders.DUNGEON_PORTAL_SS;
            };
        });
    }
}

package net.emsee.thedungeon.block.entity.custom.fightRoom;

import net.emsee.thedungeon.block.entity.ModBlockEntities;
import net.emsee.thedungeon.dungeon.src.mobSpawnRules.rules.SpawnAt;
import net.emsee.thedungeon.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TestFightRoomBlockEntity extends AbstractFightRoomBlockEntity {
    public TestFightRoomBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FIGHT_ROOM_BLOCK_ENTITY.get(), pos, blockState);
    }

    @Override
    protected Fight setupFight() {
        return new Fight()
                .addWave(new FightWave()
                                .addSpawn(new SpawnAt<>(ModEntities.HOB_GOBLIN, new BlockPos(0,1,0)))
                                .addSpawn(new SpawnAt<>(ModEntities.HOB_GOBLIN, new BlockPos(5,1,5)))
                )
                .addWave(new FightWave()
                                .addSpawn(new SpawnAt<>(ModEntities.HOB_GOBLIN, new BlockPos(0,1,0)))
                                .addSpawn(new SpawnAt<>(ModEntities.HOB_GOBLIN, new BlockPos(5,1,5)))
                );
    }

    @Override
    protected BlockPos getNegativeCornerOffset() {
        return new BlockPos(-5, 0, -5);
    }

    @Override
    protected BlockPos getPositiveCornerOffset() {
        return new BlockPos(5, 5, 5);
    }

    @Override
    protected void onFightStart() {}

    @Override
    protected void onProgressWave(int progress) {}

    @Override
    protected void onFightEnd() {}

    @Override
    protected void onFightEndEarly(int progress) {}

}

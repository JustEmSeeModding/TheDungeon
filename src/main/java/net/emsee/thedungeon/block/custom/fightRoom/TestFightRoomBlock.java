package net.emsee.thedungeon.block.custom.fightRoom;

import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.entity.ModBlockEntities;
import net.emsee.thedungeon.block.entity.custom.fightRoom.TestFightRoomBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TestFightRoomBlock extends AbstractFightRoomBlock {
    public static final MapCodec<TestFightRoomBlock> CODEC = simpleCodec(TestFightRoomBlock::new);

    public TestFightRoomBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canStartFight(BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> createTicker(BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.FIGHT_ROOM_BLOCK_ENTITY.get(),
                (level1, blockPos, blockState, blockEntity) ->
                        blockEntity.tick(level1, blockPos, blockState, blockEntity));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TestFightRoomBlockEntity(blockPos, blockState);
    }
}

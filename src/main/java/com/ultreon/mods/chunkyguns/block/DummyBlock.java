package com.ultreon.mods.chunkyguns.block;

import com.ultreon.mods.chunkyguns.util.BlockUtil;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class DummyBlock extends HorizontalFacingBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape UPPER_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(5, 7, 5, 11, 13, 11),
            Block.createCuboidShape(4, 0, 6, 12, 6, 10),
            Block.createCuboidShape(7, 6, 7, 9, 7, 9),
            Block.createCuboidShape(0, 3, 7, 4, 5, 9),
            Block.createCuboidShape(12, 3, 7, 16, 5, 9));

    private static final VoxelShape LOWER_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(4, 10, 6, 12, 16, 10),
            Block.createCuboidShape(7, 2, 7, 9, 10, 9),
            Block.createCuboidShape(5, 0, 5, 11, 2, 11));

    public DummyBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(FACING, Direction.NORTH).with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        DoubleBlockHalf half = state.get(HALF);

        return switch (facing) {
            case EAST, WEST -> BlockUtil.rotateShape(1, getHalfShape(half));
            default -> BlockUtil.rotateShape(2, getHalfShape(half));
        };
    }

    private VoxelShape getHalfShape(DoubleBlockHalf half) {
        return switch (half) {
            case UPPER -> UPPER_SHAPE;
            case LOWER -> LOWER_SHAPE;
        };
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.get(HALF);
        if (direction.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            return neighborState.isOf(this) && neighborState.get(HALF) != half ? state.with(FACING, neighborState.get(FACING)) : Blocks.AIR.getDefaultState();
        }
        else {
            return half == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            TallPlantBlock.onBreakInCreative(world, pos, state, player);
        }

        super.onBreak(world, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx)) {
            return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(HALF, DoubleBlockHalf.LOWER);
        }
        else {
            return null;
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos downPos = pos.down();
        BlockState downState = world.getBlockState(downPos);
        return state.get(HALF) == DoubleBlockHalf.LOWER ? downState.isSideSolidFullSquare(world, downPos, Direction.UP) : downState.isOf(this);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }
}

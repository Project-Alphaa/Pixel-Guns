package com.ultreon.mods.chunkyguns.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class BottleBlock extends Block {

    private static final VoxelShape SHAPE = VoxelShapes.union(Block.createCuboidShape(6.510000000000001, -1.3299999999999998, 6.5600000000000005, 9.549999999999999, 9.31, 9.6),
            Block.createCuboidShape(6.4624999999999995, -1.3775, 6.5125, 9.5975, 9.357500000000002, 9.6475),
            Block.createCuboidShape(6.89, 9.31, 6.9399999999999995, 9.170000000000002, 10.45, 9.219999999999999),
            Block.createCuboidShape(7.2700000000000005, 10.45, 7.32, 8.79, 13.870000000000001, 8.84),
            Block.createCuboidShape(7.08, 13.870000000000001, 7.13, 8.98, 14.63, 9.030000000000001));

    public BottleBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }
}

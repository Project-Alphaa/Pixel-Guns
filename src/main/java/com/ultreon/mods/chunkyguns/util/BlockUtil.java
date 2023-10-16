package com.ultreon.mods.chunkyguns.util;

import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class BlockUtil {

    public static VoxelShape rotateShape(int times, VoxelShape shape) {
        VoxelShape[] shapes = new VoxelShape[]{shape, VoxelShapes.empty()};

        for (int i = 0; i < times; i++) {
            shapes[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> shapes[1] = VoxelShapes.union(shapes[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            shapes[0] = shapes[1];
            shapes[1] = VoxelShapes.empty();
        }

        return shapes[0];
    }
}

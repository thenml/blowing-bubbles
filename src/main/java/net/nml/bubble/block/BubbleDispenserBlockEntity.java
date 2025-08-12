package net.nml.bubble.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.nml.bubble.ModRegistry;

public class BubbleDispenserBlockEntity extends BlockEntity {
	public BubbleDispenserBlockEntity(BlockPos pos, BlockState state) {
		super(ModRegistry.BUBBLE_DISPENSER_BLOCK_ENTITY, pos, state);
	}
}

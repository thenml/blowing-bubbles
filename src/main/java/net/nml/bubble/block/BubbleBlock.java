package net.nml.bubble.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Stainable;
import net.minecraft.block.TransparentBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BubbleBlock extends TransparentBlock implements Stainable {
	public static final MapCodec<BubbleBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(DyeColor.CODEC.fieldOf("color").forGetter(BubbleBlock::getColor), createSettingsCodec())
			.apply(instance, BubbleBlock::new)
	);
	private final DyeColor color;

    // public static final BooleanProperty NORTH = Properties.NORTH;
	// public static final BooleanProperty EAST = Properties.EAST;
	// public static final BooleanProperty SOUTH = Properties.SOUTH;
	// public static final BooleanProperty WEST = Properties.WEST;
	// public static final BooleanProperty UP = Properties.UP;
	// public static final BooleanProperty DOWN = Properties.DOWN;

    @Override
    protected MapCodec<BubbleBlock> getCodec() {
        return CODEC;
    }
    
    public BubbleBlock(DyeColor color, Settings settings) {
        super(settings);
		this.color = color;
		// this.setDefaultState(this.stateManager.getDefaultState().with(NORTH, false).with(SOUTH, false).with(EAST, false).with(WEST, false).with(UP, false).with(DOWN, false));
    }

	@Override
	protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
		super.onStateReplaced(state, world, pos, moved);
		// TODO: particle
		world.spawnParticles(ParticleTypes.BUBBLE_POP,
			pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
			1, 0, 0, 0, 0);
	}

    // @Override
    // protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    //     builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    // }

    // @Override
    // public BlockState getPlacementState(ItemPlacementContext ctx) {
    //     return updateConnections(ctx.getWorld(), ctx.getBlockPos(), getDefaultState());
    // }

	// @Override
	// protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView,
	// 		BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
	// 	return updateConnections(world, pos, state);
	// }

    // private BlockState updateConnections(WorldView world, BlockPos pos, BlockState state) {
    //     boolean n = connects(world, pos.north());
    //     boolean s = connects(world, pos.south());
    //     boolean e = connects(world, pos.east());
    //     boolean w = connects(world, pos.west());
	// 	boolean u = connects(world, pos.up());
	// 	boolean d = connects(world, pos.down());

    //     int connectionsAllowed = 4;
    //     n = n && (connectionsAllowed-- > 0);
    //     s = s && (connectionsAllowed-- > 0);
    //     e = e && (connectionsAllowed-- > 0);
    //     w = w && (connectionsAllowed-- > 0);
	// 	u = u && (connectionsAllowed-- > 0);
	// 	d = d && (connectionsAllowed-- > 0);

    //     return state.with(NORTH, n).with(SOUTH, s).with(EAST, e).with(WEST, w).with(UP, u).with(DOWN, d);
    // }

    // private boolean connects(WorldView world, BlockPos other) {
	// 	BlockState state = world.getBlockState(other);
	// 	if (state.getBlock() instanceof BubbleBlock bubbleBlock && bubbleBlock.getColor() == this.getColor()) {
	// 		int connectionCount = 0;
	// 		if (state.get(BubbleBlock.NORTH)) connectionCount++;
	// 		if (state.get(BubbleBlock.SOUTH)) connectionCount++;
	// 		if (state.get(BubbleBlock.EAST)) connectionCount++;
	// 		if (state.get(BubbleBlock.WEST)) connectionCount++;
	// 		if (state.get(BubbleBlock.UP)) connectionCount++;
	// 		if (state.get(BubbleBlock.DOWN)) connectionCount++;
	// 		return connectionCount <= 4;
	// 	}
	// 	return false;
    // }

	@Override
	public DyeColor getColor() {
		return this.color;
	}
	
    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }
}

package net.nml.bubble.block;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import net.nml.bubble.BubbleEntity;

public class BubbleDispenserBlock extends BlockWithEntity {
	public static final MapCodec<BubbleDispenserBlock> CODEC = createCodec(BubbleDispenserBlock::new);
	public static final EnumProperty<Direction> FACING = FacingBlock.FACING;
	public static final BooleanProperty POWERED = Properties.POWERED;
	public static final IntProperty PROGRESS = Properties.AGE_25;

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}
	
	public BubbleDispenserBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(PROGRESS, 0));
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient) {
			// TODO: spam protection
			this.dispense(world, pos, state, state.get(PROGRESS), player.getRandom());
			world.setBlockState(pos, state.with(PROGRESS, 0));
		}

		return ActionResult.SUCCESS;
	}

	@Override
	protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
		boolean powered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
		boolean prevPowered = state.get(POWERED);
		if (powered && !prevPowered) {
			if (!world.getBlockTickScheduler().isTicking(pos, this)) {
				world.scheduleBlockTick(pos, this, 2);
			}
			world.setBlockState(pos, state.with(POWERED, true).with(PROGRESS, 1), Block.NOTIFY_LISTENERS);
		} else if (!powered && prevPowered) {
			world.setBlockState(pos, state.with(POWERED, false), Block.NOTIFY_LISTENERS);
		}
	}

	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.update(world, state, pos, random);
	}

	private void update(World world, BlockState state, BlockPos pos, Random random) {
		boolean powered = state.get(POWERED);
		int progress = state.get(PROGRESS);
		world.scheduleBlockTick(pos, this, 2);
		if (powered && progress < 25) {
			world.setBlockState(pos, state.with(PROGRESS, progress + 1));
			return;
		} else if (!powered && progress == 0) {
			return;
		}
		this.dispense(world, pos, state, progress, random);
		world.setBlockState(pos, state.with(PROGRESS, 0));
	}

	public void dispense(World world, BlockPos pos, BlockState state, int progress, Random random) {
		float size = 1f + progress / 6f;
		double speed = 0.3 + Math.abs(random.nextGaussian()) * 0.1;
		Direction direction = state.get(FACING);
		
		BubbleEntity bubble = new BubbleEntity(world, size);
		bubble.setPosition(pos.offset(direction, 1).toCenterPos());
		
		double x = (direction.getOffsetX() + random.nextGaussian() * 0.1) * 0.4;
		double y = (direction.getOffsetY() + random.nextGaussian() * 0.1) * 0.4;
		double z = (direction.getOffsetZ() + random.nextGaussian() * 0.1) * 0.4;
		
		bubble.setVelocity(x * speed, y * speed, z * speed);
		bubble.calculateDimensions();

		// TODO: bubble effects
		// if (BubbleWandEffectsComponent.getColor(stack).isPresent()) {
		// 	bubble.setCustomColor(BubbleWandEffectsComponent.getColor(stack).get());
		// } else if (BubbleWandEffectsComponent.getRainbow(stack)) {
		// 	bubble.setCustomColor(BubbleEntity.rainbowColor(bubble.getRandom()));
		// }
		
		world.spawnEntity(bubble);
		world.playSound(
			null,
			pos.getX(),
			pos.getY(),
			pos.getZ(),
			SoundEvents.ENTITY_ARROW_SHOOT,
			SoundCategory.BLOCKS,
			0.2F,
			1.0F
		);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BubbleDispenserBlockEntity(pos, state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
	}

	@Override
	protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
		ItemScatterer.onStateReplaced(state, world, pos);
	}

	@Override
	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, PROGRESS);
	}
}

package net.nml.bubble;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BubbleWandItem extends Item {
	public BubbleWandItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		int useTicks = this.getUseTicks(remainingUseTicks, stack, user);
		if (user instanceof PlayerEntity playerEntity) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!playerEntity.isInCreativeMode()) {
				playerEntity.getItemCooldownManager().set(stack, this.getCooldownTicks(useTicks));
			}
		}
		
		if (BlowingBubbles.getEnchantmentLevel(stack, ModRegistry.BUBBLE_BARRAGE_ENCHANTMENT_EFFECT) != -1) {
			this.usageTick(world, user, stack, remainingUseTicks);
		} else {
			if (world instanceof ServerWorld serverWorld) {
				this.shoot(user, serverWorld, stack, this.getSize(useTicks), 0.1);
			}
		}
		return true;
	}

	public void shoot(LivingEntity user, ServerWorld world, ItemStack stack, float size, double speed) {
		BubbleEntity bubble = new BubbleEntity(world, size);
		bubble.setPosition(user.getX(), user.getEyeY() - bubble.getHeight() / 2, user.getZ());
		
		float pitch = user.getPitch();
		float yaw = user.getYaw();

		double spread = MathHelper.clamp(speed * 20.0, 0.0, 10.0);
		if (spread > 0.0) {
			float randomYaw = (float)(world.getRandom().nextGaussian() * spread);
			float randomPitch = (float)(world.getRandom().nextGaussian() * spread);
			yaw += randomYaw;
			pitch += randomPitch;
		}
		
		double x = -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
		double y = -Math.sin(Math.toRadians(pitch));
		double z = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
		
		bubble.setVelocity(x * speed, y * speed, z * speed);
		bubble.setPosition(bubble.getPos().add(bubble.getVelocity().multiply(8)));
		bubble.calculateDimensions();
		
		world.spawnEntity(bubble);
		world.playSound(
			null,
			user.getX(),
			user.getY(),
			user.getZ(),
			SoundEvents.ENTITY_ARROW_SHOOT,
			SoundCategory.PLAYERS,
			1.0F,
			1.0F
		);
	}

	public float getMaxBlowableSize(ItemStack stack) {
		if (BlowingBubbles.getEnchantmentLevel(stack, ModRegistry.BUBBLE_BARRAGE_ENCHANTMENT_EFFECT) != -1) return 1.0f;
		return 4.0f;
	}

	public float getSizePerTick() {
		// TODO: enchantment
		return 20.0f;
	}

	public int getUseTicks(int useTicks, ItemStack stack) {
		return MathHelper.clamp(useTicks, 0, (int)((this.getMaxBlowableSize(stack) - 1) * this.getSizePerTick()));
	}

	public int getUseTicks(int remainingUseTicks, ItemStack stack, LivingEntity user) {
		return this.getUseTicks(this.getMaxUseTime(stack, user) - remainingUseTicks, stack);
	}

	public float getSize(int useTicks) {
		return 1f + useTicks / this.getSizePerTick();
	}

	public int getCooldownTicks(int useTicks) {
		return 10 + useTicks / 2;
	}
	
	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		user.setCurrentHand(hand);
		return ActionResult.CONSUME;
	}

	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return 72000;
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (BlowingBubbles.getEnchantmentLevel(stack, ModRegistry.BUBBLE_BARRAGE_ENCHANTMENT_EFFECT) == -1) return;
		if (remainingUseTicks % 2 != 0) return;

		if (world instanceof ServerWorld serverWorld) {
			this.shoot(user, serverWorld, stack, 1f, 0.2);
		}
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}
}

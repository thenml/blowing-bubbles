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
		if (!(user instanceof PlayerEntity playerEntity)) {
			return false;
		} else {
			int useTicks = this.getUseTicks(remainingUseTicks, stack, user);
			if (world instanceof ServerWorld serverWorld) {
				this.shoot(playerEntity, serverWorld, stack, useTicks);
			}

			world.playSound(
				null,
				playerEntity.getX(),
				playerEntity.getY(),
				playerEntity.getZ(),
				SoundEvents.ENTITY_ARROW_SHOOT,
				SoundCategory.PLAYERS,
				1.0F,
				1.0F
			);
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!user.isInCreativeMode()) {
				playerEntity.getItemCooldownManager().set(stack, this.getCooldownTicks(useTicks));
			}
			return true;
		}
	}

	public void shoot(LivingEntity user, ServerWorld world, ItemStack stack, int useTicks) {
		// Spawn a bubble at the player's position with 2.0 velocity in facing direction
		BubbleEntity bubble = new BubbleEntity(world, this.getSize(useTicks));
		bubble.setPosition(user.getX(), user.getEyeY() - bubble.getHeight() / 2, user.getZ());
		
		// Set velocity in the direction the player is looking
		float pitch = user.getPitch();
		float yaw = user.getYaw();
		
		double x = -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
		double y = -Math.sin(Math.toRadians(pitch));
		double z = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
		
		double speed = 0.1;
		bubble.setVelocity(x * speed, y * speed, z * speed);
		bubble.setPosition(bubble.getPos().add(bubble.getVelocity().multiply(8)));
		
		world.spawnEntity(bubble);
	}

	public float getMaxBlowableSize() {
		return 4.0f;
	}

	public float getSizePerTick() {
		// TODO: enchantment
		return 20.0f;
	}

	public int getUseTicks(int useTicks) {
		return MathHelper.clamp(useTicks, 0, (int)((this.getMaxBlowableSize() - 1) * this.getSizePerTick()));
	}

	public int getUseTicks(int remainingUseTicks, ItemStack stack, LivingEntity user) {
		return this.getUseTicks(this.getMaxUseTime(stack, user) - remainingUseTicks);
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
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}
}

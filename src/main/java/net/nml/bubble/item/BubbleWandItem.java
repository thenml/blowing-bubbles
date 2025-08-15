package net.nml.bubble.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.nml.bubble.BlowingBubbles;
import net.nml.bubble.BubbleEntity;
import net.nml.bubble.ModRegistry;

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
				stack.damage(1, playerEntity);
				playerEntity.getItemCooldownManager().set(stack, this.getCooldownTicks(useTicks));
			}
		}
		
		if (BlowingBubbles.getEnchantmentLevel(stack, ModRegistry.BUBBLE_BARRAGE_ENCHANTMENT_EFFECT) != -1) {
			this.usageTick(world, user, stack, remainingUseTicks);
		} else {
			if (world instanceof ServerWorld serverWorld) {
				this.shoot(user, serverWorld, stack, this.getSize(useTicks, stack), 0.1);
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
		
		// TODO: add same mechanic as arrows to not hit the shooter
		bubble.setVelocity(x * speed, y * speed, z * speed);
		bubble.setPosition(bubble.getPos().add(bubble.getVelocity().multiply(size * 6)));
		bubble.calculateDimensions();

		if (BubbleWandEffectsComponent.getColor(stack).isPresent()) {
			bubble.setCustomColor(BubbleWandEffectsComponent.getColor(stack).get());
		} else if (BubbleWandEffectsComponent.getRainbow(stack)) {
			bubble.setCustomColor(BubbleEntity.rainbowColor(bubble.getRandom()));
		}
		bubble.setEffects(user.getStatusEffects());
		bubble.setOwner(user);
		if (BlowingBubbles.getEnchantmentLevel(stack, ModRegistry.INFINITE_BUBBLE_ENCHANTMENT_EFFECT) != -1) {
			List<BubbleEntity> list = new ArrayList<>();
			world.collectEntitiesByType(TypeFilter.instanceOf(BubbleEntity.class), (e) -> e.getDuration() == 0 && e.getOwnerReference().uuidEquals(user), list, 1);
			if (!list.isEmpty()) list.getFirst().kill(world);
			bubble.setDuration(0);
		}
		
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
		int i = BlowingBubbles.getEnchantmentLevel(stack, ModRegistry.BIGGER_BUBBLES_ENCHANTMENT_EFFECT);
		if (i != -1) return 5.0f + i * 1.5f;
		return 5.0f;
	}

	public float getSizePerTick(ItemStack stack) {
		int i = BlowingBubbles.getEnchantmentLevel(stack, ModRegistry.BIGGER_BUBBLES_ENCHANTMENT_EFFECT);
		if (i != -1) return MathHelper.clamp(20.0f - i * 2f, 1f, 20f);
		return 20.0f;
	}

	public int getUseTicks(int useTicks, ItemStack stack) {
		return MathHelper.clamp(useTicks, 0, (int)((this.getMaxBlowableSize(stack) - 1) * this.getSizePerTick(stack)));
	}

	public int getUseTicks(int remainingUseTicks, ItemStack stack, LivingEntity user) {
		return this.getUseTicks(this.getMaxUseTime(stack, user) - remainingUseTicks, stack);
	}

	public float getSize(int useTicks, ItemStack stack) {
		return 1f + useTicks / this.getSizePerTick(stack);
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
			float s = 1f;
			int i = BlowingBubbles.getEnchantmentLevel(stack, ModRegistry.BIGGER_BUBBLES_ENCHANTMENT_EFFECT);
			if (i != -1) s += i * 0.5f;
			this.shoot(user, serverWorld, stack, s, 0.2);
		}
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent,
			Consumer<Text> textConsumer, TooltipType type) {
		super.appendTooltip(stack, context, displayComponent, textConsumer, type);
		// what was the point of this? ig im missing something
		stack.appendComponentTooltip(ModRegistry.BUBBLE_WAND_EFFECTS_COMPONENT, context, displayComponent, textConsumer, type);
	}
}

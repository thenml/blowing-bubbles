package net.nml.bubble;

import java.util.Collection;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Arm;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.nml.bubble.block.BubbleBlock;

public class BubbleEntity extends LivingEntity {
	private PotionContentsComponent potion = PotionContentsComponent.DEFAULT;
	private static final TrackedData<Integer> COLOR = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> CUSTOM_COLOR = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> TIME = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> DURATION = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Float> SIZE = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> OPACITY = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Boolean> HAS_ENTITY_ON_TOP = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	protected static final TrackedData<Optional<LazyEntityReference<LivingEntity>>> OWNER_UUID = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.LAZY_ENTITY_REFERENCE);
	
	public BubbleEntity(EntityType<? extends BubbleEntity> entityType, World world) {
		super(entityType, world);
		this.setDuration(randomDuration());
		this.setRotation(this.random.nextFloat() * 360.0F, 0);
		this.updateColor();
	}

	public BubbleEntity(World world, float size) {
		this(ModRegistry.BUBBLE, world);
		this.setSize(size);
		this.setDuration(randomDuration());
	}

	@Override
	public void tick() {
		if (this.isDead()) {
			if (!this.getWorld().isClient) {
				((ServerWorld) this.getWorld()).spawnParticles(new PopParticleEffect(this.getColor(), this.getSize()), 
				this.getX(), this.getY() + 0.25, this.getZ(),
				1, 0, 0, 0, 0);
			}
			this.remove(Entity.RemovalReason.KILLED);
			return;
		}
		
		super.tick();
		if (this.getWorld().isClient) return;
		this.setAir(0);

		int duration = this.getDuration();

		if (duration > 0) {
			int v = 0;
			if (!this.hasPassengers()) {
				v++;
				if (!this.hasEntityOnTop()) v++;
				if (this.horizontalCollision ||	this.verticalCollision) v++;
				this.setTime(this.getTime() + v);
			} else if (this.getRandom().nextBoolean()) {
				this.setTime(this.getTime() + 1);
			}

			if (this.getTime() >= duration) {
				this.setHealth(0);
				this.setOpacity(0);
			} else {
				this.setHealth((duration - this.getTime()) / 40f);
				this.setOpacity((duration - this.getTime()) / (float) duration);
			}
		}
	}
	
	@Override
	protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
		super.tickControlled(controllingPlayer, movementInput);
		this.setRotation(controllingPlayer.getYaw(), controllingPlayer.getPitch() * 0.5F);
		this.lastYaw = this.bodyYaw = this.headYaw = this.getYaw();
		controllingPlayer.setAir(controllingPlayer.getAir() + 2);
		this.applyEffects(controllingPlayer);
	}

	@Override
	protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
		if (this.isTouchingWater()) {
			float r = controllingPlayer.getPitch() * (float) (Math.PI / 180.0);
			return new Vec3d(
				controllingPlayer.sidewaysSpeed,
				-MathHelper.sin(r) * controllingPlayer.forwardSpeed + (controllingPlayer.isJumping() ? 0.5f : 0f),
				MathHelper.cos(r) * controllingPlayer.forwardSpeed
			);
		}
		return new Vec3d(controllingPlayer.sidewaysSpeed, 0, controllingPlayer.forwardSpeed);
	}

	@Nullable
	@Override
	public LivingEntity getControllingPassenger() {
		return this.getFirstPassenger() instanceof PlayerEntity player ? player : super.getControllingPassenger();
	}
	
	@Override
	public boolean isCollidable(@Nullable Entity entity) {
		boolean r = entity != null && this.canEntityInteract(entity) && this.canEntityStand(entity, (float)this.getVelocity().y);
		this.dataTracker.set(HAS_ENTITY_ON_TOP, r);
		return r;
	}

	@Override
	public boolean isPushable() {
		return true;
	}

	@Override
	public void pushAwayFrom(Entity entity) {
		if (!this.canEntityInteract(entity) || this.getWorld().isClient) return;
		if (this.canEntityStand(entity, entity.getStepHeight())) {
			// TODO 1.1
		} else if (this.canEntityEnter(entity)) {
			entity.startRiding(this, true);
		} else {
			// double v = MathHelper.clamp(Math.max(entity.getHeight(), entity.getWidth()) - this.getWidth(), 0.0, 1.0);
			// this.setTime(v == 1.0 ? this.getDuration() : this.getTime() + (int)(100 * v));
			this.setTime(this.getDuration());
			entity.setAir(entity.getMaxAir());
		}
	}

	@Override
	protected void pushAway(Entity entity) {
	}

	public boolean canEntityInteract(Entity entity) {
		return !entity.noClip && entity.isLiving() && !entity.hasVehicle() && !(entity instanceof BubbleEntity);
	}
	public boolean canEntityStand(Entity entity, float stepHeight) {
		return entity.getWidth() < this.getWidth() && entity.getY() + stepHeight >= this.getY() + this.getHeight();
	}
	public boolean canEntityEnter(Entity entity) {
		return Math.max(entity.getHeight(), entity.getWidth()) + 0.1 <= this.getWidth() && !this.hasPassengers();
	}

	public boolean hasEntityOnTop() {
		return this.dataTracker.get(HAS_ENTITY_ON_TOP);
	}

	@Override
	public Vec3d getPassengerRidingPos(Entity entity) {
		// Place the passenger in the vertical center of the bubble
		double bubbleCenterY = this.getPos().y + (this.getHeight());
		double passengerHalfHeight = entity.getHeight() / 2.0;
		return new Vec3d(this.getPos().x, bubbleCenterY - passengerHalfHeight, this.getPos().z);
	}

	@Override
	public boolean damage(ServerWorld world, DamageSource source, float amount) {
		if (this.isInvulnerableTo(world, source) || this.isDead()) {
			return false;
		}
		this.setTime(this.getDuration());
		if (this.getTime() <= 0) {
			this.setHealth(0);
		}
		return true;
	}
	
	@Override
	public boolean canHaveStatusEffect(StatusEffectInstance effect) {
		return false;
	}
	
	@Override
	public boolean addStatusEffect(StatusEffectInstance effect, Entity source) {
		return this.addEffect(effect);
	}

	public boolean addEffect(StatusEffectInstance effect) {
		StatusEffectInstance statusEffectInstance = null;
		for (StatusEffectInstance e : this.potion.getEffects()) {
			if (e.getEffectType().equals(effect.getEffectType())) {
				statusEffectInstance = e;
				break;
			}
		}
		if (statusEffectInstance != null) {
			if (!statusEffectInstance.upgrade(effect)) return false;

			this.setPotionContents(PotionContentsComponent.DEFAULT);
			for (StatusEffectInstance e : this.potion.getEffects()) {
				if (!e.getEffectType().equals(effect.getEffectType())) {
					this.setPotionContents(this.potion.with(e));
				}
			}
			return true;
		}
		this.setPotionContents(this.potion.with(effect));
		return true;
	}

	public void setEffects(Collection<StatusEffectInstance> collection) {
		for (StatusEffectInstance statusEffectInstance : collection) {
			this.setPotionContents(this.potion.with((new StatusEffectInstance(statusEffectInstance))));
		}
	}

	public void applyEffects(LivingEntity entity) {
		this.potion.apply(entity, this.getOpacity());
	}

	public void setPotionContents(PotionContentsComponent potionContentsComponent) {
		this.potion = potionContentsComponent;
		this.updateColor();
	}

	public void updateColor() {
		int cc = this.dataTracker.get(CUSTOM_COLOR);
		if (cc == 16) {
			this.setColor(this.potion.getColor(this.getCustomColor()));
		} else {
			this.setColor(ColorHelper.mix(this.getCustomColor(), this.potion.getColor(-1)));
		}
	}

	public BubbleBlock getBubbleBlock() {
		float maxDistance = Float.MAX_VALUE;
		BubbleBlock block = null;

		Vector3f colorVector = ColorHelper.toVector(this.getColor());
		for (Pair<BubbleBlock, String> pair : ModRegistry.BUBBLE_BLOCKS) {
			int blockColor = pair.getLeft().getColor().getEntityColor();
			float distance = ColorHelper.toVector(blockColor).distance(colorVector);
			if (distance < maxDistance) {
				maxDistance = distance;
				block = pair.getLeft();
			}
		};
		return block;
	}

	private int randomDuration() {
		return 40 * (7 + this.random.nextInt(6)) + (int)((this.getSize() - 1f) * 20);
	}

	public void setDuration(int duration) {
		this.dataTracker.set(DURATION, duration);
	}

	public int getTime() {
		return this.dataTracker.get(TIME);
	}

	public void setTime(int time) {
		this.dataTracker.set(TIME, time);
	}					

	public int getDuration() {
		return this.dataTracker.get(DURATION);
	}

	public void setSize(float size) {
		this.dataTracker.set(SIZE, size);
	}

	public float getSize() {
		return this.dataTracker.get(SIZE);
	}

	private void setColor(int color) {
		this.dataTracker.set(COLOR, color);
	}

	public int getColor() {
		return this.dataTracker.get(COLOR);
	}

	public void setCustomColor(int color) {
		this.dataTracker.set(CUSTOM_COLOR, color);
		this.updateColor();
	}

	public static int getDefaultColor() {
		return ColorHelper.getArgb(80, 200, 255);
	}

	public int getCustomColor() {
		int cc = this.dataTracker.get(CUSTOM_COLOR);
		return cc == 16 ? getDefaultColor() : cc;
	}

	public void setOpacity(float opacity) {
		this.dataTracker.set(OPACITY, MathHelper.clamp(opacity, 0.2f, 1.0f));
	}

	// aka percentage to pop, with min at 0.2f
	public float getOpacity() {
		return this.dataTracker.get(OPACITY);
	}
	
	public void setOwner(@Nullable LivingEntity owner) {
		this.dataTracker.set(OWNER_UUID, Optional.ofNullable(owner).map(LazyEntityReference::new));
	}

	@Nullable
	public LazyEntityReference<LivingEntity> getOwnerReference() {
		return (LazyEntityReference<LivingEntity>)this.dataTracker.get(OWNER_UUID).orElse(null);
	}
	
	@Override
	public EntityDimensions getBaseDimensions(EntityPose pose) {
		return super.getBaseDimensions(pose).scaled(this.getSize());
	}

	@Override
	protected double getGravity() {
		if (this.hasPassengers() && this.isTouchingWater()) return 0.0;
		double g = -0.005;
		if (this.isTouchingWater()) g -= 0.005;
		if (this.hasEntityOnTop() || this.hasPassengers()) g += 0.01;
		if (this.isAtCloudHeight()) g += 0.008;
		g += (this.getSize() - 2f) * 0.0025;
		return MathHelper.clamp(g, -0.1, 0.1);
	}
	
	@Override
	public void travel(Vec3d movementInput) {
		double drag = this.isTouchingWater() ? 0.9 : 0.98;
		double dragX = this.hasEntityOnTop() ? 0.7 : this.hasPassengers() ? drag * 0.95 : drag;
		this.updateVelocity(0.02f, movementInput);
		this.setVelocity(this.getVelocity().add(0, -getGravity(), 0).multiply(dragX, drag, dragX));
		this.move(MovementType.SELF, getVelocity());
	}

	@Override
	public boolean shouldSwimInFluids() {
		return false;
	}

	@Override
	protected float getVelocityMultiplier() {
		return 1f; // modified in travel()
	}

	@Override
	protected void onBlockCollision(BlockState state) {
		super.onBlockCollision(state);
		if (!this.getWorld().isClient && state.isOf(Blocks.HONEY_BLOCK)) {
			this.setTime(this.getTime() + 2);
			this.setVelocity(this.getVelocity().multiply(0.5));
			if (this.getTime() >= this.getDuration() - 10) {
				this.getWorld().spawnEntity(new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), new ItemStack(this.getBubbleBlock())));
				this.remove(Entity.RemovalReason.KILLED);
			}
		}
	}
	
	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
	}

	@Override
	protected Entity.MoveEffect getMoveEffect() {
		return Entity.MoveEffect.NONE;
	}

	@Override
	public boolean hasNoDrag() {
		return true;
	}

	public static DefaultAttributeContainer.Builder createLivingAttributes() {
		return LivingEntity.createLivingAttributes();
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(HAS_ENTITY_ON_TOP, false);
		builder.add(OPACITY, 1.0f);
		builder.add(SIZE, 1.0f);
		builder.add(TIME, 0);
		builder.add(DURATION, 0);
		builder.add(COLOR, 0);
		builder.add(CUSTOM_COLOR, 16);
		builder.add(OWNER_UUID, Optional.empty());
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (SIZE.equals(data)) {
			this.calculateDimensions();
		}

		super.onTrackedDataSet(data);
	}

	@Override
	protected void writeCustomData(WriteView view) {
		super.writeCustomData(view);
		view.putInt("pop_time", this.getTime());
		view.putInt("Duration", this.getDuration());
		view.putFloat("Size", this.getSize());
		if (this.dataTracker.get(CUSTOM_COLOR) != 16) {
			view.putInt("CustomColor", this.getCustomColor());
		}
		if (!this.potion.equals(PotionContentsComponent.DEFAULT)) {
			view.put("potion_contents", PotionContentsComponent.CODEC, this.potion);
		}

		LazyEntityReference.writeData(this.getOwnerReference(), view, "Owner");
	}

	@Override
	protected void readCustomData(ReadView view) {
		super.readCustomData(view);
		this.setTime(view.getInt("pop_time", 0));
		this.dataTracker.set(SIZE, view.getFloat("Size", 1));

		Optional<Integer> duration = view.getOptionalInt("Duration");
		if (duration.isPresent()) this.setDuration(duration.get());

		Optional<Integer> color = view.getOptionalInt("CustomColor");
		if (color.isPresent()) {
			if (color.get() == 16) this.setCustomColor(rainbowColor(this.random));
			else this.setCustomColor(color.get());
		}

		LazyEntityReference<LivingEntity> lazyEntityReference = LazyEntityReference.fromDataOrPlayerName(view, "Owner", this.getWorld());
		if (lazyEntityReference != null) {
			try {
				this.dataTracker.set(OWNER_UUID, Optional.of(lazyEntityReference));
			} catch (Throwable t) {}
		} else {
			this.dataTracker.set(OWNER_UUID, Optional.empty());
		}

		this.setPotionContents((PotionContentsComponent)view.read("potion_contents", PotionContentsComponent.CODEC).orElse(PotionContentsComponent.DEFAULT));
	}

	public static int rainbowColor(Random random) {
        float hue = random.nextFloat();
        float saturation = 0.4f + random.nextFloat() * 0.3f;
        float brightness = 0.7f + random.nextFloat() * 0.3f;
        return MathHelper.hsvToRgb(hue, saturation, brightness);
    }
	public static int rainbowColor(float t) {
		float hue = Math.abs(t % 3600) / 3600;
        float saturation = 0.55f;
        float brightness = 0.85f;
        return MathHelper.hsvToRgb(hue, saturation, brightness);
    }

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}
}

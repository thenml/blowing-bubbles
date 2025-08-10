package net.nml.bubble;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BubbleEntity extends LivingEntity {
	private static final TrackedData<Integer> COLOR = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> DURATION = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Float> SIZE = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> OPACITY = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Boolean> HAS_ENTITY_ON_TOP = DataTracker.registerData(BubbleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public BubbleEntity(EntityType<? extends BubbleEntity> entityType, World world) {
		super(entityType, world);
		this.setDuration(randomDuration());
		this.setRotation(this.random.nextFloat() * 360.0F, 0);
	}

	public BubbleEntity(World world, float size) {
		this(ModRegistry.BUBBLE, world);
		this.setSize(size);
	}

	@Override
	public void tick() {
		if (this.isDead()) {
			if (!this.getWorld().isClient) {
				((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.BUBBLE_POP,
						this.getX(), this.getY() + 0.25, this.getZ(),
						1, 0, 0, 0, 0);
			}
			this.remove(Entity.RemovalReason.KILLED);
			return;
		}

		if (!this.hasPassengers()) {
			this.calculateEntityInteractions();
		}
		super.tick();
		this.setAir(0);

		int duration = this.getDuration();

		if (duration > 0) {
			if (!this.hasPassengers()) {
				this.age++;
				if (!this.hasEntityOnTop()) this.age++;
				if (this.horizontalCollision ||	this.verticalCollision) this.age++;
			}

			if (this.age >= duration) {
				this.setHealth(0);
				this.setOpacity(0);
			} else {
				this.setHealth((duration - this.age) / 40f);
				this.setOpacity((duration - this.age) / (float) duration);
			}
		}
	}
	
	@Override
	protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
		super.tickControlled(controllingPlayer, movementInput);
		this.setRotation(controllingPlayer.getYaw(), controllingPlayer.getPitch() * 0.5F);
		this.lastYaw = this.bodyYaw = this.headYaw = this.getYaw();
		controllingPlayer.setAir(controllingPlayer.getAir() + 2);
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
		return entity != null && entity.isLiving() && this.isAlive() && entity.getPos().y >= this.getBoundingBox().maxY && entity.getWidth() < this.getWidth();
	}

	@Override
	public boolean isPushable() {
		return true;
	}

	@Override
	public void pushAwayFrom(Entity entity) {
	}

	@Override
	protected void pushAway(Entity entity) {
	}

	public boolean canEntityInteract(Entity entity) {
		return !entity.noClip && entity.isLiving() && !entity.hasVehicle() && !(entity instanceof BubbleEntity);
	}
	public boolean canEntityStand(Entity entity, Box box) {
		return entity.getWidth() < this.getWidth() && box.expand(entity.getWidth(), entity.getStepHeight(), entity.getWidth()).contains(entity.getPos());
	}
	public boolean canEntityEnter(Entity entity) {
		return Math.max(entity.getHeight(), entity.getWidth()) + 0.1 <= this.getWidth() && !this.hasPassengers();
	}

	public boolean hasEntityOnTop() {
		return this.dataTracker.get(HAS_ENTITY_ON_TOP);
	}

	private void calculateEntityInteractions() {
		this.dataTracker.set(HAS_ENTITY_ON_TOP, false);
		
		Box box = this.getBoundingBox();
		Box topBox = new Box(box.minX, box.maxY - 0.1, box.minZ, box.maxX, box.maxY + this.getVelocity().y, box.maxZ);
		for (Entity entity : this.getWorld().getOtherEntities(this, box, (entityx -> this.canEntityInteract(entityx)))) {
			if (this.canEntityStand(entity, topBox)) {
				double offset = box.maxY - entity.getY();
				Vec3d velocity = new Vec3d(0, offset, 0);
				entity.move(MovementType.SELF, velocity);
				entity.setOnGround(true);
				this.dataTracker.set(HAS_ENTITY_ON_TOP, true);
			} else if (this.canEntityEnter(entity)) {
				entity.startRiding(this);
			} else {
				double v = MathHelper.clamp(Math.max(entity.getHeight(), entity.getWidth()) - this.getWidth(), 0.0, 1.0);
				this.age = v == 1.0 ? this.getDuration() : this.age + (int)(100 * v);
			}
		}
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
		this.age = this.getDuration();
		if (this.age <= 0) {
			this.setHealth(0);
		}
		return true;
	}

	private int randomDuration() {
		return 40 * (7 + this.random.nextInt(6));
	}

	public void setDuration(int duration) {
		this.dataTracker.set(DURATION, duration);
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

	public void setColor(int color) {
		this.dataTracker.set(COLOR, color);
	}

	public int getColor() {
		return this.dataTracker.get(COLOR);
	}

	public void setOpacity(float opacity) {
		this.dataTracker.set(OPACITY, MathHelper.clamp(opacity, 0.2f, 1.0f));
	}

	public float getOpacity() {
		return this.dataTracker.get(OPACITY);
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
		builder.add(DURATION, 0);
		builder.add(COLOR, ColorHelper.getArgb(34, 181, 255));
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
		view.putInt("Age", this.age);
		view.putInt("Duration", this.getDuration());
		view.putFloat("Size", this.getSize());
		view.putInt("Color", this.getColor());
	}

	@Override
	protected void readCustomData(ReadView view) {
		super.readCustomData(view);
		this.age = view.getInt("Age", 0);
		this.dataTracker.set(SIZE, view.getFloat("Size", 1));

		int duration = view.getInt("Duration", -1);
		if (duration >= 0) this.setDuration(duration);

		int color = view.getInt("Color", -1);
		if (color >= 0) this.setColor(color);
		else if (color == -2) this.setColor(randomColor(this.random));
	}

	public static int randomColor(Random random) {
        float hue = random.nextFloat();
        float saturation = 0.4f + random.nextFloat() * 0.3f;
        float brightness = 0.7f + random.nextFloat() * 0.3f;
        return MathHelper.hsvToRgb(hue, saturation, brightness);
    }

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}
}

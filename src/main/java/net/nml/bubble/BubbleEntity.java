package net.nml.bubble;

import java.util.List;

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
	private int age = 0;

	public BubbleEntity(EntityType<? extends BubbleEntity> entityType, World world) {
		super(entityType, world);
		this.setDuration(randomDuration());
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
		if (!this.getWorld().isClient) {
			this.syncEntityOnTop();
		}
		
		this.moveEntities();
		super.tick();

		int duration = this.getDuration();

		if (duration > 0) {
			this.age++;
			if (!this.hasEntityOnTop()) this.age++;
			if (this.horizontalCollision ||	this.verticalCollision) this.age++;

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
	public boolean isCollidable(@Nullable Entity entity) {
		return entity.isLiving() && this.isAlive() && entity.getPos().y >= this.getBoundingBox().maxY;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public void pushAwayFrom(Entity entity) {
	}

	public boolean canEntityStand(Entity entity) {
		return !entity.noClip && entity.isLiving() && !entity.isConnectedThroughVehicle(this) && !(entity instanceof BubbleEntity);
	}
	public boolean canEntityStand(Entity entity, Box box) {
		return this.canEntityStand(entity) && box.expand(entity.getWidth(), 0, entity.getWidth()).contains(entity.getPos());
	}

	public boolean hasEntityOnTop() {
		return this.dataTracker.get(HAS_ENTITY_ON_TOP);
	}
	
	private void syncEntityOnTop() {
		Box bbox = this.getBoundingBox();
		Box box = new Box(bbox.minX, bbox.maxY - 1.0e-5f, bbox.minZ, bbox.maxX, bbox.maxY + 1, bbox.maxZ);

		List<Entity> list = this.getWorld().getOtherEntities(this, box,	(entityx -> canEntityStand(entityx)));
		this.dataTracker.set(HAS_ENTITY_ON_TOP, !list.isEmpty());
	}

	private void moveEntities() {
		Box bbox = this.getBoundingBox();
		Box box = new Box(bbox.minX, bbox.maxY - 0.5, bbox.minZ, bbox.maxX, bbox.maxY + this.getVelocity().y, bbox.maxZ);

		for (Entity entity : this.getWorld().getOtherEntities(this, box, (entityx -> canEntityStand(entityx, box)))) {
			double offset = bbox.maxY - entity.getY();
			Vec3d velocity = new Vec3d(0, offset, 0);
			entity.move(MovementType.SELF, velocity);
			entity.setOnGround(true);
		}
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
		double g = -0.005;
		if (this.isTouchingWater()) g -= 0.005;
		if (this.hasEntityOnTop()) g += 0.01;
		if (this.isAtCloudHeight()) g += 0.008;
		g += (this.getSize() - 2f) * 0.0025;
		return MathHelper.clamp(g, -0.1, 0.1);
	}
	
	@Override
	public void travel(Vec3d movementInput) {
		double drag = this.isTouchingWater() ? 0.9 : 0.98;
		double dragX = this.hasEntityOnTop() ? 0.7 : drag;
		this.setVelocity(this.getVelocity().add(0, -getGravity(), 0 ).multiply(dragX, drag, dragX));
		move(MovementType.SELF, getVelocity());
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

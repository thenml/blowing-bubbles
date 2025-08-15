package net.nml.bubble;

import org.joml.Vector3f;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class PopParticle extends SpriteBillboardParticle {
	private final SpriteProvider spriteProvider;

	PopParticle(ClientWorld clientWorld, double x, double y, double z, PopParticleEffect parameters, SpriteProvider spriteProvider) {
		super(clientWorld, x, y, z);
		this.spriteProvider = spriteProvider;
		this.scale = (parameters.getScale() - 0.5f) * (this.random.nextFloat() * 0.4F + 0.8F) * 0.5f;
		this.maxAge = 3;
		this.setSpriteForAge(spriteProvider);
		Vector3f color = parameters.getColor();
		this.red = color.x();
		this.green = color.y();
		this.blue = color.z();
	}

	@Override
	public void tick() {
		this.lastX = this.x;
		this.lastY = this.y;
		this.lastZ = this.z;
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.setSpriteForAge(this.spriteProvider);
		}
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	public static class Factory implements ParticleFactory<PopParticleEffect> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(PopParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new PopParticle(world, x, y, z, parameters, this.spriteProvider);
		}
	}
	
}

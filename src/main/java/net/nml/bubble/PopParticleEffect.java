package net.nml.bubble;

import org.joml.Vector3f;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;

public class PopParticleEffect implements ParticleEffect {
	protected static final Codec<Float> SCALE_CODEC = Codec.FLOAT;
	public static final PopParticleEffect DEFAULT = new PopParticleEffect(16711680, 1.0F);
	public static final MapCodec<PopParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				Codecs.RGB.fieldOf("color").forGetter(p -> p.color),
				SCALE_CODEC.fieldOf("scale").forGetter(PopParticleEffect::getScale)
			)
			.apply(instance, PopParticleEffect::new)
	);
	public static final PacketCodec<RegistryByteBuf, PopParticleEffect> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.INTEGER, p -> p.color,
		PacketCodecs.FLOAT, PopParticleEffect::getScale,
		PopParticleEffect::new
	);

	private final float scale;
	private final int color;

	public PopParticleEffect(int color, float scale) {
		this.color = color;
		this.scale = scale;
	}

	@Override
	public ParticleType<?> getType() {
		return ModRegistry.POP_PARTICLE;
	}

	public Vector3f getColor() {
		return ColorHelper.toVector(this.color);
	}

	public float getScale() {
		return this.scale;
	}	
}

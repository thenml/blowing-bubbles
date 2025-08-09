package net.nml.bubble.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.util.math.random.Random;

public record BarrageBubblesEnchantmentEffect(EnchantmentLevelBasedValue amount) implements EnchantmentValueEffect {
	public static final MapCodec<BarrageBubblesEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
				EnchantmentLevelBasedValue.CODEC.fieldOf("amount").forGetter(BarrageBubblesEnchantmentEffect::amount)
			).apply(instance, BarrageBubblesEnchantmentEffect::new)
	);

	@Override
	public MapCodec<? extends EnchantmentValueEffect> getCodec() {
		return CODEC;
	}

	@Override
	public float apply(int level, Random random, float inputValue) {
		return inputValue;
	}
}
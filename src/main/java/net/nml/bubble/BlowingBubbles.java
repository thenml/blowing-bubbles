package net.nml.bubble;

import net.fabricmc.api.ModInitializer;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.datafixers.util.Pair;

public class BlowingBubbles implements ModInitializer {
	public static final String MOD_ID = "blowing-bubbles";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static <T> int getEnchantmentLevel(ItemStack stack, ComponentType<T> enchantment) {
		Pair<T, Integer> pair = EnchantmentHelper.getHighestLevelEffect(stack, enchantment);
		if (pair != null) return pair.getSecond();
		return -1;
	}

	@Override
	public void onInitialize() {
		ModRegistry.initialize();
	}
}
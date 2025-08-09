package net.nml.bubble;

import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;

public class ModRegistry {
	public static final EntityType<BubbleEntity> BUBBLE = entity("bubble", EntityType.Builder.<BubbleEntity>create(BubbleEntity::new, SpawnGroup.CREATURE).dimensions(0.4f, 0.4f));
	public static final Item BUBBLE_WAND = item("bubble_wand", BubbleWandItem::new, new Item.Settings());
	public static final RegistryKey<Enchantment> BUBBLE_BARRAGE_ENCHANTMENT = enchantment("bubble_barrage");
	public static ComponentType<Unit> BUBBLE_BARRAGE_ENCHANTMENT_EFFECT = enchantmentEffect("bubble_barrage", Unit.CODEC);

	private static <T extends Entity> EntityType<T> entity(String name, EntityType.Builder<T> builder) {
		return Registry.register(
			Registries.ENTITY_TYPE,
			Identifier.of(BlowingBubbles.MOD_ID, name),
			builder.build(RegistryKey.of(Registries.ENTITY_TYPE.getKey(), Identifier.of(BlowingBubbles.MOD_ID, name)))
		);
	}
	private static Item item(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return Items.register(
			RegistryKey.of(RegistryKeys.ITEM,
			Identifier.of(BlowingBubbles.MOD_ID, name)),
			factory,
			settings);
	}
	private static RegistryKey<Enchantment> enchantment(String name) {
		return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(BlowingBubbles.MOD_ID, name));
	}
	private static <T> ComponentType<T> enchantmentEffect(String name, Codec<T> codec) {
		// TODO: i know this is incorrect but i dont know what is correct
		return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Identifier.of(BlowingBubbles.MOD_ID, name), ComponentType.<T>builder().codec(codec).build());
	}

	public static void initialize() {
		FabricDefaultAttributeRegistry.register(BUBBLE, BubbleEntity.createLivingAttributes());
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(BUBBLE_WAND);
        });
	}
}

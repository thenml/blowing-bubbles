package net.nml.bubble;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
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

public class ModRegistry {
	public static final EntityType<BubbleEntity> BUBBLE = Registry.register(Registries.ENTITY_TYPE,
		Identifier.of(BlowingBubbles.MOD_ID, "bubble"),
		EntityType.Builder.<BubbleEntity>create(BubbleEntity::new, SpawnGroup.CREATURE).dimensions(0.4f, 0.4f)
		.build(RegistryKey.of(Registries.ENTITY_TYPE.getKey(), Identifier.of(BlowingBubbles.MOD_ID, "bubble"))));

	public static final Item BUBBLE_WAND = Items.register(
		RegistryKey.of(RegistryKeys.ITEM,
		Identifier.of(BlowingBubbles.MOD_ID, "bubble_wand")),
		BubbleWandItem::new, new Item.Settings());

	public static void initialize() {
		FabricDefaultAttributeRegistry.register(BUBBLE, BubbleEntity.createLivingAttributes());
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(BUBBLE_WAND);
        });
	}
}

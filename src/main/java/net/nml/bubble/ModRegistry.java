package net.nml.bubble;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.Unit;
import net.nml.bubble.block.BubbleBlock;
import net.nml.bubble.block.BubbleDispenserBlock;
import net.nml.bubble.block.BubbleDispenserBlockEntity;

public class ModRegistry {
	public static final EntityType<BubbleEntity> BUBBLE = entity("bubble",
		EntityType.Builder.<BubbleEntity>create(BubbleEntity::new, SpawnGroup.CREATURE)
			.dimensions(0.4f, 0.4f));

	public static final Item BUBBLE_WAND = item("bubble_wand", BubbleWandItem::new,
		new Item.Settings()
			.maxCount(1)
			.maxDamage(250)
			.repairable(TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "ingots/copper"))));

	public static final List<Pair<BubbleBlock, String>> BUBBLE_BLOCKS = new ArrayList<>();
	public static final Block WHITE_BUBBLE_BLOCK = bubbleBlock("white_bubble_block", DyeColor.WHITE);
	public static final Block LIGHT_GRAY_BUBBLE_BLOCK = bubbleBlock("light_gray_bubble_block", DyeColor.LIGHT_GRAY);
	public static final Block GRAY_BUBBLE_BLOCK = bubbleBlock("gray_bubble_block", DyeColor.GRAY);
	public static final Block BLACK_BUBBLE_BLOCK = bubbleBlock("black_bubble_block", DyeColor.BLACK);
	public static final Block BROWN_BUBBLE_BLOCK = bubbleBlock("brown_bubble_block", DyeColor.BROWN);
	public static final Block RED_BUBBLE_BLOCK = bubbleBlock("red_bubble_block", DyeColor.RED);
	public static final Block ORANGE_BUBBLE_BLOCK = bubbleBlock("orange_bubble_block", DyeColor.ORANGE);
	public static final Block YELLOW_BUBBLE_BLOCK = bubbleBlock("yellow_bubble_block", DyeColor.YELLOW);
	public static final Block LIME_BUBBLE_BLOCK = bubbleBlock("lime_bubble_block", DyeColor.LIME);
	public static final Block GREEN_BUBBLE_BLOCK = bubbleBlock("green_bubble_block", DyeColor.GREEN);
	public static final Block CYAN_BUBBLE_BLOCK = bubbleBlock("cyan_bubble_block", DyeColor.CYAN);
	public static final Block LIGHT_BLUE_BUBBLE_BLOCK = bubbleBlock("light_blue_bubble_block", DyeColor.LIGHT_BLUE);
	public static final Block BLUE_BUBBLE_BLOCK = bubbleBlock("blue_bubble_block", DyeColor.BLUE);
	public static final Block PURPLE_BUBBLE_BLOCK = bubbleBlock("purple_bubble_block", DyeColor.PURPLE);
	public static final Block MAGENTA_BUBBLE_BLOCK = bubbleBlock("magenta_bubble_block", DyeColor.MAGENTA);
	public static final Block PINK_BUBBLE_BLOCK = bubbleBlock("pink_bubble_block", DyeColor.PINK);

	public static final Block BUBBLE_DISPENSER = block("bubble_dispenser", BubbleDispenserBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(MapColor.STONE_GRAY)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool()
			.strength(3.5f),
			true);

	public static final BlockEntityType<BubbleDispenserBlockEntity> BUBBLE_DISPENSER_BLOCK_ENTITY = blockEntityType("bubble_dispenser", FabricBlockEntityTypeBuilder.<BubbleDispenserBlockEntity>create(BubbleDispenserBlockEntity::new, BUBBLE_DISPENSER).build());

	public static final RegistryKey<Enchantment> BUBBLE_BARRAGE_ENCHANTMENT = enchantment("bubble_barrage");
	public static ComponentType<Unit> BUBBLE_BARRAGE_ENCHANTMENT_EFFECT = enchantmentEffect("bubble_barrage", Unit.CODEC);

	public static final RecipeSerializer<BubbleWandEffectsRecipe> BUBBLE_WAND_EFFECTS_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, 
		Identifier.of(BlowingBubbles.MOD_ID, "special_bubble_wand_effects"), new SpecialCraftingRecipe.SpecialRecipeSerializer<>(BubbleWandEffectsRecipe::new));
	public static final ComponentType<BubbleWandEffectsComponent> BUBBLE_WAND_EFFECTS_COMPONENT = Registry.register(
		Registries.DATA_COMPONENT_TYPE,
		Identifier.of(BlowingBubbles.MOD_ID, "bubble_wand_effects"),
		ComponentType.<BubbleWandEffectsComponent>builder().codec(BubbleWandEffectsComponent.CODEC).packetCodec(BubbleWandEffectsComponent.PACKET_CODEC).build()
	);


	private static <T> RegistryKey<T> key(Registry<T> registry, String name) {
		return RegistryKey.of(registry.getKey(), Identifier.of(BlowingBubbles.MOD_ID, name));
	}

	private static <T extends Entity> EntityType<T> entity(String name, EntityType.Builder<T> builder) {
		return Registry.register(
			Registries.ENTITY_TYPE,
			Identifier.of(BlowingBubbles.MOD_ID, name),
			builder.build(key(Registries.ENTITY_TYPE, name))
		);
	}

	private static Item item(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return Items.register(key(Registries.ITEM, name), factory, settings);
	}

	private static Block block(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean hasItem) {
		RegistryKey<Block> blockKey = key(Registries.BLOCK, name);
		Block block = blockFactory.apply(settings.registryKey(blockKey));

		if (hasItem) {
			RegistryKey<Item> itemKey = key(Registries.ITEM, name);
			BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
			Registry.register(Registries.ITEM, itemKey, blockItem);
		}

		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	private static Block bubbleBlock(String name, DyeColor color) {
		Block block = block(name, settings -> new BubbleBlock(color, settings),
			AbstractBlock.Settings.create()
				.breakInstantly() // TODO: temp, it crashes otherwise
				.sounds(BlockSoundGroup.SLIME) // TODO: temp
				.instrument(NoteBlockInstrument.HAT)
				.pistonBehavior(PistonBehavior.DESTROY)
				.solid()
				.nonOpaque()
				.noBlockBreakParticles()
				.allowsSpawning(Blocks::never)
				.suffocates(Blocks::never)
				.blockVision(Blocks::never),
				true);
		if (block instanceof BubbleBlock bubbleBlock) {
			BUBBLE_BLOCKS.add(new Pair<BubbleBlock, String>(bubbleBlock, name));
		}

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(content -> content.add(block.asItem()));
		return block;
	}

	private static <T extends BlockEntityType<?>> T blockEntityType(String path, T blockEntityType) {
	  return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(BlowingBubbles.MOD_ID, path), blockEntityType);
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
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> content.add(BUBBLE_WAND));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(BUBBLE_DISPENSER.asItem()));
	}
}

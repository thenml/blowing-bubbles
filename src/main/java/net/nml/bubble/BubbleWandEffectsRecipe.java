package net.nml.bubble;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class BubbleWandEffectsRecipe extends SpecialCraftingRecipe {
	private static final Ingredient BUBBLE_WAND = Ingredient.ofItem(ModRegistry.BUBBLE_WAND);
	private static final Ingredient DIAMOND = Ingredient.ofItem(Items.DIAMOND);
	
	public BubbleWandEffectsRecipe(CraftingRecipeCategory craftingRecipeCategory) {
		super(craftingRecipeCategory);
	}

	public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
		if (craftingRecipeInput.getStackCount() < 2) return false;
			
		boolean wand = false;
		boolean dye = false;
		boolean diamond = false;

		for (int i = 0; i < craftingRecipeInput.size(); i++) {
			ItemStack itemStack = craftingRecipeInput.getStackInSlot(i);
			if (!itemStack.isEmpty()) {
				if (BUBBLE_WAND.test(itemStack)) {
					if (wand) return false;
					wand = true;
				} else if (DIAMOND.test(itemStack)) {
					if (dye || diamond) return false;
					diamond = true;
				} else if (itemStack.getItem() instanceof DyeItem) {
					if (diamond) return false;
					dye = true;
				} else {
					return false;
				}
			}
		}

		return (dye || diamond) && wand;
	}

	public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
		List<DyeItem> dyes = new ArrayList<DyeItem>();
		ItemStack wandStack = ItemStack.EMPTY;
		ItemStack diamondStack = ItemStack.EMPTY;

		for (int i = 0; i < craftingRecipeInput.size(); i++) {
			ItemStack stack = craftingRecipeInput.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (BUBBLE_WAND.test(stack)) {
					if (!wandStack.isEmpty()) return ItemStack.EMPTY;
					wandStack = stack.copy();
				} else if (DIAMOND.test(stack)) {
					if (!dyes.isEmpty() || !diamondStack.isEmpty()) return ItemStack.EMPTY;
					diamondStack = stack.copy();
				} else if (stack.getItem() instanceof DyeItem dyeItem) {
					if (!diamondStack.isEmpty()) return ItemStack.EMPTY;
					dyes.add(dyeItem);
				} else {
					return ItemStack.EMPTY;
				}
			}
		}
		if (dyes.isEmpty() && diamondStack.isEmpty()) return ItemStack.EMPTY;

		if (diamondStack.isEmpty()) {
			ItemStack outputStack = BubbleWandEffectsComponent.setColor(wandStack, dyes);
			return outputStack;
		} else {
			ItemStack outputStack = BubbleWandEffectsComponent.setRainbow(wandStack);
			return outputStack;
		}
	}

	@Override
	public RecipeSerializer<BubbleWandEffectsRecipe> getSerializer() {
		return ModRegistry.BUBBLE_WAND_EFFECTS_RECIPE_SERIALIZER;
	}	
}

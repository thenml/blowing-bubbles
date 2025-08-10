package net.nml.bubble;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;

public record BubbleWandEffectsComponent(Optional<Integer> color, Optional<Boolean> rainbow) implements TooltipAppender {
	public static final Codec<BubbleWandEffectsComponent> CODEC = RecordCodecBuilder.create(builder -> {
		return builder.group(
			Codec.INT.lenientOptionalFieldOf("color").forGetter(BubbleWandEffectsComponent::color),
			Codec.BOOL.lenientOptionalFieldOf("rainbow").forGetter(BubbleWandEffectsComponent::rainbow)
		).apply(builder, BubbleWandEffectsComponent::new);
	});
	public static final PacketCodec<RegistryByteBuf, BubbleWandEffectsComponent> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.optional(PacketCodecs.INTEGER), BubbleWandEffectsComponent::color,
		PacketCodecs.optional(PacketCodecs.BOOLEAN), BubbleWandEffectsComponent::rainbow,
		BubbleWandEffectsComponent::new
	);

	public static Optional<Integer> getColor(ItemStack stack) {
		BubbleWandEffectsComponent bubbleWandEffectsComponent = stack.get(ModRegistry.BUBBLE_WAND_EFFECTS_COMPONENT);
		return bubbleWandEffectsComponent != null ? bubbleWandEffectsComponent.color() : Optional.empty();
	}

	public static boolean getRainbow(ItemStack stack) {
		BubbleWandEffectsComponent bubbleWandEffectsComponent = stack.get(ModRegistry.BUBBLE_WAND_EFFECTS_COMPONENT);
		return bubbleWandEffectsComponent != null ? bubbleWandEffectsComponent.rainbow().orElse(false) : false;
	}

	public static ItemStack setRainbow(ItemStack stack) {
		ItemStack itemStack = stack.copyWithCount(1);
		itemStack.set(ModRegistry.BUBBLE_WAND_EFFECTS_COMPONENT, new BubbleWandEffectsComponent(Optional.empty(), Optional.of(true)));
		return itemStack;
	}
	
	public static ItemStack setColor(ItemStack stack, List<DyeItem> dyes) {
		if (!stack.isOf(ModRegistry.BUBBLE_WAND)) {
			return ItemStack.EMPTY;
		} else {
			ItemStack itemStack = stack.copyWithCount(1);
			int i = 0;
			int j = 0;
			int k = 0;
			int l = 0;
			int m = 0;
			BubbleWandEffectsComponent dyedColorComponent = itemStack.get(ModRegistry.BUBBLE_WAND_EFFECTS_COMPONENT);
			if (dyedColorComponent != null && dyedColorComponent.color().isPresent()) {
				int n = ColorHelper.getRed(dyedColorComponent.color().get());
				int o = ColorHelper.getGreen(dyedColorComponent.color().get());
				int p = ColorHelper.getBlue(dyedColorComponent.color().get());
				l += Math.max(n, Math.max(o, p));
				i += n;
				j += o;
				k += p;
				m++;
			}

			for (DyeItem dyeItem : dyes) {
				int p = dyeItem.getColor().getEntityColor();
				int q = ColorHelper.getRed(p);
				int r = ColorHelper.getGreen(p);
				int s = ColorHelper.getBlue(p);
				l += Math.max(q, Math.max(r, s));
				i += q;
				j += r;
				k += s;
				m++;
			}

			int n = i / m;
			int o = j / m;
			int p = k / m;
			float f = (float)l / m;
			float g = Math.max(n, Math.max(o, p));
			n = (int)(n * f / g);
			o = (int)(o * f / g);
			p = (int)(p * f / g);
			int s = ColorHelper.getArgb(0, n, o, p);
			itemStack.set(ModRegistry.BUBBLE_WAND_EFFECTS_COMPONENT, new BubbleWandEffectsComponent(Optional.of(s), Optional.empty()));
			return itemStack;
		}
	}
	
	@Override
	public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
		if (this.color.isPresent() || this.rainbow.isPresent()) {
			int color = this.color.isPresent() ? this.color.get() : BubbleEntity.rainbowColor((float)(System.currentTimeMillis() % 36000)); // lol
			if (type.isAdvanced()) {
				if (this.color.isPresent()) {
					textConsumer.accept(Text.translatable("item.color", String.format(Locale.ROOT, "#%06X", color)).withColor(color));
				} else {
					textConsumer.accept(Text.translatable("item.blowing-bubbles.bubble_wand.rainbow").withColor(color));
				}
			} else {
				textConsumer.accept(Text.translatable("item.dyed").formatted(Formatting.ITALIC).withColor(color));
			}
		}
	}
}

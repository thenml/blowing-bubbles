package net.nml.bubble;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.nml.bubble.item.BubbleWandItem;

public class BubbleSizeIndicatorHUD implements HudElement {
	private static final Identifier PIXEL_TEXTURE = Identifier.of(BlowingBubbles.MOD_ID, "pixel");

	@Override
	public void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

		if (!client.options.getPerspective().isFirstPerson()) return;

        ItemStack stack = client.player.getActiveItem();
        if (!(stack.getItem() instanceof BubbleWandItem wand)) return;
        if (!client.player.isUsingItem()) return;

        float size = wand.getSize(wand.getUseTicks(client.player.getItemUseTime(), stack), stack);
		int radius = (int)(size * 5);

		int x = (context.getScaledWindowWidth() - 1) / 2;
		int y = (context.getScaledWindowHeight() - 1) / 2;

		drawCircle(context, x, y, radius);

		if (client.getDebugHud().shouldShowDebugHud()) {
			String text = String.format("%.1f", size);
			int textWidth = client.textRenderer.getWidth(text);
			context.drawText(client.textRenderer, text, x - (textWidth / 2), y + 2 + radius, 0xeeeeeeff, true);
		}
	}
	private void drawCircle(DrawContext context, int cx, int cy, int radius) {
		int x = radius;
        int y = 0;
        int d = 1 - radius;

        while (x >= y) {
			drawPixel(context, cx + x, cy + y);
			drawPixel(context, cx - x, cy - y);
			if (x > y) {
				drawPixel(context, cx + y, cy + x);
				drawPixel(context, cx - y, cy - x);
			}
            if (y > 0) {
				drawPixel(context, cx - x, cy + y);
				drawPixel(context, cx + x, cy - y);
				if (x > y) {
					drawPixel(context, cx - y, cy + x);
					drawPixel(context, cx + y, cy - x);
				}
			}

            y++;

            if (d < 0) {
                d += 2 * y + 1;
            } else {
                x--;
                d += 2 * (y - x) + 1;
            }
        }
	}
	
	private void drawPixel(DrawContext context, int x, int y) {
		context.drawGuiTexture(RenderPipelines.CROSSHAIR, PIXEL_TEXTURE, x, y, 1, 1);
		// context.fill(RenderPipelines.CROSSHAIR, x, y, x + size, y + size, color);
	}
	
}

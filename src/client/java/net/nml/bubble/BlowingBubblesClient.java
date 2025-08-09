package net.nml.bubble;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.util.Identifier;
import net.nml.bubble.renderer.BubbleModelMedium;
import net.nml.bubble.renderer.BubbleModelSmall;
import net.nml.bubble.renderer.BubbleRenderer;

public class BlowingBubblesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(BubbleModelSmall.LAYER, BubbleModelSmall::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BubbleModelMedium.LAYER, BubbleModelMedium::getTexturedModelData);
		EntityRendererRegistry.register(ModRegistry.BUBBLE, BubbleRenderer::new);
		HudElementRegistry.attachElementAfter(VanillaHudElements.BOSS_BAR, Identifier.of(BlowingBubbles.MOD_ID, "bubble_size_indicator"), new BubbleSizeIndicatorHUD());
    }
}
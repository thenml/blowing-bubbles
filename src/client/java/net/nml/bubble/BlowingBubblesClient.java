package net.nml.bubble;

// import com.supermartijn642.fusion.api.predicate.FusionPredicateRegistry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.nml.bubble.block.BubbleBlock;
// import net.nml.bubble.datagen.BubbleBlockConnectionPredicate;
import net.nml.bubble.renderer.BubbleModelMedium;
import net.nml.bubble.renderer.BubbleModelSmall;
import net.nml.bubble.renderer.BubbleRenderer;

public class BlowingBubblesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// FusionPredicateRegistry.registerConnectionPredicate(Identifier.of(BlowingBubbles.MOD_ID, "bubble_block"), BubbleBlockConnectionPredicate.SERIALIZER);

		EntityModelLayerRegistry.registerModelLayer(BubbleModelSmall.LAYER, BubbleModelSmall::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BubbleModelMedium.LAYER, BubbleModelMedium::getTexturedModelData);
		EntityRendererRegistry.register(ModRegistry.BUBBLE, BubbleRenderer::new);
		HudElementRegistry.attachElementAfter(VanillaHudElements.BOSS_BAR, Identifier.of(BlowingBubbles.MOD_ID, "bubble_size_indicator"), new BubbleSizeIndicatorHUD());
		ParticleFactoryRegistry.getInstance().register(ModRegistry.POP_PARTICLE, PopParticle.Factory::new);

		for (Pair<BubbleBlock, String> pair : ModRegistry.BUBBLE_BLOCKS) {
			BubbleBlock block = pair.getLeft();
			ColorProviderRegistry.BLOCK.register((state, view, pos, i) -> block.getColor().getEntityColor() % 0xffffff, block);
			BlockRenderLayerMap.putBlock(block, BlockRenderLayer.TRANSLUCENT);
		}
    }
}
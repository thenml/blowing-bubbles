package net.nml.bubble.renderer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.nml.bubble.BlowingBubbles;
import net.nml.bubble.BubbleEntity;
import net.nml.bubble.Config;

public class BubbleRenderer extends LivingEntityRenderer<BubbleEntity, BubbleRenderState, BubbleModelAbstract> {
	private static final Identifier BASE_TEXTURE_FAST = Identifier.of(BlowingBubbles.MOD_ID, "textures/entity/bubble/bubble_fast.png");
	private static final Identifier BASE_TEXTURE_SIMPLE = Identifier.of(BlowingBubbles.MOD_ID, "textures/entity/bubble/bubble_simple.png");
	private static final Identifier BASE_TEXTURE_SMALL = Identifier.of(BlowingBubbles.MOD_ID, "textures/entity/bubble/bubble_small.png");
	private static final Identifier BASE_TEXTURE_MEDIUM = Identifier.of(BlowingBubbles.MOD_ID, "textures/entity/bubble/bubble_medium.png");
	private static final Identifier SHINE_TEXTURE = Identifier.of(BlowingBubbles.MOD_ID, "textures/entity/bubble/shine.png");

	private final BubbleModelAbstract model_fast;
	private final BubbleModelAbstract model_simple;
	private final BubbleModelAbstract model_medium;

	public BubbleRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new BubbleModelSmall(ctx.getPart(BubbleModelSmall.LAYER)), 0.0f);
		this.model_fast = new BubbleModelFast(ctx.getPart(BubbleModelFast.LAYER));
		this.model_simple = new BubbleModelSimple(ctx.getPart(BubbleModelSimple.LAYER));
		this.model_medium = new BubbleModelMedium(ctx.getPart(BubbleModelMedium.LAYER));
	}

	@Override
	public void render(BubbleRenderState state, MatrixStack matrices, VertexConsumerProvider vertex, int light) {
		// shine texture
		if (state.squaredDistanceToCamera >= state.width * state.width / 2f) {
			float shineScale = getScale(state);
			matrices.push();
			matrices.scale(shineScale, shineScale, shineScale);
			matrices.translate(0.0, 0.25, 0);
			matrices.multiply(this.dispatcher.getRotation());
			matrices.scale(0.5f, 0.5f, 0.5f);
			
			MatrixStack.Entry positionMatrix = matrices.peek();
			MatrixStack.Entry normalMatrix = matrices.peek();

			VertexConsumer shineConsumer = vertex.getBuffer(RenderLayer.getEnergySwirl(SHINE_TEXTURE, 0, 0)); // idk and idc
			
			float[][] vertices = {
				{-1f, -1f, 0f, 0f, 1f},
				{1f, -1f, 0f, 1f, 1f},
				{1f, 1f, 0f, 1f, 0f},
				{-1f, 1f, 0f, 0f, 0f}
			};
			
			for (float[] v : vertices) {
				shineConsumer.vertex(positionMatrix, v[0], v[1], v[2])
				.color(shineColor(state))
				.texture(v[3], v[4])
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(normalMatrix, 0, 0, -1);
			}		
			
			matrices.pop(); // pop shine
		}
		
		// bubble
		float bubbleScale = getBubbleScale(state);
		matrices.push();
		matrices.scale(bubbleScale, bubbleScale, bubbleScale);
		// this.setupTransforms(state, matrices, state.bodyYaw, bubbleScale);
		
		VertexConsumer base = state.hasOutline
			? vertex.getBuffer(RenderLayer.getOutline(this.getTexture(state)))
			: vertex.getBuffer(RenderLayer.getEntityNoOutline(this.getTexture(state)));
		//TODO: getEntityTranslucent() is making the player / water invisible

		this.getModel(state).render(matrices, base, light, OverlayTexture.DEFAULT_UV, ColorHelper.withAlpha(0.5f * state.opacity, state.color));
		
		matrices.pop(); // pop main
		
	}

	private int shineColor(BubbleRenderState state) {
		// float base = 0.5f * state.opacity;
		// float t = (float)state.squaredDistanceToCamera / 100f;
		return ColorHelper.scaleRgb(ColorHelper.fullAlpha(state.color), 1.5f);
	}

	private boolean isMedium(BubbleRenderState state) {
		return Config.bubbleQuality == Config.ModelQuality.QUALITY && state.size >= 2.5f;
	}

	private float getScale(BubbleRenderState state) {
		return state.baseScale * state.size;
	}

	private float getBubbleScale(BubbleRenderState state) {
		float scale = getScale(state);
		if (isMedium(state)) scale *= 0.5f;
		return scale;
	}

	@Override
	public Identifier getTexture(BubbleRenderState state) {
		if (Config.bubbleQuality == Config.ModelQuality.FAST) return BASE_TEXTURE_FAST;
		if (Config.bubbleQuality == Config.ModelQuality.PLAIN) return BASE_TEXTURE_SIMPLE;
		if (isMedium(state)) return BASE_TEXTURE_MEDIUM;
		return BASE_TEXTURE_SMALL;
	}

	@Override
	protected boolean hasLabel(BubbleEntity entity, double d) {
		return false;
	}

	public BubbleModelAbstract getModel(BubbleRenderState state) {
		if (Config.bubbleQuality == Config.ModelQuality.FAST) return this.model_fast;
		if (Config.bubbleQuality == Config.ModelQuality.PLAIN) return this.model_simple;
		if (isMedium(state)) return this.model_medium;
		return this.model;
	}

	@Override
	public BubbleRenderState createRenderState() {
		return new BubbleRenderState();
	}
	
	public void updateRenderState(BubbleEntity entity, BubbleRenderState state, float tickDelta) {
		super.updateRenderState(entity, state, tickDelta);
        state.size = entity.getSize();
        state.opacity = entity.getOpacity();
		state.color = entity.getColor();
		state.squaredDistanceToCamera = this.dispatcher.camera.getPos().squaredDistanceTo(entity.getPos().add(0, entity.getHeight() / 2f, 0));
	}
}

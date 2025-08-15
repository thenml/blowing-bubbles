package net.nml.bubble.renderer;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.nml.bubble.BlowingBubbles;

public class BubbleModelSmall extends BubbleModelAbstract {
	public static final EntityModelLayer LAYER = new EntityModelLayer(
			Identifier.of(BlowingBubbles.MOD_ID, "bubble_small"), "main");

	public BubbleModelSmall(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("bb_main",
			ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, -1.0F, 6.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 5).cuboid(-10.0F, -8.0F, 6.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
				.uv(20, 20).cuboid(-10.0F, -2.0F, 10.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(20, 22).cuboid(-10.0F, -2.0F, 5.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(16, 5).cuboid(-6.0F, -2.0F, 6.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 18).cuboid(-11.0F, -2.0F, 6.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
				.uv(20, 15).cuboid(-6.0F, -7.0F, 6.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
				.uv(10, 23).cuboid(-10.0F, -7.0F, 10.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(20, 10).cuboid(-11.0F, -7.0F, 6.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 23).cuboid(-10.0F, -7.0F, 5.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 10).cuboid(-5.0F, -6.0F, 6.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.0F))
				.uv(10, 10).cuboid(-12.0F, -6.0F, 6.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.0F))
				.uv(20, 24).cuboid(-6.0F, -6.0F, 5.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(24, 24).cuboid(-6.0F, -6.0F, 10.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 25).cuboid(-11.0F, -6.0F, 10.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(4, 25).cuboid(-11.0F, -6.0F, 5.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(16, 0).cuboid(-10.0F, -6.0F, 4.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(10, 18).cuboid(-10.0F, -6.0F, 11.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F)),
			ModelTransform.origin(8.0F, 8.0F, -8.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(BubbleRenderState state) {
		super.setAngles(state);
	}
}

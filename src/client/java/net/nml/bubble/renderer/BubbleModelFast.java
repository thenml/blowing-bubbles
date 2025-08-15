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

public class BubbleModelFast extends BubbleModelAbstract {
	public static final EntityModelLayer LAYER = new EntityModelLayer(
			Identifier.of(BlowingBubbles.MOD_ID, "bubble_fast"), "main");

	public BubbleModelFast(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("bb_main",
				ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)),
				ModelTransform.origin(0F, 8.0F, 0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(BubbleRenderState state) {
		super.setAngles(state);
	}
}

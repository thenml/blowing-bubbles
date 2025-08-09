package net.nml.bubble.renderer;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;

public abstract class BubbleModelAbstract extends EntityModel<BubbleRenderState> {
	protected BubbleModelAbstract(ModelPart root) {
		super(root);
	}	
}

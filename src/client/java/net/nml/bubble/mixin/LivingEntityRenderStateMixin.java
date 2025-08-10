package net.nml.bubble.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.nml.bubble.LivingEntityRendererStateI;

@Mixin(LivingEntityRenderState.class)
public abstract class LivingEntityRenderStateMixin implements LivingEntityRendererStateI {
	public boolean inBubble;

	@Override
	public void setInBubble(boolean b) {
		this.inBubble = b;
	}

	@Override
	public boolean getInBubble() {
		return this.inBubble;
	}
}

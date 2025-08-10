package net.nml.bubble.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import net.nml.bubble.BubbleEntity;
import net.nml.bubble.LivingEntityRendererStateI;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD")) 
	public void updateRenderState(LivingEntity entity, LivingEntityRenderState state, float f, CallbackInfo ci) {
		if (state instanceof LivingEntityRendererStateI stateI) {
			stateI.setInBubble(entity.getVehicle() instanceof BubbleEntity && !(entity instanceof BubbleEntity));
		}
	}

	@Inject(method = "setupTransforms(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;FF)V", at = @At("RETURN"))
	public void onSetupTransforms(LivingEntityRenderState state, MatrixStack matrices, float bodyYaw, float baseHeight, CallbackInfo ci) {
		if (state instanceof LivingEntityRendererStateI stateI && stateI.getInBubble()) {
			float time = state.age / 20.0f;
			float spinX = (float) (Math.sin(time) * 15f);
			float spinY = time * 40f;
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(spinX));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(spinY));
			matrices.translate(0.0, -state.height / 2f, 0.0);
		}
	}
}

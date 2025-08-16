package net.nml.bubble.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.nml.bubble.BubbleEntity;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F", at = @At("RETURN"), cancellable = true)
    private void getBlockBreakingSpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        PlayerEntity self = (PlayerEntity)(Object)this;
        if (self.getVehicle() instanceof BubbleEntity) {
            float speed = cir.getReturnValue();
            if (!self.isOnGround()) {
                speed *= 5.0F;
            }

            if (self.isSubmergedIn(FluidTags.WATER)) {
                double waterPenalty = self.getAttributeInstance(EntityAttributes.SUBMERGED_MINING_SPEED).getValue();
                if (waterPenalty != 0.0) {
                    speed /= (float) waterPenalty;
                }
            }

            cir.setReturnValue(speed);
        }
    }
}

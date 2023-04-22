package net.jay.mobsexplode.mixin;

import net.jay.mobsexplode.MobsExplode;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "scale", at = @At("HEAD"))
    public void scale(T entity, MatrixStack matrices, float amount, CallbackInfo ci) {
        int entityId = entity.getId();

        float g = MathHelper.lerp(amount, (float)MobsExplode.lastMobsFuseTime[entityId], (float)MobsExplode.mobsFuseTime[entityId]) / (float)(MobsExplode.fuseTime - 2);
        float h = 1.0F + MathHelper.sin(g * 100.0F) * g * 0.01F;
        g = MathHelper.clamp(g, 0.0F, 1.0F);
        g *= g;
        g *= g;
        float i = (1.0F + g * 0.4F) * h;
        float j = (1.0F + g * 0.1F) / h;
        matrices.scale(i, j, i);
    }

    @Inject(method = "getAnimationCounter", at = @At("HEAD"), cancellable = true)
    public void getAnimationCounter(T entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
        int entityId = entity.getId();
        float g = MathHelper.lerp(tickDelta, (float)MobsExplode.lastMobsFuseTime[entityId], (float)MobsExplode.mobsFuseTime[entityId]) / (float)(MobsExplode.fuseTime - 2);
        cir.setReturnValue((int)(g * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(g, 0.5F, 1.0F));
    }
}

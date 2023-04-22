package net.jay.mobsexplode.mixin;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.CreeperChargeFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntityRenderer.class)
public abstract class MobEntityRendererMixin<T extends MobEntity, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {
    public MobEntityRendererMixin(EntityRendererFactory.Context ctx, M model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(EntityRendererFactory.Context context, EntityModel entityModel, float f, CallbackInfo ci) {

    }
}

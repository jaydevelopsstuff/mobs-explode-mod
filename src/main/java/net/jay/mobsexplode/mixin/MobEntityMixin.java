package net.jay.mobsexplode.mixin;

import net.jay.mobsexplode.MobIgniteGoal;
import net.jay.mobsexplode.MobsExplode;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Targeter;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Iterator;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements Targeter {
	protected MobEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	protected void constructor(EntityType entityType, World world, CallbackInfo ci) {
		if(((MobEntity)(Object)this) instanceof PathAwareEntity) ((MobEntityAccessor)this).getGoalSelector().add(4, new MeleeAttackGoal(((PathAwareEntity)(Object)this), 1.0, false));
		((MobEntityAccessor)this).getGoalSelector().add(0, new MobIgniteGoal((MobEntity)(Object)this));
		((MobEntityAccessor)this).getTargetSelector().add(1, new ActiveTargetGoal(((MobEntity)(Object)this), PlayerEntity.class, true));
	}

	@Inject(method = "tick", at = @At("RETURN"))
	public void tick(CallbackInfo ci) {
		if(((MobEntity)(Object)this) instanceof CreeperEntity) return;

		if(this.isAlive()) {
			MobsExplode.lastMobsFuseTime[this.getId()] = MobsExplode.mobsFuseTime[this.getId()];

			int i = MobsExplode.mobsFuseSpeed[this.getId()];
			if (i > 0 && MobsExplode.mobsFuseTime[this.getId()] == 0) {
				this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 1.0F, 0.5F);
			}

			MobsExplode.mobsFuseTime[this.getId()] += i;
			if(MobsExplode.mobsFuseTime[this.getId()] < 0) {
				MobsExplode.mobsFuseTime[this.getId()] = 0;
			}

			if (MobsExplode.mobsFuseTime[this.getId()] >= MobsExplode.fuseTime) {
				MobsExplode.mobsFuseTime[this.getId()] = MobsExplode.fuseTime;
				this.explode();
			}
		}
	}

	private void explode() {
		if (!this.world.isClient) {
			this.dead = true;
			this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), (float)3 * 1, World.ExplosionSourceType.MOB);
			this.discard();
			spawnEffectsCloud();
		}
	}

	private void spawnEffectsCloud() {
		Collection<StatusEffectInstance> collection = this.getStatusEffects();
		if (!collection.isEmpty()) {
			AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.world, this.getX(), this.getY(), this.getZ());
			areaEffectCloudEntity.setRadius(2.5F);
			areaEffectCloudEntity.setRadiusOnUse(-0.5F);
			areaEffectCloudEntity.setWaitTime(10);
			areaEffectCloudEntity.setDuration(areaEffectCloudEntity.getDuration() / 2);
			areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float)areaEffectCloudEntity.getDuration());
			Iterator var3 = collection.iterator();

			while(var3.hasNext()) {
				StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var3.next();
				areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffectInstance));
			}

			this.world.spawnEntity(areaEffectCloudEntity);
		}

	}
}

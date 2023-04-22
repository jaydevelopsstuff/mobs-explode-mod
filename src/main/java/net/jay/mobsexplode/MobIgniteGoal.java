package net.jay.mobsexplode;

import java.util.EnumSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;

public class MobIgniteGoal extends Goal {
    private final MobEntity mob;
    @Nullable
    private LivingEntity target;

    public MobIgniteGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    public boolean canStart() {
        LivingEntity livingEntity = this.mob.getTarget();
        return MobsExplode.mobsFuseSpeed[mob.getId()] > 0 || livingEntity != null && this.mob.squaredDistanceTo(livingEntity) < 9.0;
    }

    public void start() {
        this.mob.getNavigation().stop();
        this.target = this.mob.getTarget();
    }

    public void stop() {
        this.target = null;
    }

    public boolean shouldRunEveryTick() {
        return true;
    }

    public void tick() {
        if (this.target == null) {
            MobsExplode.mobsFuseSpeed[mob.getId()] = -1;
        } else if (this.mob.squaredDistanceTo(this.target) > 49.0) {
            MobsExplode.mobsFuseSpeed[mob.getId()] = -1;
        } else if (!this.mob.getVisibilityCache().canSee(this.target)) {
            MobsExplode.mobsFuseSpeed[mob.getId()]= -1;
        } else {
            MobsExplode.mobsFuseSpeed[mob.getId()] = 1;
        }
    }
}

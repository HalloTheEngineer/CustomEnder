package de.hallotheengineer.customender.mixin;

import de.hallotheengineer.customender.EndermanBehaviour;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Mixin(EnderMan.class)
public abstract class EndermanMixin extends Monster {

    protected EndermanMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void initPlace(CallbackInfo ci) {

        if (!de.hallotheengineer.customender.config.Config.get().active) return;

        EnderMan enderman = (EnderMan) (Object) this;

        if (!(enderman.level() instanceof ServerLevel)) return;

        GoalSelector goalSelector = ((MobAccessor) enderman).getGoalSelector();

        enderman.removeAllGoals(goal -> {
            String n = goal.getClass().getSimpleName();

            // :skull:
            return Objects.equals(n, "EndermanLeaveBlockGoal") // place
                    || Objects.equals(n, "EndermanTakeBlockGoal"); // pickup
        });

        goalSelector.addGoal(8, new EndermanBehaviour.PlaceBlockGoal(enderman));
    }

}

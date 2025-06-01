package de.hallotheengineer.customender.mixin;

import de.hallotheengineer.customender.EndermanBehaviour;
import de.hallotheengineer.customender.config.Config;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Mixin(EndermanEntity.class)
public abstract class EndermanMixin extends MobEntity {

    protected EndermanMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void initPlace(CallbackInfo ci) {

        if (!Config.get().active) return;

        EndermanEntity enderman = (EndermanEntity) (Object) this;

        if (!(enderman.getWorld() instanceof ServerWorld)) return;

        GoalSelector goalSelector = ((MobEntityAccessor) enderman).getGoalSelector();

        enderman.clearGoals(goal -> {
            String n = goal.getClass().getSimpleName();

            // :skull:
            return Objects.equals(n, "class_1561") // place
                    || Objects.equals(n, "class_1563"); // pickup
        });

        goalSelector.add(8, new EndermanBehaviour.PlaceBlockGoal(enderman));
    }

}

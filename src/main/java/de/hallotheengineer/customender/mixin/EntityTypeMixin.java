package de.hallotheengineer.customender.mixin;

import de.hallotheengineer.customender.EndermanBehaviour;
import de.hallotheengineer.customender.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Mixin(EntityType.class)
public class EntityTypeMixin<T extends Entity> {

    @Unique
    @Deprecated
    private static final List<EntitySpawnReason> spawnReasons = Arrays.asList(
            EntitySpawnReason.NATURAL,
            EntitySpawnReason.MOB_SUMMONED,
            EntitySpawnReason.SPAWN_ITEM_USE
    );

    @Inject(method = "spawn(Lnet/minecraft/server/level/ServerLevel;Ljava/util/function/Consumer;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/EntitySpawnReason;ZZ)Lnet/minecraft/world/entity/Entity;", at = @At("RETURN"))
    private void initialize(ServerLevel level, @Nullable Consumer<T> postSpawnConfig, BlockPos spawnPos, EntitySpawnReason spawnReason, boolean tryMoveDown, boolean movedUp, CallbackInfoReturnable<T> cir) {
        if (!Config.get().active) return;

        T entity = cir.getReturnValue();

        if (entity instanceof EnderMan enderman) {
            Block randomBlock = EndermanBehaviour.getRandomEndermanBlock();

            if (randomBlock != null) {
                enderman.setCarriedBlock(randomBlock.defaultBlockState());
            }
        }
    }
}

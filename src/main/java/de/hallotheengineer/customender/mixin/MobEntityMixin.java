package de.hallotheengineer.customender.mixin;

import de.hallotheengineer.customender.EndermanBehaviour;
import de.hallotheengineer.customender.config.Config;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Unique
    private static final List<SpawnReason> spawnReasons = Arrays.asList(
            SpawnReason.NATURAL,
            SpawnReason.MOB_SUMMONED,
            SpawnReason.SPAWN_ITEM_USE
    );
    @Inject(method = "initialize", at = @At("TAIL"))
    private void initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {

        if (!Config.get().active) return;

        if (((Object) this) instanceof EndermanEntity enderman && spawnReasons.contains(spawnReason)) {
            Block b = EndermanBehaviour.getRandomEndermanBlock();
            if (b != null) {
                enderman.setCarriedBlock(b.getDefaultState());
            }
        }

    }
}

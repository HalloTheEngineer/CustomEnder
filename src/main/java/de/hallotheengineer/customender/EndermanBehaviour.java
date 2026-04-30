package de.hallotheengineer.customender;

import de.hallotheengineer.customender.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class EndermanBehaviour {

    private static final Random random = new Random();

    public static Block getRandomEndermanBlock() {
        List<String> bl = Config.get().blocks;
        if (bl.isEmpty() || random.nextFloat(100) >= Math.min(Config.get().spawnPercentage, 100))
            return null;

        Identifier i = Identifier.tryParse(bl.get(random.nextInt(bl.size())));
        if (i == null) return null;
        return BuiltInRegistries.BLOCK.get(i).orElseThrow().value();
    }

    public static class PlaceBlockGoal extends Goal {
        private final EnderMan enderman;

        public PlaceBlockGoal(EnderMan enderman) {
            this.enderman = enderman;
        }

        /**
         * Converts the base tick value into a value adjusted for the server's tick rate
         * and difficulty, matching the standard Minecraft AI behavior.
         */
        protected int toGoalTicks(int ticks) {
            return reducedTickDelay(ticks);
        }

        private ServerLevel getServerLevel(EnderMan enderman) {
            return (ServerLevel) enderman.level();
        }

        @Override
        public boolean canUse() {
            if (this.enderman.getCarriedBlock() == null) {
                return false;
            } else if (!getServerLevel(this.enderman).getGameRules().get(GameRules.MOB_GRIEFING)) {
                return false;
            } else {
                return this.enderman.getRandom().nextInt(this.toGoalTicks(2000)) == 0;
            }
        }

        @Override
        public void tick() {
            RandomSource random = this.enderman.getRandom();
            Level world = this.enderman.level();
            int i = (int) Math.floor(this.enderman.getX() - 1.0 + random.nextDouble() * 2.0);
            int j = (int) Math.floor(this.enderman.getY() + random.nextDouble() * 2.0);
            int k = (int) Math.floor(this.enderman.getZ() - 1.0 + random.nextDouble() * 2.0);
            BlockPos blockPos = new BlockPos(i, j, k);
            BlockState blockState = world.getBlockState(blockPos);
            BlockPos blockPosBelow = blockPos.below();
            BlockState blockStateBelow = world.getBlockState(blockPosBelow);
            BlockState carriedState = this.enderman.getCarriedBlock();

            if (carriedState != null) {
                if (this.canPlaceOn(world, blockPos, carriedState, blockState, blockStateBelow, blockPosBelow)) {
                    world.setBlock(blockPos, carriedState, Block.UPDATE_ALL);
                    world.gameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(this.enderman, carriedState));
                    this.enderman.setCarriedBlock(null);
                }
            }
        }

        private boolean canPlaceOn(Level world, BlockPos posAbove, BlockState carriedState, BlockState state, BlockState stateBelow, BlockPos pos) {
            return state.isAir()
                    && (stateBelow.is(BlockTags.BASE_STONE_OVERWORLD))
                    && !stateBelow.isAir()
                    && !stateBelow.is(Blocks.BEDROCK)
                    && stateBelow.isCollisionShapeFullBlock(world, pos)
                    && world.getEntities(this.enderman, AABB.ofSize(new Vec3(posAbove.getX() + 0.5, posAbove.getY() + 0.5, posAbove.getZ() + 0.5), 1, 1, 1)).isEmpty();
        }
    }
}
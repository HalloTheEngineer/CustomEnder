package de.hallotheengineer.customender;

import de.hallotheengineer.customender.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

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
        return Registries.BLOCK.get(i);
    }

    public static class PlaceBlockGoal extends Goal {
        private final EndermanEntity enderman;

        public PlaceBlockGoal(EndermanEntity enderman) {
            this.enderman = enderman;
        }

        @Override
        public boolean canStart() {
            if (this.enderman.getCarriedBlock() == null) return false;
            else return getServerWorld(this.enderman).getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)
                    && this.enderman.getRandom().nextInt(toGoalTicks(2000)) == 0;
        }

        @Override
        public void tick() {
            net.minecraft.util.math.random.Random random = this.enderman.getRandom();
            World world = this.enderman.getWorld();
            int i = MathHelper.floor(this.enderman.getX() - 1.0 + random.nextDouble() * 2.0);
            int j = MathHelper.floor(this.enderman.getY() + random.nextDouble() * 2.0);
            int k = MathHelper.floor(this.enderman.getZ() - 1.0 + random.nextDouble() * 2.0);
            BlockPos blockPos = new BlockPos(i, j, k);
            BlockState blockState = world.getBlockState(blockPos);
            BlockPos blockPos2 = blockPos.down();
            BlockState blockState2 = world.getBlockState(blockPos2);
            BlockState blockState3 = this.enderman.getCarriedBlock();
            if (blockState3 != null) {
                //blockState3 = Block.postProcessState(blockState3, this.enderman.getWorld(), blockPos);
                if (this.canPlaceOn(world, blockPos, blockState3, blockState, blockState2, blockPos2)) {
                    world.setBlockState(blockPos, blockState3, Block.NOTIFY_ALL);
                    world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Emitter.of(this.enderman, blockState3));
                    this.enderman.setCarriedBlock(null);
                }
            }
        }

        private boolean canPlaceOn(World world, BlockPos posAbove, BlockState carriedState, BlockState state, BlockState stateBelow, BlockPos pos) {
            return state.isAir()
                    && (stateBelow.isIn(BlockTags.BASE_STONE_OVERWORLD))
                    && !stateBelow.isAir()
                    && !stateBelow.isOf(Blocks.BEDROCK)
                    && stateBelow.isFullCube(world, pos)
                    // && carriedState.canPlaceAt(world, posAbove)
                    && world.getOtherEntities(this.enderman, Box.from(Vec3d.of(posAbove))).isEmpty();
        }
    }
}

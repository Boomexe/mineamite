package net.boomexe.mineamite.entity.custom;

import net.boomexe.mineamite.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class TimedC4Entity extends Entity implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
//    private static final EntityDataAccessor<Integer> FUSE_TIMER = SynchedEntityData.defineId(TimedC4Entity.class, EntityDataSerializers.INT);

    private final int maxFuseTime = 800;
    private int fuseTime = 0;

    private final int maxDefuseTime = 100;
    private int defuseTime = 0;
    private int ticksSinceLastInteraction = 0;

    private int ticksUntilBeep = 20;

    private boolean defused = false;

    public TimedC4Entity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
//        this.entityData.define(FUSE_TIMER, 800);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
//        if (pCompound.contains())
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            fuseTime++;
            ticksUntilBeep--;

            if (!defused) {
                // Explosion
                if (fuseTime >= maxFuseTime) {
                    this.level().explode(this, this.getX(), this.getY(), this.getZ(), 100, Level.ExplosionInteraction.NONE);
                    this.discard();
                }

                if (defuseTime >= maxDefuseTime) {
                    defused = true;
                }

                // Sounds
                if (ticksUntilBeep <= 0) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.AMBIENT, 10f, 1f);
                    ticksUntilBeep = (int) (Math.max(0.1 + 0.9 * -(((double) fuseTime - maxFuseTime) / maxFuseTime), 0.15) * 20);
                }
            }

            // if edit 5 then also edit below 5 in interact method
            if (ticksSinceLastInteraction > 5) {
                defuseTime = 0;
                setCustomNameVisible(false);
            } else {
                ticksSinceLastInteraction++;
            }
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (!defused) {
            if (pPlayer.distanceToSqr(this) <= 1.5f) {
                if (pPlayer.getItemInHand(pHand).getItem() == ModItems.WIRE_CUTTERS.get()) {
                    if (!this.level().isClientSide) {

                        if (ticksSinceLastInteraction > 5) {
                            defuseTime++;
                        } else {
                            defuseTime += ticksSinceLastInteraction;
                        }

                        ticksSinceLastInteraction = 0;

                        int defuseTimeLeft = maxDefuseTime - defuseTime;

                        setCustomName(Component.nullToEmpty(Integer.toString(defuseTimeLeft)));
                        setCustomNameVisible(true);
                    }
                }
            }
        } else {
            if (pPlayer.isCrouching()) {
                if (!this.level().isClientSide) {
                    ItemEntity itemEntity = new ItemEntity(this.level(), this.position().x(), this.position().y(), this.position().z(), new ItemStack(ModItems.C4.get(), 1));
                    this.level().addFreshEntity(itemEntity);
                    this.discard();

                    return InteractionResult.SUCCESS;
                }

                return InteractionResult.PASS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean isPickable() {
        return true;
    }
}

package net.boomexe.mineamite.entity.custom;

import net.boomexe.mineamite.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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

    private static final EntityDataAccessor<Boolean> DEFUSED = SynchedEntityData.defineId(TimedC4Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FUSE_TIME = SynchedEntityData.defineId(TimedC4Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DEFUSE_TIME = SynchedEntityData.defineId(TimedC4Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TICKS_SINCE_LAST_INTERACTION = SynchedEntityData.defineId(TimedC4Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TICKS_UNTIL_BEEP = SynchedEntityData.defineId(TimedC4Entity.class, EntityDataSerializers.INT);


    private final int maxFuseTime = 800;
//    private int fuseTime = 0;

    private final int maxDefuseTime = 100;
//    private int defuseTime = 0;
//    private int ticksSinceLastInteraction = 0;

//    private int ticksUntilBeep = 20;

//    private boolean defused = false;

    public TimedC4Entity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DEFUSED, false);
        this.entityData.define(FUSE_TIME, 0);
        this.entityData.define(DEFUSE_TIME, 0);
        this.entityData.define(TICKS_SINCE_LAST_INTERACTION, 0);
        this.entityData.define(TICKS_UNTIL_BEEP, 20);
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
            this.entityData.set(FUSE_TIME, getFuseTime() + 1);
            this.entityData.set(FUSE_TIME, this.entityData.get(TICKS_UNTIL_BEEP) - 1);
//            fuseTime++;
//            ticksUntilBeep--;

            if (!getDefused()) {
                // configurable
                int primeExplosionSoundOffset = 20;

                if (getFuseTime() >= maxFuseTime - primeExplosionSoundOffset) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.AMBIENT, 5f, 1f);
                }
                // Explosion
                if (getFuseTime() >= maxFuseTime) {
                    this.level().explode(this, this.getX(), this.getY(), this.getZ(), 100, Level.ExplosionInteraction.NONE);
                    this.discard();
                }

                if (getFuseTime() >= maxDefuseTime) {
                    this.entityData.set(DEFUSED, true);
                }

                // Sounds
                if (getTicksUntilBeep() <= 0) {
                    // config possibility
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.AMBIENT, 5f, 1f);

                    int ticksUntilBeep = (int) (Math.max(0.1 + 0.9 * -(((double) getFuseTime() - maxFuseTime) / maxFuseTime), 0.15) * 20);
                    this.entityData.set(TICKS_UNTIL_BEEP, ticksUntilBeep);
                }
            }

            // if edit 5 then also edit below 5 in interact method
            if (getTicksSinceLastInteraction() > 5) {
                this.entityData.set(DEFUSE_TIME, 0);
                setCustomNameVisible(false);
            } else {
                this.entityData.set(TICKS_SINCE_LAST_INTERACTION, getTicksSinceLastInteraction() + 1);
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
        if (!getDefused()) {
            if (pPlayer.distanceToSqr(this) <= 1.5f) {
                if (pPlayer.getItemInHand(pHand).getItem() == ModItems.WIRE_CUTTERS.get()) {
                    if (!this.level().isClientSide) {

                        // Defuse Begin Sound
                        if (getDefuseTime() == 0) {
                            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ALLAY_THROW, SoundSource.AMBIENT, 5f, 0.7f);
                        }

                        if (getTicksSinceLastInteraction() > 5) {
                            this.entityData.set(DEFUSE_TIME, getDefuseTime() + 1);
                        } else {
                            this.entityData.set(DEFUSE_TIME, getDefuseTime() + getTicksSinceLastInteraction());
                        }

                        this.entityData.set(TICKS_SINCE_LAST_INTERACTION, 0);

                        int defuseTimeLeft = Math.max(maxDefuseTime - getDefuseTime(), 0);

                        setCustomName(Component.nullToEmpty(Integer.toString(defuseTimeLeft)));
                        setCustomNameVisible(true);
                    }
                }
            }
        } else {
            if (pPlayer.isCrouching()) {
                if (!this.level().isClientSide) {
                    spawnAtLocation(new ItemStack(ModItems.C4.get(), 1));
//                    ItemEntity itemEntity = new ItemEntity(this.level(), this.position().x(), this.position().y(), this.position().z(), new ItemStack(ModItems.C4.get(), 1));
//                    this.level().addFreshEntity(itemEntity);
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

    private int getFuseTime() {
        return this.entityData.get(FUSE_TIME);
    }

    private int getTicksUntilBeep() {
        return this.entityData.get(TICKS_UNTIL_BEEP);
    }

    private int getTicksSinceLastInteraction() {
        return this.entityData.get(TICKS_SINCE_LAST_INTERACTION);
    }

    private int getDefuseTime() {
        return this.entityData.get(DEFUSE_TIME);
    }

    private boolean getDefused() {
        return this.entityData.get(DEFUSED);
    }
}

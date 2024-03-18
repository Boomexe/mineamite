package net.boomexe.mineamite.item.custom;

import net.boomexe.mineamite.entity.ModEntities;
import net.boomexe.mineamite.entity.custom.TimedC4Entity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class C4Item extends Item {
    public C4Item(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 80;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    // Runs on first tick
//    @Override
//    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
//        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
//    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        //Sound
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.AMBIENT, 5f, 1f);
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        super.finishUsingItem(pStack, pLevel, pLivingEntity);

        if (!pLevel.isClientSide) {
//            pLivingEntity.sendSystemMessage(Component.literal("Planted bomb!"));

            TimedC4Entity timedC4Entity = new TimedC4Entity(ModEntities.TIMED_C4.get(), pLevel);
            timedC4Entity.setPos(pLivingEntity.position());
            pLevel.addFreshEntity(timedC4Entity);

            pStack.setCount(pStack.getCount() - 1);
        }

        if (pLivingEntity instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        return pStack;
    }
}

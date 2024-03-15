package net.boomexe.mineamite.entity;

import net.boomexe.mineamite.MineamiteMod;
import net.boomexe.mineamite.entity.custom.TimedC4Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MineamiteMod.MOD_ID);

    public static final RegistryObject<EntityType<TimedC4Entity>> TIMED_C4 =
            ENTITY_TYPES.register("timed_c4",
                    () -> EntityType.Builder.of(TimedC4Entity::new, MobCategory.MISC)
                            .sized(1f, 0.3f)
                            .build(new ResourceLocation(MineamiteMod.MOD_ID, "timed_c4").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

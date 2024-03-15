package net.boomexe.mineamite.item.custom;

import net.boomexe.mineamite.MineamiteMod;
import net.boomexe.mineamite.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MineamiteMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MINEAMITE_TAB = CREATIVE_MODE_TABS.register("mineamite_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WIRE_CUTTERS.get()))
                    .title(Component.translatable("creativetab.mineamite_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.C4.get());
                        pOutput.accept(ModItems.WIRE_CUTTERS.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

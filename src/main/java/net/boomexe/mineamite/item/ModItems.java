package net.boomexe.mineamite.item;

import net.boomexe.mineamite.MineamiteMod;
import net.boomexe.mineamite.item.custom.C4Item;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MineamiteMod.MOD_ID);

    public static final RegistryObject<Item> C4 = ITEMS.register("c4",
            () -> new C4Item(new Item.Properties().rarity(Rarity.RARE).fireResistant().stacksTo(1)));

    public static final RegistryObject<Item> WIRE_CUTTERS = ITEMS.register("wire_cutters",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

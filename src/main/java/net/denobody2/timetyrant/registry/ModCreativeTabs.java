package net.denobody2.timetyrant.registry;

import net.denobody2.timetyrant.TimeTyrant;
import net.denobody2.timetyrant.common.item.DebugItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TimeTyrant.MOD_ID);
    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("timetyrant", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.TIME_SHARD.get()))
            .title(Component.translatable("itemGroup.timetyrant"))
            .displayItems((pParameters, pOutput) -> {
                for (var item : ModItems.ITEMS.getEntries()) {
                    if(!(item.get() instanceof DebugItem)){
                        pOutput.accept(item.get());
                    }
                }
                /*for (var block: ModBlocks.BLOCKS.getEntries()){
                    if(!(block.equals(ModBlocks.MANDARIN_WALL_HANGING_SIGN))){
                        if(!(block.equals(ModBlocks.MANDARIN_WALL_SIGN))){
                            pOutput.accept(block.get());
                        }
                    }
                }*/
            }).build());
}

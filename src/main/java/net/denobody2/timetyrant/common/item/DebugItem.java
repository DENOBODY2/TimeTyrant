package net.denobody2.timetyrant.common.item;

import com.github.alexthe666.citadel.server.tick.ServerTickRateTracker;
import com.github.alexthe666.citadel.server.tick.modifier.GlobalTickRateModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugItem extends Item {
    public DebugItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);
        if(!playerIn.getCooldowns().isOnCooldown(this)){
            modifyTickRate(playerIn, playerIn.isShiftKeyDown() ? 0.5F : 2, worldIn);
        }
        playerIn.getCooldowns().addCooldown(this, 15);
        return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, itemStackIn);
    }

    private void modifyTickRate(Player player, float multiplier, Level pLevel){
        if(pLevel.isClientSide){
            player.displayClientMessage(Component.translatable("message.timetyrant.universal", multiplier) , true);
        }
        ServerTickRateTracker.modifyTickRate(pLevel, new GlobalTickRateModifier(100, multiplier));
    }
}

package net.denobody2.timetyrant.common.item;

import com.github.alexthe666.citadel.server.tick.ServerTickRateTracker;
import com.github.alexthe666.citadel.server.tick.modifier.CelestialTickRateModifier;
import com.github.alexthe666.citadel.server.tick.modifier.LocalPositionTickRateModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class CelestialTimeRemoteItem extends Item {
    float tickMulti = 1F;
    int multiIndex = 0;
    UUID Dev = UUID.fromString("380df991-f603-344c-a090-369bad2a924a"); /*Dev*/
    UUID Denobody = UUID.fromString("3562ab33-f01b-4801-aab5-807f3750ded1"); /*AbysswalkerDeno*/
    public CelestialTimeRemoteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);
        if(!playerIn.getCooldowns().isOnCooldown(this)){
            if(playerIn.getUUID() == Denobody || playerIn.getUUID() == Dev || playerIn.getUUID().equals(Denobody) || playerIn.getUUID().equals(Dev)){
                if(!playerIn.isShiftKeyDown()){
                    modifyTickRate(playerIn, tickMulti, worldIn);

                    playerIn.getCooldowns().addCooldown(this, 5);

                } else {
                    if(worldIn.isClientSide){
                        multiIndex++;
                        if(multiIndex > 4){
                            multiIndex = 0;
                        }
                        switch (multiIndex) {
                            case 0 -> tickMulti = 50F;
                            case 1 -> tickMulti = 120F;
                            case 2 -> tickMulti = 500F;
                            case 3 -> tickMulti = 1800F;
                            case 4 -> tickMulti = 3600F;
                            default -> tickMulti = 1F;
                        }
                        playerIn.displayClientMessage(Component.translatable("message.timetyrant.cycle", tickMulti) , true);
                    }
                    playerIn.getCooldowns().addCooldown(this, 5);
                }
            } else {
                modifyTickRate(playerIn, playerIn.isShiftKeyDown()? 50F : 120F, worldIn);
                playerIn.getCooldowns().addCooldown(this, 200);
            }

        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, itemStackIn);
    }
    private void modifyTickRate(Player player, float multiplier, Level pLevel){
        if(pLevel.isClientSide){
            player.displayClientMessage(Component.translatable("message.timetyrant.remote.celestial", tickMulti) , true);
        }
        ServerTickRateTracker.modifyTickRate(pLevel, new CelestialTickRateModifier(100, multiplier));
    }
}

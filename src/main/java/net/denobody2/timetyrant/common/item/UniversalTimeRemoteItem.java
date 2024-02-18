package net.denobody2.timetyrant.common.item;

import com.github.alexthe666.citadel.server.entity.IModifiesTime;
import com.github.alexthe666.citadel.server.tick.ServerTickRateTracker;
import com.github.alexthe666.citadel.server.tick.modifier.GlobalTickRateModifier;
import com.github.alexthe666.citadel.server.tick.modifier.LocalEntityTickRateModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class UniversalTimeRemoteItem extends Item {
    float tickMulti = 1F;
    int multiIndex = 0;

    int ID;
    UUID Dev = UUID.fromString("380df991-f603-344c-a090-369bad2a924a"); /*Dev*/
    UUID Denobody = UUID.fromString("3562ab33-f01b-4801-aab5-807f3750ded1"); /*AbysswalkerDeno*/
    public UniversalTimeRemoteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        this.ID = playerIn.getId();
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);
            if(!playerIn.getCooldowns().isOnCooldown(this)){
                if(playerIn.getUUID() == Denobody || playerIn.getUUID() == Dev || playerIn.getUUID().equals(Denobody) || playerIn.getUUID().equals(Dev)){
                    if(!playerIn.isShiftKeyDown()){
                        modifyTickRateSpecial(playerIn, tickMulti, worldIn);
                        playerIn.getCooldowns().addCooldown(this, 5);
                    } else {
                        if(worldIn.isClientSide){
                            multiIndex++;
                            if(multiIndex > 6){
                                multiIndex = 0;
                            }
                            switch (multiIndex) {
                                case 0 -> tickMulti = 0.5F;
                                case 1 -> tickMulti = 0.25F;
                                case 2 -> tickMulti = 0.1F;
                                case 3 -> tickMulti = 2F;
                                case 4 -> tickMulti = 4F;
                                case 5 -> tickMulti = 10F;
                                case 6 -> tickMulti = 50F;
                                default -> tickMulti = 1F;
                            }
                            playerIn.displayClientMessage(Component.translatable("message.timetyrant.cycle", tickMulti) , true);
                        }
                        playerIn.getCooldowns().addCooldown(this, 5);
                    }
                } else {
                    modifyTickRate(playerIn, playerIn.isShiftKeyDown()? 0.5F : 2.0F, worldIn);
                    playerIn.getCooldowns().addCooldown(this, 300);
                }

            }
        return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, itemStackIn);
    }


    private void modifyTickRate(Player player, float multiplier, Level pLevel){
        if(pLevel.isClientSide){
            player.displayClientMessage(Component.translatable("message.timetyrant.universal", multiplier) , true);
        }
        ServerTickRateTracker.modifyTickRate(pLevel, new GlobalTickRateModifier(200, multiplier));
    }

    private void modifyTickRateSpecial(Player player, float multiplier, Level pLevel){
        if(pLevel.isClientSide){
            player.displayClientMessage(Component.translatable("message.timetyrant.universal", multiplier) , true);
        }
        ServerTickRateTracker.modifyTickRate(pLevel, new GlobalTickRateModifier(200, multiplier));
    }
}

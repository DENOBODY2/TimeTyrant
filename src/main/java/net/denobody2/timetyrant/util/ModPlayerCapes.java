package net.denobody2.timetyrant.util;

import com.github.alexthe666.citadel.client.rewards.CitadelCapes;
import net.denobody2.timetyrant.TimeTyrant;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModPlayerCapes {
    private static final ResourceLocation TIME_KNIGHT_CAPE = new ResourceLocation(TimeTyrant.MOD_ID, "textures/entity/cape/time_knight.png");
    private static final List<UUID> DEVS = List.of(
            UUID.fromString("380df991-f603-344c-a090-369bad2a924a"), /*Dev*/
            UUID.fromString("3562ab33-f01b-4801-aab5-807f3750ded1") /*AbysswalkerDeno*/
    );

    public static void setup() {
        CitadelCapes.addCapeFor(DEVS, "timetyrant.timeknight", TIME_KNIGHT_CAPE);
    }
}

package net.denobody2.timetyrant.util;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockStateProperties {
    public static final BooleanProperty ISACTIVE = BooleanProperty.create("isactive");
    public static final IntegerProperty TICKS = IntegerProperty.create("ticks", 0, 250);
}

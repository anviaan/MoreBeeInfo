package net.anvian.bee_info.mixin;

import net.anvian.bee_info.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin extends BaseEntityBlock {
    @Final
    @Shadow
    public static int MAX_HONEY_LEVELS;

    @Unique
    private final ChatFormatting moreBeeInfo$defaultFormatting = ChatFormatting.YELLOW;

    protected BeehiveBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> componentList, TooltipFlag tooltipFlag) {
        List<BeehiveBlockEntity.Occupant> beeData = itemStack.getOrDefault(DataComponents.BEES, List.of());
        BlockItemStateProperties blockState = itemStack.get(DataComponents.BLOCK_STATE);

        if (blockState != null && !blockState.isEmpty()) {
            moreBeeInfo$appendHoneyLevelText(componentList, blockState);
            moreBeeInfo$appendBeeCountText(componentList, beeData);
        }
    }

    @Unique
    private void moreBeeInfo$appendHoneyLevelText(List<Component> componentList, BlockItemStateProperties blockState) {
        try {
            int honeyLevel = Integer.parseInt(blockState.properties().getOrDefault("honey_level", "0"));
            MutableComponent levelText = Component.translatable("tooltip.honey").append(": ").append(honeyLevel + "/" + MAX_HONEY_LEVELS)
                    .withStyle(moreBeeInfo$defaultFormatting);
            componentList.add(levelText);
        } catch (NumberFormatException e) {
            Constants.LOG.error("Failed to parse honey level from block state: {}", blockState);
        }
    }

    @Unique
    private void moreBeeInfo$appendBeeCountText(List<Component> componentList, List<BeehiveBlockEntity.Occupant> beeData) {
        final int MAX_BEES = 3;
        MutableComponent beeText = Component.translatable("tooltip.bees").append(": ").append(beeData.size() + "/" + MAX_BEES)
                .withStyle(moreBeeInfo$defaultFormatting);
        componentList.add(beeText);
    }
}

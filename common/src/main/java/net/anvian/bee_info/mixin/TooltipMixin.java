package net.anvian.bee_info.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class TooltipMixin {
    @Unique
    private static final int TAG_COMPOUND = 10;
    @Unique
    private static final int TAG_STRING = 8;

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract CompoundTag getTag();

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void getTooltipdone(Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> list = cir.getReturnValue();
        if (this.isEmpty()) return;

        Item item = this.getItem();
        if (item != Items.BEEHIVE && item != Items.BEE_NEST) return;

        CompoundTag rootTag = this.getTag();
        if (rootTag == null) return;

        int honeyLevel = moreBeeInfo$parseHoneyLevel(rootTag.getCompound("BlockStateTag"));

        ListTag bees = rootTag.getCompound("BlockEntityTag").getList("Bees", TAG_COMPOUND);
        int beeCount = bees.size();

        Style yellow = Style.EMPTY.withColor(ChatFormatting.YELLOW);

        for (int i = 0; i < beeCount; i++) {
            CompoundTag entityData = bees.getCompound(i).getCompound("EntityData");
            if (entityData.contains("CustomName", TAG_STRING)) {
                String beeName = entityData.getString("CustomName");
                list.add(Math.min(1, list.size()), Component.literal("- " + Component.Serializer.fromJson(beeName).getString()).setStyle(yellow));
            }
        }

        String beesLabel = I18n.get("bee_info.tooltip.bees");
        list.add(Math.min(1, list.size()), Component.literal(beesLabel + ": " + beeCount).setStyle(yellow));

        String honeyLabel = I18n.get("bee_info.tooltip.honey");
        list.add(Math.min(1, list.size()), Component.literal(honeyLabel + ": " + honeyLevel + "/5").setStyle(yellow));
    }

    @Unique
    private static int moreBeeInfo$parseHoneyLevel(CompoundTag blockStateTag) {
        String honeyLevelStr = blockStateTag.getString("honey_level");
        if (honeyLevelStr.isEmpty()) return 0;
        try {
            return Integer.parseInt(honeyLevelStr);
        } catch (NumberFormatException e) {
            System.out.println("MoreBeeInfo: could not parse honey_level '" + honeyLevelStr + "'");
            return 0;
        }
    }
}
package net.anvian.bee_info.mixin;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class TooltipMixin {
    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract CompoundTag getTag();
    //TODO honey_level is not working
    @Inject(method = "getTooltipLines", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void getTooltipdone(Player playerIn, TooltipFlag advanced, CallbackInfoReturnable<List> ci, List<Component> list) {

        try {
            if (!this.isEmpty() && (this.getItem() == Items.BEEHIVE || this.getItem() == Items.BEE_NEST)) {
                CompoundTag tag = this.getTag();
                if (tag != null) {

                    int honeyLevel = tag.getCompound("BlockStateTag").getInt("honey_level");
                    String honeyLevelStr = tag.getCompound("BlockStateTag").getString("honey_level");
                    if (honeyLevelStr != null || !honeyLevelStr.isEmpty()) {
                        try {
                            honeyLevel = Integer.parseInt(honeyLevelStr);//honey level
                        } catch (NumberFormatException ex) {
                        }
                    }

                    ListTag bees = tag.getCompound("BlockEntityTag").getList("Bees", 10);
                    int beeCount = bees.size();//beeCount

                    for (int i = 0; i < beeCount; i++) {
                        tag = bees.getCompound(i).getCompound("EntityData");
                        if (tag != null && tag.contains("CustomName", 8)) {
                            String beeName = tag.getString("CustomName");
                            list.add(Math.min(1, list.size()), Component.nullToEmpty(I18n.get("tooltip.name", Component.Serializer.fromJson(beeName).getString())));
                        }
                    }

                    list.add(Math.min(1, list.size()), Component.nullToEmpty(I18n.get("tooltip.bees", beeCount)));
                    list.add(Math.min(1, list.size()), Component.nullToEmpty(I18n.get("tooltip.honey", honeyLevel)));
                }
            }
        } catch (NullPointerException ex) {
            System.out.println("NPE in getTooltipdone");
            Item item = this.getItem();
            if (item == null) {
                System.out.println("item is null");
            } else {
                System.out.println("item is " + this.getItem().getDescriptionId());
            }
        }
    }
}

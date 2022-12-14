package net.anvian.bee_info.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
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
    @Shadow public abstract Item getItem();
    @Shadow public abstract NbtCompound getNbt();

    @Inject(method="getTooltip", at=@At("RETURN"), locals=LocalCapture.CAPTURE_FAILHARD)
    private void getTooltipdone(PlayerEntity playerIn, TooltipContext advanced, CallbackInfoReturnable<List> ci, List<Text> list) {

        try {
            if (!this.isEmpty() && (this.getItem() == Items.BEEHIVE || this.getItem() == Items.BEE_NEST)) {
                NbtCompound tag = this.getNbt();
                if (tag != null) {

                    int honeyLevel = tag.getCompound("BlockStateTag").getInt("honey_level");
                    String honeyLevelStr = tag.getCompound("BlockStateTag").getString("honey_level");
                    if (honeyLevelStr != null || !honeyLevelStr.isEmpty()) {
                        try {
                            honeyLevel = Integer.parseInt(honeyLevelStr);//honey level
                        } catch (NumberFormatException ex) {
                        }
                    }

                    NbtList bees = tag.getCompound("BlockEntityTag").getList("Bees", 10);
                    int beeCount = bees.size();//beeCount

                    for (int i = 0; i < beeCount; i++)
                    {
                        tag = bees.getCompound(i).getCompound("EntityData");
                        if (tag != null && tag.contains("CustomName", 8))
                        {
                            String beeName = tag.getString("CustomName");
                            list.add(Math.min(1, list.size()), Text.of(I18n.translate("tooltip.name", Text.Serializer.fromJson(beeName).getString())));
                        }
                    }

                    list.add(Math.min(1, list.size()), Text.of(I18n.translate("tooltip.bees", beeCount)));
                    list.add(Math.min(1, list.size()), Text.of(I18n.translate("tooltip.honey", honeyLevel)));
                }
            }
        } catch (NullPointerException ex) {
            System.out.println("NPE in getTooltipdone");
            Item item = this.getItem();
            if (item == null) {
                System.out.println("item is null");
            } else {
                   System.out.println("item is "+this.getItem().getTranslationKey());
            }
        }
    }
}
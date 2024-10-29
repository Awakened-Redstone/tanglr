package nl.theepicblock.tanglr;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ItemEvents {
    public static void onInventoryTick(ItemStack stack, Level level, Entity entity, int inventorySlot, boolean isCurrentItem, CallbackInfo ci) {
        if (entity instanceof ServerPlayer pl && !TimeLogic.isStackValid(stack, level.getServer())) {
            if (pl.getInventory().getItem(inventorySlot) == stack) {
                pl.getInventory().setItem(inventorySlot, ItemStack.EMPTY);
                ci.cancel();
            }
        }
    }

    @SubscribeEvent
    public static void onUse(LivingEntityUseItemEvent.Start e) {
        if (!TimeLogic.isStackValid(e.getItem(), e.getEntity().getServer())) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onUseBlock(UseItemOnBlockEvent e) {
        if (!TimeLogic.isStackValid(e.getItemStack(), e.getLevel().getServer())) {
            if (e.getPlayer() != null) {
                if (e.getPlayer().getItemInHand(e.getHand()) == e.getItemStack()) {
                    e.getPlayer().setItemInHand(e.getHand(), ItemStack.EMPTY);
                }
            }
            e.cancelWithResult(ItemInteractionResult.FAIL);
        }
    }

    @SubscribeEvent
    public static void itemPickup(ItemEntityPickupEvent.Pre e) {
        if (!TimeLogic.isStackValid(e.getItemEntity().getItem(), e.getItemEntity().getServer())) {
            e.getItemEntity().setItem(ItemStack.EMPTY);
            e.getItemEntity().discard();
            e.setCanPickup(TriState.FALSE);
        }
    }
}
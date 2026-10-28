package de.example.autoelytra;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AutoElytra implements ClientModInitializer {
    private boolean hadElytra = false;
    private boolean initialized = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity p = client.player;
            if (p == null) { initialized = false; return; }

            ItemStack chest = p.getEquippedStack(EquipmentSlot.CHEST);
            boolean hasElytra = chest.isOf(Items.ELYTRA)
                    && chest.getDamage() < chest.getMaxDamage() - 1;

            if (initialized && hasElytra && !hadElytra
                    && !p.isOnGround()
                    && !p.isGliding()
                    && !p.isTouchingWater()
                    && !p.hasVehicle()
                    && !p.getAbilities().flying
                    && !p.hasStatusEffect(StatusEffects.LEVITATION)) {
                p.checkGliding();
            }

            hadElytra = hasElytra;
            initialized = true;
        });
    }
}

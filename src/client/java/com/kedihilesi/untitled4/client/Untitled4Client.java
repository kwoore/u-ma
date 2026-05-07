package com.kedihilesi.untitled4.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.phys.Vec3;

public class Untitled4Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Oyuncu veya dünya yüklenmemişse işlem yapma
            if (client.player == null || client.level == null) return;

            // Hayatta kalma veya Macera modunda mıyız kontrol et
            if (!client.player.isCreative() && !client.player.isSpectator()) {
                
                Vec3 velocity = client.player.getDeltaMovement();
                // Oyuncunun 2 blok altını kontrol et
                BlockPos posBelow = client.player.blockPosition().below(2);
                
                // Altımız hava değilse (yani zemin varsa)
                boolean isNearGround = !client.level.getBlockState(posBelow).isAir();

                // Eğer hızlı düşüyorsak (y < -0.5) ve yere yakınsak
                if (velocity.y < -0.5 && isNearGround) {
                    
                    Abilities abilities = client.player.getAbilities();
                    abilities.mayfly = true; // 26.1.2 Mapping: Uçma izni
                    abilities.flying = true; // Uçuşu başlat

                    // Sunucuya yeteneklerin güncellendiğini bildir (Yeni sürümlerde metod ismi kontrolü)
                    client.player.onUpdateAbilities();

                    // 26.1.2 Fix: setDeltaMovement artık yeni bir Vec3 objesi bekler
                    client.player.setDeltaMovement(new Vec3(velocity.x, 0.0, velocity.z));
                }
            }
        });
    }
}

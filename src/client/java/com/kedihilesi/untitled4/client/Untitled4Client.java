package com.kedihilesi.untitled4.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class Untitled4Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // İstemci tarafı güvenlik kontrolleri
            if (client.player == null || client.level == null) return;

            // Yaratıcı modda değilsek otomatik kurtarmayı çalıştır
            if (!client.player.isCreative() && !client.player.isSpectator()) {
                
                Vec3 velocity = client.player.getDeltaMovement();
                // 26.1.2 Official Mapping: blockPosition().below(2)
                BlockPos posBelow = client.player.blockPosition().below(2);
                
                // Zemin kontrolü
                boolean isNearGround = !client.level.getBlockState(posBelow).isAir();

                // Düşüş hızı eşiği ve mesafe kontrolü
                if (velocity.y < -0.5 && isNearGround) {
                    
                    var abilities = client.player.getAbilities();
                    abilities.mayfly = true; // Uçma yeteneğini ver
                    abilities.flying = true; // Uçuşu başlat

                    // Yetenek değişimini sunucuya ve yerel oyuncuya bildir
                    client.player.onUpdateAbilities();

                    // Dikey hızı sıfırla (Çakılmayı önle), yatay ivmeyi koru
                    client.player.setDeltaMovement(velocity.x, 0.0, velocity.z);
                }
            }
        });
    }
}

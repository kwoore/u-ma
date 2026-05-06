package com.kedihilesi.untitled4.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * Minecraft 26.1.2 (Tiny Takeover) Sürümü için güncellenmiştir.
 * Bu sürümde Java 25 ve resmi Mojang mappingleri kullanılmaktadır.
 */
public class Untitled4Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // İstemci tarafında oyuncu ve dünya kontrolü
            if (client.player == null || client.level == null) return;

            // Bebek mobların bol olduğu bu sürümde, hayatta kalma modundaysak kontrol et
            if (!client.player.isCreative() && !client.player.isSpectator()) {

                // Oyuncunun hareket verisini al (26.1.x sürümünde Vec3 kullanımı aynıdır)
                Vec3 velocity = client.player.getDeltaMovement();

                // Oyuncunun 2 blok altını kontrol et
                BlockPos posBelow = client.player.blockPosition().below(2);

                // 26.1.2 ile gelen yeni blok kontrolü: Hava değilse ve yere yaklaşıyorsak
                boolean isNearGround = !client.level.getBlockState(posBelow).isAir();

                // Eğer düşme hızı kritik seviyedeyse (-0.5 altı) ve yere yakınsak
                if (velocity.y < -0.5 && isNearGround) {

                    // Yetenekleri güncelle (Official Mapping: mayfly ve flying)
                    var abilities = client.player.getAbilities();
                    abilities.mayfly = true;
                    abilities.flying = true;

                    // Sunucuyla yetenekleri senkronize et
                    client.player.onUpdateAbilities();

                    // Y eksenindeki düşüş ivmesini sıfırla, yatay hızı (X ve Z) koru
                    client.player.setDeltaMovement(velocity.x, 0.0, velocity.z);

                    /*
                     * Not: 26.1.2 sürümünde anti-cheat sistemleri bebek mobların
                     * hit-box değişiklikleri nedeniyle güncellendiği için
                     * bu işlem bazı sunucularda "Flight Kick" sebebi olabilir.
                     */
                }
            }
        });
    }
}
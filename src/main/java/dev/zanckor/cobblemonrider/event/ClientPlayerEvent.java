package dev.zanckor.cobblemonrider.event;

import dev.zanckor.cobblemonrider.CobblemonRider;
import dev.zanckor.cobblemonrider.network.NetworkUtil;
import dev.zanckor.cobblemonrider.network.packet.KeyPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

import static dev.zanckor.cobblemonrider.CobblemonRider.MODID;


@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientPlayerEvent {

    @SubscribeEvent
    public static void tickEvent(TickEvent.ClientTickEvent e) {
        Player player = Minecraft.getInstance().player;
        Options options = Minecraft.getInstance().options;

        if (options.keyJump.isDown()) {
            NetworkUtil.TO_SERVER(new KeyPacket(KeyPacket.Key.SPACE));
            Objects.requireNonNull(player).getPersistentData().putBoolean("press_space", true);
        }

        if (options.keySprint.isDown()) {
            NetworkUtil.TO_SERVER(new KeyPacket(KeyPacket.Key.SPRINT));
            Objects.requireNonNull(player).getPersistentData().putBoolean("press_sprint", true);
        }

        if (CobblemonRider.ClientEventHandlerRegister.pokemonDismount.isDown()) {
            NetworkUtil.TO_SERVER(new KeyPacket(KeyPacket.Key.POKEMON_DISMOUNT));
            Objects.requireNonNull(player).getPersistentData().putBoolean("pokemon_dismount", true);
        }
    }
}

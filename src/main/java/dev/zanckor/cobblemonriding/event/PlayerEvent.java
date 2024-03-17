package dev.zanckor.cobblemonriding.event;

import dev.zanckor.cobblemonriding.network.NetworkUtil;
import dev.zanckor.cobblemonriding.network.packet.KeyEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

import static dev.zanckor.cobblemonriding.CobblemonRiding.MODID;


@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerEvent {

    @SubscribeEvent
    public static void onPlayerJump(TickEvent.ClientTickEvent e) {
        Player player = Minecraft.getInstance().player;

        if (Minecraft.getInstance().options.keyJump.isDown()) {
            NetworkUtil.TO_SERVER(new KeyEvent());
            Objects.requireNonNull(player).getPersistentData().putBoolean("press_space", true);
        }
    }
}

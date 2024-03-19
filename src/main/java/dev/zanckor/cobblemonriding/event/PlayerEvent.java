package dev.zanckor.cobblemonriding.event;

import com.cobblemon.mod.common.Cobblemon;
import dev.zanckor.cobblemonriding.CobblemonRiding;
import dev.zanckor.cobblemonriding.network.NetworkUtil;
import dev.zanckor.cobblemonriding.network.packet.KeyEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
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
        Options options = Minecraft.getInstance().options;

        if (options.keyJump.isDown()) {
            NetworkUtil.TO_SERVER(new KeyEvent(KeyEvent.Key.SPACE));
            Objects.requireNonNull(player).getPersistentData().putBoolean("press_space", true);
        }

        if(options.keySprint.isDown()) {
            NetworkUtil.TO_SERVER(new KeyEvent(KeyEvent.Key.SPRINT));
            Objects.requireNonNull(player).getPersistentData().putBoolean("press_sprint", true);
        }

        if(CobblemonRiding.ClientEventHandlerRegister.pokemonDismount.isDown()){
            NetworkUtil.TO_SERVER(new KeyEvent(KeyEvent.Key.POKEMON_DISMOUNT));
            Objects.requireNonNull(player).getPersistentData().putBoolean("pokemon_dismount", true);
        }
    }
}

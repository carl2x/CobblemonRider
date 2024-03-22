package dev.zanckor.cobblemonrider.event;

import com.google.gson.Gson;
import dev.zanckor.cobblemonrider.CobblemonRider;
import dev.zanckor.cobblemonrider.config.PokemonJsonObject;
import dev.zanckor.cobblemonrider.network.NetworkUtil;
import dev.zanckor.cobblemonrider.network.packet.ConfigPacket;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static dev.zanckor.cobblemonrider.CobblemonRider.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerPlayerEvent {

    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent e) {
        NetworkUtil.TO_CLIENT(e.getEntity(), new ConfigPacket(loadConfig()));
    }

    public static PokemonJsonObject loadConfig() {
        File pokemonRideConfigFile = CobblemonRider.PokemonRideConfigFile;
        String pokemonRideConfig = null;

        try {
            if (pokemonRideConfigFile != null)
                pokemonRideConfig = new String(Files.readAllBytes(pokemonRideConfigFile.toPath()));
        } catch (IOException e) {
            CobblemonRider.LOGGER.info("Error reading cobblemon pokemon ride config file" + pokemonRideConfigFile);

            return null;
        }

        return pokemonRideConfig != null ? new Gson().fromJson(pokemonRideConfig, PokemonJsonObject.class) : null;
    }
}

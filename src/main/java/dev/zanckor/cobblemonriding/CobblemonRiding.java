package dev.zanckor.cobblemonriding;

import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import dev.zanckor.cobblemonriding.config.PokemonJsonObject;
import dev.zanckor.cobblemonriding.network.NetworkHandler;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.zanckor.cobblemonriding.config.PokemonJsonObject.MountType.*;

@Mod(CobblemonRiding.MODID)
public class CobblemonRiding {
    public static final String MODID = "cobblemonriding";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static File PokemonRideConfigFile;

    public CobblemonRiding() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        NetworkHandler.register();
    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class FolderManager {

        /**
         * Each time that server starts <code> serverFolderManager </code> is called to create json files
         */
        @SubscribeEvent
        public static void serverFolderManager(ServerAboutToStartEvent e) {
            Path serverDirectory = e.getServer().getWorldPath(LevelResource.ROOT).toAbsolutePath();
            File pokemonRideConfig = Paths.get(serverDirectory.toString(), "serverconfig\\pokemonRideConfig.json").toFile();
            PokemonRideConfigFile = pokemonRideConfig;

            PokemonJsonObject pokemonJsonObject = new PokemonJsonObject();

            pokemonJsonObject.add("Bulbasaur",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 0.0f, 0.0f))));

            pokemonJsonObject.add("Charizard",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, FLY)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Blastoise",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Pidgeot",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, FLY)),
                            new ArrayList<>(Arrays.asList(0.0f, 0.7f, 0.0f))));

            pokemonJsonObject.add("Fearow",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, FLY)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Nidoking",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Ninetales",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 0.0f, 0.0f))));

            pokemonJsonObject.add("Arcanine",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Tentacruel",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.5f, 0.0f))));

            pokemonJsonObject.add("Rapidash",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 0.8f, 0.0f))));

            pokemonJsonObject.add("Magnezone",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Dodrio",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.2f, 0.0f))));

            pokemonJsonObject.add("Dewgong",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 0.0f, 0.0f))));

            pokemonJsonObject.add("Exeggutor",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 3.0f, 0.0f))));

            pokemonJsonObject.add("Tauros",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 2.0f, 0.0f))));

            pokemonJsonObject.add("Gyarados",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 0.0f, 0.0f))));

            pokemonJsonObject.add("Lapras",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 0.0f, 0.0f))));

            pokemonJsonObject.add("Aerodactyl",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, FLY)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Dragonite",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, FLY)),
                            new ArrayList<>(Arrays.asList(0.0f, 2.0f, 0.0f))));

            pokemonJsonObject.add("Meganium",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 2.0f, 0.0f))));

            pokemonJsonObject.add("Feraligatr",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 2.0f, 0.0f))));

            pokemonJsonObject.add("Steelix",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 6.0f, 0.0f))));

            pokemonJsonObject.add("Stantler",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Swampert",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Mightyena",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Sharpedo",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Wailmer",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 2.0f, 0.0f))));

            pokemonJsonObject.add("Camerupt",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 2.0f, 0.0f))));

            pokemonJsonObject.add("Relicanth",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Metagross",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 2.0f, 0.0f))));

            pokemonJsonObject.add("Staraptor",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, FLY)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Luxray",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Garchomp",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 2.0f, 0.0f))));

            pokemonJsonObject.add("Yanmega",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(FLY)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Mamoswine",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 5.0f, 0.0f))));

            pokemonJsonObject.add("Samurott",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 2.0f, 0.0f))));

            pokemonJsonObject.add("Stoutland",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Scolipede",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Sawsbuck",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Golurk",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 5.0f, 0.0f))));

            pokemonJsonObject.add("Bouffalant",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 2.0f, 0.0f))));

            pokemonJsonObject.add("Avalugg",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, SWIM)),
                            new ArrayList<>(Arrays.asList(0.0f, 3.0f, 0.0f))));

            pokemonJsonObject.add("Mudsdale",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 2.0f, 0.0f))));

            pokemonJsonObject.add("Corviknight",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK, FLY)),
                            new ArrayList<>(Arrays.asList(0.0f, 3.0f, 0.0f))));

            pokemonJsonObject.add("Wyrdeer",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Ursaluna",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Skeledirge",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            pokemonJsonObject.add("Espahtla",
                    new PokemonJsonObject.PokemonConfigData(
                            new ArrayList<>(List.of(WALK)),
                            new ArrayList<>(Arrays.asList(0.0f, 1.0f, 0.0f))));

            if(pokemonRideConfig.exists()) {
                System.out.println("Cobblemon pokemon ride config file already exists at " + pokemonRideConfig);
            } else {
                try (FileWriter file = new FileWriter(pokemonRideConfig)) {
                    file.write(new GsonBuilder().setPrettyPrinting().create().toJson(pokemonJsonObject));
                    System.out.println("File created: " + pokemonRideConfig.getName());

                    System.out.println("Cobblemon pokemon ride config file created at " + pokemonRideConfig);
                } catch (
                        IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Error creating cobblemon pokemon ride config file" + pokemonRideConfig);
                }
            }
        }
    }
}
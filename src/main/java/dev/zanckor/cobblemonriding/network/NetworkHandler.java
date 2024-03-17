package dev.zanckor.cobblemonriding.network;


import dev.zanckor.cobblemonriding.network.packet.KeyEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static dev.zanckor.cobblemonriding.CobblemonRiding.MODID;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );


    public static void register() {
        int index = 0;

        CHANNEL.messageBuilder(KeyEvent.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(KeyEvent::encodeBuffer).decoder(KeyEvent::new)
                .consumerNetworkThread(KeyEvent::handler).add();
    }
}
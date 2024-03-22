package dev.zanckor.cobblemonrider.network;


import dev.zanckor.cobblemonrider.network.packet.ConfigPacket;
import dev.zanckor.cobblemonrider.network.packet.KeyPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static dev.zanckor.cobblemonrider.CobblemonRider.MODID;

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

        CHANNEL.messageBuilder(KeyPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(KeyPacket::encodeBuffer).decoder(KeyPacket::new)
                .consumerNetworkThread(KeyPacket::handler).add();


        CHANNEL.messageBuilder(ConfigPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ConfigPacket::encodeBuffer).decoder(ConfigPacket::new)
                .consumerNetworkThread(ConfigPacket::handler).add();
    }
}
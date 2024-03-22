package dev.zanckor.cobblemonrider.network.packet;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.google.gson.Gson;
import dev.zanckor.cobblemonrider.config.PokemonJsonObject;
import dev.zanckor.cobblemonrider.network.ClientHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ConfigPacket {
    private final PokemonJsonObject jsonObject;

    public ConfigPacket(PokemonJsonObject config) {
        this.jsonObject = config;
    }

    public ConfigPacket(FriendlyByteBuf buffer) {
        jsonObject = new Gson().fromJson(buffer.readUtf(), PokemonJsonObject.class);
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeUtf(new Gson().toJson(this.jsonObject));
    }

    public static void handler(ConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.saveConfigObject(msg.jsonObject));
        });
    }
}

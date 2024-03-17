package dev.zanckor.cobblemonriding.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class KeyEvent {


    public KeyEvent() {
    }

    public KeyEvent(FriendlyByteBuf buffer) {
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
    }


    public static void handler(KeyEvent msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();

            CompoundTag tag = Objects.requireNonNull(player).getPersistentData();
            tag.putBoolean("press_space", true);
        });

        ctx.get().setPacketHandled(true);
    }
}

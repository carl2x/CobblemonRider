package dev.zanckor.cobblemonriding.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class KeyEvent {
    private final Key key;

    public KeyEvent(Key key) {
        this.key = key;
    }

    public KeyEvent(FriendlyByteBuf buffer) {
        key = buffer.readEnum(Key.class);
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.key);
    }

    public static void handler(KeyEvent msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();

            CompoundTag tag = Objects.requireNonNull(player).getPersistentData();

            switch (msg.key) {
                case SPACE -> tag.putBoolean("press_space", true);
                case SPRINT -> tag.putBoolean("press_sprint", true);
                case POKEMON_DISMOUNT -> tag.putBoolean("pokemon_dismount", true);
            }
        });

        ctx.get().setPacketHandled(true);
    }

    public enum Key {
        SPACE,
        SPRINT,
        POKEMON_DISMOUNT
    }
}

package dev.zanckor.cobblemonriding.mixin;


import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends Entity {

    public PlayerMixin(EntityType<?> type, Level world) {
        super(type, world);
    }


    @Inject(method = "removeVehicle", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("ConstantConditions")
    public void shouldDismount(CallbackInfo ci) {
        Entity vehicle = this.getVehicle();

        if (vehicle instanceof PokemonEntity && getDistanceToSurface(vehicle) < 0 && !vehicle.isRemoved()) {
            ci.cancel();
        }
    }

    float getDistanceToSurface(Entity entity) {
        double yPos = entity.getY();
        double surfaceYPos = entity.level().getHeight(Heightmap.Types.WORLD_SURFACE_WG, (int) entity.getX(), (int) entity.getZ());

        return (float) (surfaceYPos - yPos);
    }
}

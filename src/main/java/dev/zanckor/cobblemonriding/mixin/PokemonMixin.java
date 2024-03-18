package dev.zanckor.cobblemonriding.mixin;


import com.cobblemon.mod.common.api.entity.PokemonSideDelegate;
import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import dev.zanckor.cobblemonriding.CobblemonRiding;
import dev.zanckor.cobblemonriding.config.PokemonJsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import static dev.zanckor.cobblemonriding.config.PokemonJsonObject.MountType.*;

@Mixin(PokemonEntity.class)
public abstract class PokemonMixin extends TamableAnimal implements PlayerRideableJumping {
    Gson gson = new Gson().newBuilder().create();
    PokemonJsonObject.PokemonConfigData passengerObject;
    @Shadow
    private Pokemon pokemon;

    @Shadow
    public abstract boolean getIsSubmerged();

    @Shadow
    public abstract PokemonSideDelegate getDelegate();

    private Player passenger;

    protected PokemonMixin(EntityType<? extends ShoulderRidingEntity> p_29893_, Level p_29894_) {
        super(p_29893_, p_29894_);
    }


    @Inject(method = "travel", at = @At("HEAD"))
    private void travel(Vec3 pos, CallbackInfo ci) {
        if (passengerObject != null && passenger != null) {
            travelHandler(pos);

            if (passengerObject.getMountTypes().contains(SWIM))
                swimmingHandler();

            if (passengerObject.getMountTypes().contains(FLY))
                flyingHandler();

            passenger.getPersistentData().putBoolean("press_space", false);
            passenger.getPersistentData().putBoolean("press_sprint", false);
        } else {
            super.travel(pos);
        }
    }

    void travelHandler(Vec3 pos) {
        float modifierSpeed = passengerObject.getSpeedModifier();

        // Set the entity's yaw and pitch from the passenger's yaw and pitch
        setYHeadRot(passenger.getYHeadRot());
        setRot(passenger.getYRot(), passenger.getXRot());

        float x = (float) passenger.getDeltaMovement().x * 10;
        float z = (float) passenger.getDeltaMovement().z * 10;

        if ((passengerObject.getMountTypes().contains(SWIM) && isInWater())
                || (passengerObject.getMountTypes().contains(FLY) && !onGround())
                || (passengerObject.getMountTypes().contains(WALK))) {
            if (passengerObject.getMountTypes().contains(WALK) && onGround()) {


                if (isSpacePressed()){
                    jumpFromGround();
                }

                if (isSprintPressed()) {
                    modifierSpeed *= 1.5f;
                }
            }

            setDeltaMovement(x * modifierSpeed, getDeltaMovement().y, z * modifierSpeed);
        }

        super.travel(new Vec3(x, pos.y, z));
    }

    void swimmingHandler() {
        if (isInWater()) {
            if (getDelegate() instanceof PokemonClientDelegate) {
                ((PokemonClientDelegate) getDelegate()).setPose("swim");
            }

            double waterEmergeSpeed = isSpacePressed() ? 0.5 : passenger.isShiftKeyDown() ? -0.25 : 0.02;
            setAirSupply(getMaxAirSupply());
            passenger.setAirSupply(passenger.getMaxAirSupply());

            setDeltaMovement(getDeltaMovement().x, waterEmergeSpeed, getDeltaMovement().z);

            if(getDistanceToSurface(this) <= 0.5 && passenger.isShiftKeyDown()) {
                moveTo(getX(), getY() - 0.1, getZ());
            }
        }
    }

    void flyingHandler() {
        boolean increaseAltitude = isSpacePressed();
        boolean decreaseAltitude = passenger.isShiftKeyDown();

        if (!onGround() || increaseAltitude) {
            double altitudeIncreaseValue = increaseAltitude ? 0.3 : decreaseAltitude ? -0.3 : 0.025;
            setDeltaMovement(getDeltaMovement().x, altitudeIncreaseValue, getDeltaMovement().z);
        }

        if (getDelegate() instanceof PokemonClientDelegate) {
            ((PokemonClientDelegate) getDelegate()).setPose("swim");
        }
    }

    private PokemonJsonObject.PokemonConfigData getPassengerObject(String pokemonType) {
        // Obtain the passenger object from the config
        File pokemonRideConfigFile = CobblemonRiding.PokemonRideConfigFile;
        String pokemonRideConfig = null;

        try {
            if (pokemonRideConfigFile != null)
                pokemonRideConfig = new String(Files.readAllBytes(pokemonRideConfigFile.toPath()));
        } catch (IOException e) {
            CobblemonRiding.LOGGER.info("Error reading cobblemon pokemon ride config file" + pokemonRideConfigFile);
            
            return null;
        }

        if (pokemonRideConfig != null) {
            PokemonJsonObject pokemonJsonObject = gson.fromJson(pokemonRideConfig, PokemonJsonObject.class);

            if (pokemonJsonObject != null) {
                // Check if Pokemon is in the list of pokemon that can be mounted
                for (String translationKey : pokemonJsonObject.getPokemonIDs()) {
                    if (translationKey.equalsIgnoreCase(pokemonType)) {
                        return pokemonJsonObject.getPokemonData(translationKey);
                    }
                }
            }
        }

        return null;
    }

    Vec3 getPassengerPositionOffset(Entity passenger) {
        // If the passenger is a player, update their position
        if (this.passenger != null && this.passenger.equals(passenger)) {
            // Get the offsets from the config
            float xOffset = passengerObject != null ? passengerObject.getOffSet().get(0) : 0;
            float yOffset = passengerObject != null ? passengerObject.getOffSet().get(1) : 0;
            float zOffset = passengerObject != null ? passengerObject.getOffSet().get(2) : 0;

            return new Vec3(xOffset, yOffset, zOffset);
        }

        return null;
    }

    float getDistanceToSurface(Entity entity) {
        double yPos = entity.getY();
        double surfaceYPos = entity.level().getHeight(Heightmap.Types.WORLD_SURFACE_WG, (int) (entity.getX() - entity.getEyeHeight()), (int) entity.getZ());

        return (float) (surfaceYPos - yPos);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (!isAlive() || !isAddedToWorld() || isRemoved()) {
            ejectPassengers();
            removeVehicle();
        }

        if (passenger != null) {
            Vec3 offset = getPassengerPositionOffset(passenger) != null ? getPassengerPositionOffset(passenger) : Vec3.ZERO;

            // Update the passenger's position
            double xPos = getX() + offset.x;
            double yPos = getY() + offset.y;
            double zPos = getZ() + offset.z;

            passenger.moveTo(xPos, yPos, zPos);

            if ((passenger.isShiftKeyDown() && getDistanceToSurface(this) >= 0 && !isInWater()) || (getPassengers().isEmpty()) && passenger != null) {
                passenger.stopRiding();
                passenger = null;
            }
        }
    }

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    public void causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }


    @Inject(method = "mobInteract", at = @At("HEAD"))
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        // On player interaction, if the player is not already riding the entity, add the player as a passenger
        if (canAddPassenger(player) && Objects.equals(this.pokemon.getOwnerPlayer(), player)) {
            passengerObject = getPassengerObject(pokemon.getSpecies().getName());

            if (passengerObject != null) {
                player.startRiding(this);
                passenger = player;
            }
        }
    }


    private boolean isSpacePressed() {
        return passenger != null && passenger.getPersistentData().contains("press_space") && passenger.getPersistentData().getBoolean("press_space");
    }

    private boolean isSprintPressed() {
        return passenger != null && passenger.getPersistentData().contains("press_sprint") && passenger.getPersistentData().getBoolean("press_sprint");
    }
}
package dev.zanckor.cobblemonrider.mixin;


import com.cobblemon.mod.common.api.entity.PokemonSideDelegate;
import com.cobblemon.mod.common.entity.EntityProperty;
import com.cobblemon.mod.common.entity.pokemon.PokemonBehaviourFlag;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import dev.zanckor.cobblemonrider.CobblemonRider;
import dev.zanckor.cobblemonrider.MCUtil;
import dev.zanckor.cobblemonrider.config.PokemonJsonObject;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static dev.zanckor.cobblemonrider.config.PokemonJsonObject.MountType.*;

@Mixin(PokemonEntity.class)
public abstract class PokemonMixin extends TamableAnimal {
    Gson gson = new Gson().newBuilder().create();
    PokemonJsonObject.PokemonConfigData passengerObject;

    @Shadow
    public abstract PokemonSideDelegate getDelegate();

    @Shadow
    public abstract EntityProperty<Boolean> isMoving();

    @Shadow
    public abstract Pokemon getPokemon();

    @Shadow
    public abstract void checkDespawn();

    private Player passenger;

    protected PokemonMixin(EntityType<? extends ShoulderRidingEntity> p_29893_, Level p_29894_) {
        super(p_29893_, p_29894_);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lcom/cobblemon/mod/common/pokemon/Pokemon;Lnet/minecraft/world/entity/EntityType;)V", at = @At("RETURN"))
    private void init(Level level, Pokemon pokemon, EntityType<? extends PokemonEntity> entityType, CallbackInfo ci) {
        this.setMaxUpStep(1.0F);
    }


    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        dismountHandler();
        movementHandler();
    }

    @Override
    protected void positionRider(Entity entity, MoveFunction moveFunction) {
        if (this.hasPassenger(entity)) {
            PokemonJsonObject.PokemonConfigData passengerObject = MCUtil.getPassengerObject(getPokemon().getSpecies().getName());
            ArrayList<Float> offSet = passengerObject != null ? passengerObject.getOffSet() : new ArrayList<>(Arrays.asList(0.0f, 0.0f, 0.0f));
            
            double d0 = this.getY();
            moveFunction.accept(entity, this.getX() + offSet.get(0), d0 + offSet.get(1), this.getZ() + offSet.get(2));
        }
    }

    private void movementHandler() {

        if (passengerObject != null && passenger != null) {
            travelHandler();

            if (passengerObject.getMountTypes().contains(SWIM)) {
                swimmingHandler();
            }

            if (passengerObject.getMountTypes().contains(FLY)) {
                flyingHandler();
            }


            if (passenger != null) {
                passenger.getPersistentData().putBoolean("press_space", false);
                passenger.getPersistentData().putBoolean("press_sprint", false);
                passenger.getPersistentData().putBoolean("pokemon_dismount", false);
            }
        }
    }

    void travelHandler() {
        float modifierSpeed = passengerObject.getSpeedModifier();

        // Set the entity's yaw and pitch from the passenger's yaw and pitch
        setYHeadRot(passenger.getYHeadRot());
        setRot(passenger.getYRot(), passenger.getXRot());

        float x = (float) passenger.getDeltaMovement().x * 10;
        float z = (float) passenger.getDeltaMovement().z * 10;

        if ((passengerObject.getMountTypes().contains(SWIM) && isInWater()) // Not pretty clear code, should be refactored
                || (passengerObject.getMountTypes().contains(FLY) && !onGround())
                || (passengerObject.getMountTypes().contains(WALK))) {
            if (passengerObject.getMountTypes().contains(WALK) && onGround()) {
                if (isSpacePressed()) {
                    jumpFromGround();
                }
            }

            if (passenger.isShiftKeyDown()) {
                modifierSpeed *= 0.3f;
            }

            setDeltaMovement(x * modifierSpeed, getDeltaMovement().y, z * modifierSpeed);
        }

        travel(new Vec3(x, 0, z));
    }

    void swimmingHandler() {
        if (isInWater()) {
            double waterEmergeSpeed = isSpacePressed() ? 0.5 : isDescendPressed() ? -0.25 : 0.02;
            setAirSupply(getMaxAirSupply());
            passenger.setAirSupply(passenger.getMaxAirSupply());

            setDeltaMovement(getDeltaMovement().x, waterEmergeSpeed, getDeltaMovement().z);

            if (getDistanceToSurface(this) <= 0.5 && isDescendPressed()) {
                moveTo(getX(), getY() - 0.1, getZ());
            }
        }
    }

    void flyingHandler() {
        boolean increaseAltitude = isSpacePressed();
        boolean decreaseAltitude = isDescendPressed();

        if ((!onGround() || increaseAltitude) && getPokemon().getEntity() != null) {
            double altitudeIncreaseValue = increaseAltitude ? 0.3 : decreaseAltitude ? -0.3 : -0.0005;
            setDeltaMovement(getDeltaMovement().x, altitudeIncreaseValue, getDeltaMovement().z);
            getPokemon().getEntity().setBehaviourFlag(PokemonBehaviourFlag.FLYING, true);
        }

        if (onGround() && getPokemon().getEntity() != null) {
            getPokemon().getEntity().setBehaviourFlag(PokemonBehaviourFlag.FLYING, false);
        }
    }

    float getDistanceToSurface(Entity entity) {
        double yPos = entity.getY();
        double surfaceYPos = entity.level().getHeight(Heightmap.Types.WORLD_SURFACE, (int) (entity.getX() - entity.getEyeHeight()), (int) entity.getZ());

        return (float) (surfaceYPos - yPos);
    }

    public void dismountHandler() {
        if (!isAlive() || !isAddedToWorld() || isRemoved()) {
            ejectPassengers();
        }

        if (passenger != null && checkShouldDismount()) {
            passenger.stopRiding();
            ejectPassengers();
            passenger = null;
        }
    }

    public boolean checkShouldDismount() {
        return ((isPokemonDismountPressed()) || (getPassengers().isEmpty()) ||
                (!passengerObject.getMountTypes().contains(SWIM) && isInWater()));
    }

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    public void causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }


    @Inject(method = "mobInteract", at = @At("HEAD"))
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        // On player interaction, if the player is not already riding the entity, add the player as a passenger
        if (canAddPassenger(player) && Objects.equals(getPokemon().getOwnerPlayer(), player)) {
            passengerObject = MCUtil.getPassengerObject(getPokemon().getSpecies().getName());

            if (passengerObject != null) {
                player.startRiding(this);
                passenger = player;

                player.getPersistentData().putBoolean("press_space", false);
                player.getPersistentData().putBoolean("press_sprint", false);
                player.getPersistentData().putBoolean("pokemon_dismount", false);
            }
        }
    }

    @Inject(method = "isMoving", at = @At("RETURN"), cancellable = true, remap = false)
    public void isMoving(CallbackInfoReturnable<EntityProperty<Boolean>> cir) {
        if (passenger != null) {
            EntityProperty<Boolean> property = cir.getReturnValue();
            boolean isMoving = getDeltaMovement().x != 0 || getDeltaMovement().z != 0;
            property.set(isMoving);


            cir.setReturnValue(property);
        }
    }


    private boolean isSpacePressed() {
        return passenger != null && passenger.getPersistentData().contains("press_space") && passenger.getPersistentData().getBoolean("press_space");
    }

    private boolean isDescendPressed() {
        return passenger != null && passenger.getPersistentData().contains("press_sprint") && passenger.getPersistentData().getBoolean("press_sprint");
    }

    private boolean isPokemonDismountPressed() {
        return passenger != null && passenger.getPersistentData().contains("pokemon_dismount") && passenger.getPersistentData().getBoolean("pokemon_dismount");
    }
}
package dev.zanckor.cobblemonriding.config;


import java.util.*;

public class PokemonJsonObject {

    Map<String, PokemonConfigData> pokemonTypes = new HashMap<>();

    public PokemonJsonObject() {
    }

    public void add(String pokemonType, PokemonConfigData pokemonConfigData) {
        pokemonTypes.put(pokemonType, pokemonConfigData);
    }


    public static class PokemonConfigData {
        ArrayList<MountType> mountType;

        ArrayList<Float> offSet;
        float speedModifier = 1;

        public PokemonConfigData(ArrayList<MountType> mountType, ArrayList<Float> offSet) {
            this.mountType = mountType;
            this.offSet = offSet;
        }

        public PokemonConfigData(ArrayList<MountType> mountType) {
            this.mountType = mountType;
            this.offSet = new ArrayList<>(List.of(0.0f, 0.0f, 0.0f));
        }

        public PokemonConfigData() {
            this.mountType = new ArrayList<>(List.of(MountType.WALK));
            this.offSet = new ArrayList<>(List.of(0.0f, 0.0f, 0.0f));
        }

        public ArrayList<MountType> getMountTypes() {
            return mountType;
        }

        public ArrayList<Float> getOffSet() {
            return offSet;
        }

        public float getSpeedModifier() {
            return speedModifier;
        }
    }


    public Set<String> getPokemonIDs() {
        return pokemonTypes.keySet();
    }

    public PokemonConfigData getPokemonData(String pokemonType) {
        return pokemonTypes.get(pokemonType);
    }

    public enum MountType{
        WALK,
        SWIM,
        FLY
    }
}
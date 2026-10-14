package com.example.zone51;

/** consumable=false: карта не расходуется. rare: восстанавливается медленнее (15 с вместо 5 с). */
public enum CardType {
    BASIC("basic", false, false),
    EXPLOSIVE("explosive", true, true),
    FIRE("fire", true, false),
    ICE("ice", true, false),
    POISON("poison", true, false),
    SLOW("slow", true, false),
    SUMMON("summon", true, true),
    TRAP("trap", true, true),
    RETURN("return", true, true),
    HEAL("heal", true, false);

    public final String id;
    public final boolean consumable;
    public final boolean rare;

    CardType(String id, boolean consumable, boolean rare) {
        this.id = id;
        this.consumable = consumable;
        this.rare = rare;
    }
}

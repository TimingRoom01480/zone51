package com.example.zone51;

/** Данные для шкалы энергии (только клиент). */
public final class ClientEnergy {
    public static boolean hasAp, hasSuit;
    public static int ap, suit;

    public static void set(boolean hasAp, int ap, boolean hasSuit, int suit) {
        ClientEnergy.hasAp = hasAp; ClientEnergy.ap = ap; ClientEnergy.hasSuit = hasSuit; ClientEnergy.suit = suit;
    }
}

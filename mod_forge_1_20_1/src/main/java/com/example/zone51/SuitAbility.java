package com.example.zone51;

/** Активные способности костюмов. energy: расход энергии костюма (0 у костюмов без энергии); cd: перезарядка в тиках. */
public enum SuitAbility {
    GUARD_FLASH(SuitType.GUARD, 0, 200), GUARD_SCAN(SuitType.GUARD, 0, 300), GUARD_SMOKE(SuitType.GUARD, 0, 400),
    GUARD_TASER(SuitType.GUARD, 0, 100), GUARD_BACKUP(SuitType.GUARD, 0, 1200), GUARD_SPRINT(SuitType.GUARD, 0, 300),
    LAB_ANTIDOTE(SuitType.LAB, 0, 400), LAB_ACID(SuitType.LAB, 0, 200), LAB_PULSE(SuitType.LAB, 0, 400),
    LAB_STIM(SuitType.LAB, 0, 900), LAB_CRYO(SuitType.LAB, 0, 300), LAB_FIELD(SuitType.LAB, 0, 300),
    EXO_JET(SuitType.EXO, 20, 20), EXO_ROCKET(SuitType.EXO, 30, 60), EXO_MISSILES(SuitType.EXO, 40, 100),
    EXO_SHOCK(SuitType.EXO, 35, 120), EXO_OVERCHARGE(SuitType.EXO, 50, 600), EXO_BARRIER(SuitType.EXO, 45, 300),
    EXO_HOVER(SuitType.EXO, 25, 200),
    MUTANT_RAGE(SuitType.MUTANT, 0, 600), MUTANT_POUND(SuitType.MUTANT, 0, 200), MUTANT_HOWL(SuitType.MUTANT, 0, 300),
    MUTANT_REGEN(SuitType.MUTANT, 0, 500), MUTANT_BOUND(SuitType.MUTANT, 0, 60), MUTANT_FRENZY(SuitType.MUTANT, 0, 200),
    ANCIENT_DASH(SuitType.ANCIENT, 25, 40), ANCIENT_ELECTRO(SuitType.ANCIENT, 60, 200), ANCIENT_ARC(SuitType.ANCIENT, 40, 100),
    ANCIENT_AEGIS(SuitType.ANCIENT, 50, 300), ANCIENT_SENSE(SuitType.ANCIENT, 20, 300), ANCIENT_OVERLOAD(SuitType.ANCIENT, 100, 600),
    CARDSHARP_VORTEX(SuitType.CARDSHARP, 0, 600), CARDSHARP_VOLLEY(SuitType.CARDSHARP, 0, 120), CARDSHARP_TRAP(SuitType.CARDSHARP, 0, 200),
    CARDSHARP_JOKER(SuitType.CARDSHARP, 0, 300), CARDSHARP_SHUFFLE(SuitType.CARDSHARP, 0, 1200), CARDSHARP_ROYAL(SuitType.CARDSHARP, 0, 1800);

    public final SuitType suit;
    public final int energy;
    public final int cd;

    SuitAbility(SuitType suit, int energy, int cd) {
        this.suit = suit;
        this.energy = energy;
        this.cd = cd;
    }

    public String id() { return name().toLowerCase(); }

    public static SuitAbility first(SuitType s) {
        for (SuitAbility a : values()) if (a.suit == s) return a;
        return null;
    }

    public static SuitAbility byId(String id) {
        for (SuitAbility a : values()) if (a.id().equalsIgnoreCase(id)) return a;
        return null;
    }

    /** Следующая способность того же костюма (по кругу). */
    public SuitAbility next() {
        SuitAbility[] all = values();
        for (int i = 1; i <= all.length; i++) {
            SuitAbility c = all[(ordinal() + i) % all.length];
            if (c.suit == suit) return c;
        }
        return this;
    }
}

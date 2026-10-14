package com.example.zone51;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;

/** NPC-торговец из хижины Карточника: продаёт карты к данжам за золотые монеты. */
public class CardTrader extends AbstractVillager {
    public CardTrader(EntityType<? extends AbstractVillager> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.5);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        goalSelector.addGoal(2, new LookAtTradingPlayerGoal(this));
        goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.5));
        goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
    }

    @Override
    protected void updateTrades() {
        MerchantOffers o = getOffers();
        o.add(new MerchantOffer(new ItemStack(Items.GOLD_INGOT, 1), new ItemStack(ModItems.GOLD_COIN.get(), 3), 64, 0, 0.0f));
        o.add(new MerchantOffer(new ItemStack(ModItems.GOLD_COIN.get(), 8), new ItemStack(ModItems.STONE_DUNGEON_MAP.get()), 8, 0, 0.05f));
        o.add(new MerchantOffer(new ItemStack(ModItems.GOLD_COIN.get(), 16), new ItemStack(ModItems.CRYSTAL_DUNGEON_MAP.get()), 8, 0, 0.05f));
        o.add(new MerchantOffer(new ItemStack(ModItems.GOLD_COIN.get(), 24), new ItemStack(ModItems.ZONE51_MAP.get()), 8, 0, 0.05f));
        o.add(new MerchantOffer(new ItemStack(ModItems.GOLD_COIN.get(), 6), new ItemStack(ModItems.CARD_DECK.get()), 8, 0, 0.05f));
    }

    @Override
    public InteractionResult mobInteract(Player p, InteractionHand hand) {
        if (isAlive() && !isTrading()) {
            if (!level().isClientSide) {
                setTradingPlayer(p);
                openTradingScreen(p, getDisplayName(), 1);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(p, hand);
    }

    @Override protected void rewardTradeXp(MerchantOffer offer) { }
    @Override public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) { return null; }
    @Override public SoundEvent getNotifyTradeSound() { return SoundEvents.VILLAGER_YES; }
    @Override public boolean removeWhenFarAway(double d) { return false; }
}

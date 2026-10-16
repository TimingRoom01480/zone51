package com.example.zone51;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(modid = Zone51.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END || !(e.player instanceof ServerPlayer p)) return;
        long t = p.level().getGameTime();
        AncientPower.tick(p, t);
        AncientParts.tick(p, t);
        Suits.tickFast(p, t);
        if (t % 10 == 0) Suits.tick(p, t);
        if (t % 20 == 0) { // временные предметы (энергетический меч) исчезают
            Inventory inv = p.getInventory();
            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack s = inv.getItem(i);
                if (s.hasTag() && s.getTag().contains("zone51_expire") && t >= s.getTag().getLong("zone51_expire")) {
                    inv.setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent e) {
        LivingEntity victim = e.getEntity();
        DamageSource src = e.getSource();
        float amt = e.getAmount();

        if (victim instanceof ServerPlayer p) {
            long t = p.level().getGameTime();
            if (Suits.full(p, SuitType.GUARD) && src.is(DamageTypes.INDIRECT_MAGIC)) amt *= 0.5f; // защита от лазеров
            boolean field = (Suits.full(p, SuitType.EXO) || Suits.full(p, SuitType.ANCIENT)) && Suits.energy(p) > 0;
            if (field) amt *= 0.85f; // защитное поле
            if (Suits.full(p, SuitType.ANCIENT) && Suits.energy(p) >= 10 && src.is(DamageTypeTags.IS_PROJECTILE)
                    && t >= p.getPersistentData().getLong(AncientPower.SHIELD_CD)) {
                p.getPersistentData().putLong(AncientPower.SHIELD_CD, t + 200); // энергетический щит
                Suits.addEnergy(p, -10);
                e.setCanceled(true);
                return;
            }
        }

        if (src.getEntity() instanceof ServerPlayer a) {
            long t = a.level().getGameTime();
            CompoundTag d = a.getPersistentData();
            d.putLong(CardBeltItem.LAST_HIT, t);
            if (t < d.getLong(AncientPower.STRIKE)) { // «Удары энергией»
                amt += 6.0f + 2.0f * AncientPower.lvl(a, "attack");
                d.putLong(AncientPower.STRIKE, 0);
            }
            if (Suits.full(a, SuitType.ANCIENT) && Suits.energy(a) >= 5 && a.getRandom().nextFloat() < 0.2f) {
                Suits.addEnergy(a, -5); // молниевый удар
                Abilities.visualLightning(a.level(), victim.position());
                amt += 4.0f;
            }
            if (AncientParts.has(a, ModItems.ANCIENT_CRYSTAL.get()) && a.getRandom().nextFloat() < 0.2f) amt += 3.0f;
        }
        e.setAmount(amt);
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent e) {
        if (e.getEntity() instanceof ServerPlayer p
                && (Suits.full(p, SuitType.EXO) || Suits.full(p, SuitType.ANCIENT)) && Suits.energy(p) >= 2) {
            Suits.addEnergy(p, -2);
            e.setCanceled(true);
        }
    }

    /** Временные союзники (волки от карт и Сердца) исчезают через 30 секунд. */
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent e) {
        LivingEntity en = e.getEntity();
        if (en instanceof Wolf && !en.level().isClientSide && en.tickCount % 20 == 0) {
            CompoundTag t = en.getPersistentData();
            if (t.contains("zone51_expire") && en.level().getGameTime() >= t.getLong("zone51_expire")) en.discard();
        }
    }

    /** Древняя Сила сохраняется после смерти. */
    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone e) {
        if (!e.isWasDeath()) return;
        CompoundTag o = e.getOriginal().getPersistentData();
        CompoundTag n = e.getEntity().getPersistentData();
        for (String k : AncientPower.KEEP) {
            if (o.contains(k)) n.put(k, o.get(k).copy());
        }
    }

    // ---- Древнее Оружие: Shift+ПКМ меняет форму ----
    private static boolean transform(Player p, InteractionHand hand) {
        ItemStack s = p.getItemInHand(hand);
        List<RegistryObject<Item>> forms = ModItems.WEAPON_FORMS;
        for (int i = 0; i < forms.size(); i++) {
            if (s.is(forms.get(i).get())) {
                ItemStack n = new ItemStack(forms.get((i + 1) % forms.size()).get());
                if (s.isDamaged() && n.isDamageableItem()) n.setDamageValue(s.getDamageValue());
                if (!p.level().isClientSide) p.setItemInHand(hand, n);
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem e) {
        if (e.getEntity().isShiftKeyDown() && transform(e.getEntity(), e.getHand())) {
            e.setCancellationResult(InteractionResult.SUCCESS);
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
        if (e.getEntity().isShiftKeyDown() && transform(e.getEntity(), e.getHand())) {
            e.setCancellationResult(InteractionResult.SUCCESS);
            e.setCanceled(true);
        }
    }

    // ---- Метание карт по ЛКМ ----
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
        Player p = e.getEntity();
        if (p.getMainHandItem().getItem() instanceof CardItem && CardBeltItem.isWearing(p)) {
            e.setCanceled(true);
            if (!p.level().isClientSide) CardItem.throwFrom(p, InteractionHand.MAIN_HAND);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent e) {
        Player p = e.getEntity();
        if (p.getMainHandItem().getItem() instanceof CardItem && CardBeltItem.isWearing(p)) {
            e.setCanceled(true);
            if (!p.level().isClientSide) CardItem.throwFrom(p, InteractionHand.MAIN_HAND);
        }
    }

    /** Крафт-бонус Лабораторного костюма: 25% шанс получить копию результата. */
    @SubscribeEvent
    public static void onCrafted(PlayerEvent.ItemCraftedEvent e) {
        if (e.getEntity() instanceof ServerPlayer p && Suits.full(p, SuitType.LAB) && p.getRandom().nextFloat() < 0.25f) {
            ItemHandlerHelper.giveItemToPlayer(p, e.getCrafting().copy());
            p.displayClientMessage(Component.translatable("message.zone51.craft_bonus"), true);
        }
    }

    /** /zone51 grant: выдать себе Древнюю Силу (для теста, нужен оп). */
    @SubscribeEvent
    public static void onCommands(RegisterCommandsEvent e) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("zone51");
        root.then(Commands.literal("grant").requires(s -> s.hasPermission(2)).executes(c -> {
            AncientPower.grant(c.getSource().getPlayerOrException());
            return Command.SINGLE_SUCCESS;
        }));
        // команды, которые вызывают способности Palladium (значки справа внизу)
        root.then(Commands.literal("ability").then(Commands.argument("name", StringArgumentType.word()).executes(c -> {
            AncientPower.Ability a = AncientPower.fromName(StringArgumentType.getString(c, "name"));
            if (a != null) AncientPower.useAbility(c.getSource().getPlayerOrException(), a);
            return Command.SINGLE_SUCCESS;
        })));
        root.then(Commands.literal("suit").executes(c -> {
            Suits.suitAbility(c.getSource().getPlayerOrException());
            return Command.SINGLE_SUCCESS;
        }));
        root.then(Commands.literal("flight")
                .then(Commands.literal("on").executes(c -> {
                    AncientPower.setFlight(c.getSource().getPlayerOrException(), true);
                    return Command.SINGLE_SUCCESS;
                }))
                .then(Commands.literal("off").executes(c -> {
                    AncientPower.setFlight(c.getSource().getPlayerOrException(), false);
                    return Command.SINGLE_SUCCESS;
                })));
        e.getDispatcher().register(root);
    }
}

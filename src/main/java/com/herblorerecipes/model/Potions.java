package com.herblorerecipes.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.runelite.api.gameval.ItemID;

public enum Potions
{

	// BASIC POTIONS
	// Special case - only potion with many primary ingredients - using secondaryIngredients to support this.
	IMP_REPELLENT(Potion.builder()
			.level(3)
			.name("Imp repellent")
			.basicBase(ItemID.II_ANCHOVY_OIL)
			.secondaries(ImmutableSet.of(ItemID.MARIGOLD, ItemID.ROSEMARY, ItemID.NASTURTIUM, ItemID.FLOWERS_WATERFALL_QUEST,
					ItemID.FLOWERS_WATERFALL_QUEST_RED, ItemID.FLOWERS_WATERFALL_QUEST_BLUE, ItemID.FLOWERS_WATERFALL_QUEST_YELLOW, ItemID.FLOWERS_WATERFALL_QUEST_PURPLE,
					ItemID.FLOWERS_WATERFALL_QUEST_ORANGE, ItemID.FLOWERS_WATERFALL_QUEST_MIXED, ItemID.FLOWERS_WATERFALL_QUEST_BLACK, ItemID.FLOWERS_WATERFALL_QUEST_WHITE))
			.ids(ImmutableSet.of(ItemID.II_IMP_REPELLENT))
			.build()),

	ATTACK_POTION(Potion.builder()
			.level(3)
			.name("Attack potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.GUAM_LEAF)
			.grimyHerb(ItemID.UNIDENTIFIED_GUAM)
			.primarySeed(ItemID.GUAM_SEED)
			.secondaries(ImmutableSet.of(ItemID.EYE_OF_NEWT))
			.unfinishedPotion(ItemID.GUAMVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE1ATTACK, ItemID._2DOSE1ATTACK, ItemID._3DOSE1ATTACK, ItemID._4DOSE1ATTACK))
			.build()),

	ANTIPOISON(Potion.builder()
			.level(5)
			.name("Antipoison")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.MARENTILL)
			.grimyHerb(ItemID.UNIDENTIFIED_MARENTILL)
			.primarySeed(ItemID.MARRENTILL_SEED)
			.secondaries(ImmutableSet.of(ItemID.UNICORN_HORN_DUST))
			.secondariesAlt(ImmutableSet.of(ItemID.UNICORN_HORN))
			.unfinishedPotion(ItemID.MARRENTILLVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEANTIPOISON, ItemID._2DOSEANTIPOISON, ItemID._3DOSEANTIPOISON, ItemID._4DOSEANTIPOISON))
			.build()),

	RELICYMS_BALM(Potion.builder()
			.level(8)
			.name("Relicym's balm")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.ROGUES_PURSE)
			.grimyHerb(ItemID.UNIDENTIFIED_ROGUES_PURSE)
			.secondaries(ImmutableSet.of(ItemID.SNAKE_WEED))
			.secondariesAlt(ImmutableSet.of(ItemID.UNIDENTIFIED_SNAKE_WEED))
			.unfinishedPotion(ItemID.ROGUES_PURSE_SOL)
			.ids(ImmutableSet.of(ItemID.RELICYMS_BALM1, ItemID.RELICYMS_BALM2, ItemID.RELICYMS_BALM3, ItemID.RELICYMS_BALM4))
			.build()),

	STRENGTH_POTION(Potion.builder()
			.level(12)
			.name("Strength potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.TARROMIN)
			.grimyHerb(ItemID.UNIDENTIFIED_TARROMIN)
			.primarySeed(ItemID.TARROMIN_SEED)
			.secondaries(ImmutableSet.of(ItemID.LIMPWURT_ROOT))
			.secondariesAlt(ImmutableSet.of(ItemID.LIMPWURT_SEED))
			.unfinishedPotion(ItemID.TARROMINVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE1STRENGTH, ItemID._2DOSE1STRENGTH, ItemID._3DOSE1STRENGTH, ItemID.STRENGTH4))
			.build()),

	SERUM_207(Potion.builder()
			.level(15)
			.name("Serum 207")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.TARROMIN)
			.grimyHerb(ItemID.UNIDENTIFIED_TARROMIN)
			.primarySeed(ItemID.TARROMIN_SEED)
			.secondaries(ImmutableSet.of(ItemID.ASHES))
			.unfinishedPotion(ItemID.TARROMINVIAL)
			.ids(ImmutableSet.of(ItemID.MORT_SERUM1, ItemID.MORT_SERUM2, ItemID.MORT_SERUM3, ItemID.MORT_SERUM4))
			.build()),

	GUTHIX_REST_TEA(Potion.builder()
			.level(18)
			.name("Guthix rest tea")
			.basicBase(ItemID.CUP_HOT_WATER)
			.primary(ItemID.HARRALANDER)
			.grimyHerb(ItemID.UNIDENTIFIED_HARRALANDER)
			.primarySeed(ItemID.HARRALANDER_SEED)
			.secondaries(ImmutableSet.of(ItemID.GUAM_LEAF, ItemID.MARENTILL))
			.secondariesAlt(ImmutableSet.of(ItemID.UNIDENTIFIED_GUAM, ItemID.UNIDENTIFIED_MARENTILL))
			.unfinishedPotion(ItemID.HARRALANDERVIAL)
			.ids(ImmutableSet.of(ItemID.CUP_GUTHIX_REST_1, ItemID.CUP_GUTHIX_REST_2, ItemID.CUP_GUTHIX_REST_3, ItemID.CUP_GUTHIX_REST_4))
			.build()),

	COMPOST_POTION(Potion.builder()
			.level(22)
			.name("Compost potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.HARRALANDER)
			.grimyHerb(ItemID.UNIDENTIFIED_HARRALANDER)
			.primarySeed(ItemID.HARRALANDER_SEED)
			.secondaries(ImmutableSet.of(ItemID.FOSSIL_VOLCANIC_ASH))
			.unfinishedPotion(ItemID.HARRALANDERVIAL)
			.ids(ImmutableSet.of(ItemID.SUPERCOMPOST_POTION_1, ItemID.SUPERCOMPOST_POTION_2, ItemID.SUPERCOMPOST_POTION_3, ItemID.SUPERCOMPOST_POTION_4))
			.build()),

	RESTORE_POTION(Potion.builder()
			.level(22)
			.name("Restore potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.HARRALANDER)
			.grimyHerb(ItemID.UNIDENTIFIED_HARRALANDER)
			.primarySeed(ItemID.HARRALANDER_SEED)
			.secondaries(ImmutableSet.of(ItemID.RED_SPIDERS_EGGS))
			.unfinishedPotion(ItemID.HARRALANDERVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSESTATRESTORE, ItemID._2DOSESTATRESTORE, ItemID._3DOSESTATRESTORE, ItemID._4DOSESTATRESTORE))
			.build()),

	BLAMISH_OIL(Potion.builder()
			.level(25)
			.name("Blamish oil")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.HARRALANDER)
			.grimyHerb(ItemID.UNIDENTIFIED_HARRALANDER)
			.primarySeed(ItemID.HARRALANDER_SEED)
			.secondaries(ImmutableSet.of(ItemID.BLAMISH_SNAIL_SLIME))
			.unfinishedPotion(ItemID.HARRALANDERVIAL)
			.ids(ImmutableSet.of(ItemID.BLAMISH_OIL))
			.build()),

	ENERGY_POTION(Potion.builder()
			.level(26)
			.name("Energy potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.HARRALANDER)
			.grimyHerb(ItemID.UNIDENTIFIED_HARRALANDER)
			.primarySeed(ItemID.HARRALANDER_SEED)
			.secondaries(ImmutableSet.of(ItemID.CHOCOLATE_DUST))
			.secondariesAlt(ImmutableSet.of(ItemID.CHOCOLATE_BAR))
			.unfinishedPotion(ItemID.HARRALANDERVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE1ENERGY, ItemID._2DOSE1ENERGY, ItemID._3DOSE1ENERGY, ItemID._4DOSE1ENERGY))
			.build()),

	DEFENCE_POTION(Potion.builder()
			.level(30)
			.name("Defence potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.RANARR_WEED)
			.grimyHerb(ItemID.UNIDENTIFIED_RANARR)
			.primarySeed(ItemID.RANARR_SEED)
			.secondaries(ImmutableSet.of(ItemID.WHITE_BERRIES))
			.unfinishedPotion(ItemID.RANARRVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE1DEFENSE, ItemID._2DOSE1DEFENSE, ItemID._3DOSE1DEFENSE, ItemID._4DOSE1DEFENSE))
			.build()),

	AGILITY_POTION(Potion.builder()
			.level(34)
			.name("Agility potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.TOADFLAX)
			.grimyHerb(ItemID.UNIDENTIFIED_TOADFLAX)
			.primarySeed(ItemID.TOADFLAX_SEED)
			.secondaries(ImmutableSet.of(ItemID.TOADS_LEGS))
			.unfinishedPotion(ItemID.TOADFLAXVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE1AGILITY, ItemID._2DOSE1AGILITY, ItemID._3DOSE1AGILITY, ItemID._4DOSE1AGILITY))
			.build()),

	COMBAT_POTION(Potion.builder()
			.level(36)
			.name("Combat potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.HARRALANDER)
			.grimyHerb(ItemID.UNIDENTIFIED_HARRALANDER)
			.primarySeed(ItemID.HARRALANDER_SEED)
			.secondaries(ImmutableSet.of(ItemID.GROUND_DESERT_GOAT_HORN))
			.secondariesAlt(ImmutableSet.of(ItemID.DESERT_GOAT_HORN))
			.unfinishedPotion(ItemID.HARRALANDERVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSECOMBAT, ItemID._2DOSECOMBAT, ItemID._3DOSECOMBAT, ItemID._4DOSECOMBAT))
			.build()),

	PRAYER_POTION(Potion.builder()
			.level(38)
			.name("Prayer potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.RANARR_WEED)
			.grimyHerb(ItemID.UNIDENTIFIED_RANARR)
			.primarySeed(ItemID.RANARR_SEED)
			.secondaries(ImmutableSet.of(ItemID.SNAPE_GRASS))
			.secondariesAlt(ImmutableSet.of(ItemID.SNAPE_GRASS_SEED))
			.unfinishedPotion(ItemID.RANARRVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEPRAYERRESTORE, ItemID._2DOSEPRAYERRESTORE, ItemID._3DOSEPRAYERRESTORE, ItemID._4DOSEPRAYERRESTORE))
			.build()),

	SUPER_ATTACK(Potion.builder()
			.level(45)
			.name("Super attack")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.IRIT_LEAF)
			.grimyHerb(ItemID.UNIDENTIFIED_IRIT)
			.primarySeed(ItemID.IRIT_SEED)
			.secondaries(ImmutableSet.of(ItemID.EYE_OF_NEWT))
			.unfinishedPotion(ItemID.IRITVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE2ATTACK, ItemID._2DOSE2ATTACK, ItemID._3DOSE2ATTACK, ItemID._4DOSE2ATTACK))
			.build()),

	GOBLIN_POTION(Potion.builder()
			.level(47)
			.name("Goblin potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.TOADFLAX)
			.grimyHerb(ItemID.UNIDENTIFIED_TOADFLAX)
			.primarySeed(ItemID.TOADFLAX_SEED)
			.secondaries(ImmutableSet.of(ItemID.LOTG_PHARMAKOS_BERRY))
			.unfinishedPotion(ItemID.TOADFLAXVIAL)
			.ids(ImmutableSet.of(ItemID.LOTG_1DOSEGOBLIN, ItemID.LOTG_2DOSEGOBLIN, ItemID.LOTG_3DOSEGOBLIN, ItemID.LOTG_4DOSEGOBLIN))
			.build()),

	SUPERANTIPOISON(Potion.builder()
			.level(48)
			.name("Superantipoison")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.IRIT_LEAF)
			.grimyHerb(ItemID.UNIDENTIFIED_IRIT)
			.primarySeed(ItemID.IRIT_SEED)
			.secondaries(ImmutableSet.of(ItemID.UNICORN_HORN_DUST))
			.secondariesAlt(ImmutableSet.of(ItemID.UNICORN_HORN))
			.unfinishedPotion(ItemID.IRITVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE2ANTIPOISON, ItemID._2DOSE2ANTIPOISON, ItemID._3DOSE2ANTIPOISON, ItemID._4DOSE2ANTIPOISON))
			.build()),

	FISHING_POTION(Potion.builder()
			.level(50)
			.name("Fishing potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.AVANTOE)
			.grimyHerb(ItemID.UNIDENTIFIED_AVANTOE)
			.primarySeed(ItemID.AVANTOE_SEED)
			.secondaries(ImmutableSet.of(ItemID.SNAPE_GRASS))
			.secondariesAlt(ImmutableSet.of(ItemID.SNAPE_GRASS_SEED))
			.unfinishedPotion(ItemID.AVANTOEVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEFISHERSPOTION, ItemID._2DOSEFISHERSPOTION, ItemID._3DOSEFISHERSPOTION, ItemID._4DOSEFISHERSPOTION))
			.build()),

	SUPER_ENERGY(Potion.builder()
			.level(52)
			.name("Super energy")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.AVANTOE)
			.grimyHerb(ItemID.UNIDENTIFIED_AVANTOE)
			.primarySeed(ItemID.AVANTOE_SEED)
			.secondaries(ImmutableSet.of(ItemID.MORTMYREMUSHROOM))
			.unfinishedPotion(ItemID.AVANTOEVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE2ENERGY, ItemID._2DOSE2ENERGY, ItemID._3DOSE2ENERGY, ItemID._4DOSE2ENERGY))
			.build()),

	SHRINK_ME_QUICK(Potion.builder()
			.level(52)
			.name("Shrink-me-quick")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.TARROMIN)
			.grimyHerb(ItemID.UNIDENTIFIED_TARROMIN)
			.primarySeed(ItemID.TARROMIN_SEED)
			.secondaries(ImmutableSet.of(ItemID.GRIM_TURNIP))
			.unfinishedPotion(ItemID.TARROMINVIAL)
			.ids(ImmutableSet.of(ItemID.GRIM_SHRINKING_POTION))
			.build()),

	HUNTER_POTION(Potion.builder()
			.level(53)
			.name("Hunter potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.AVANTOE)
			.grimyHerb(ItemID.UNIDENTIFIED_AVANTOE)
			.primarySeed(ItemID.AVANTOE_SEED)
			.secondaries(ImmutableSet.of(ItemID.HUNTINGBEAST_SABRETEETH_DUST))
			.secondariesAlt(ImmutableSet.of(ItemID.HUNTINGBEAST_SABRETEETH))
			.unfinishedPotion(ItemID.AVANTOEVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEHUNTING, ItemID._2DOSEHUNTING, ItemID._3DOSEHUNTING, ItemID._4DOSEHUNTING))
			.build()),

	GOADING_POTION(Potion.builder()
			.level(54)
			.name("Goading potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.HARRALANDER)
			.grimyHerb(ItemID.UNIDENTIFIED_HARRALANDER)
			.primarySeed(ItemID.HARRALANDER_SEED)
			.secondaries(ImmutableSet.of(ItemID.ALDARIUM))
			.unfinishedPotion(ItemID.HARRALANDERVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEGOADING, ItemID._2DOSEGOADING, ItemID._3DOSEGOADING, ItemID._4DOSEGOADING))
			.build()),

	SUPER_STRENGTH(Potion.builder()
			.level(55)
			.name("Super strength")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.KWUARM)
			.grimyHerb(ItemID.UNIDENTIFIED_KWUARM)
			.primarySeed(ItemID.KWUARM_SEED)
			.secondaries(ImmutableSet.of(ItemID.LIMPWURT_ROOT))
			.secondariesAlt(ImmutableSet.of(ItemID.LIMPWURT_SEED))
			.unfinishedPotion(ItemID.KWUARMVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE2STRENGTH, ItemID._2DOSE2STRENGTH, ItemID._3DOSE2STRENGTH, ItemID._4DOSE2STRENGTH))
			.build()),

	MAGIC_ESSENCE(Potion.builder()
			.level(57)
			.name("Magic essence")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.FAIRYTALE2_FLOWER_HERBS)
			.secondaries(ImmutableSet.of(ItemID.FAIRYTALE2_GROUND_GORAK_CLAWS))
			.secondariesAlt(ImmutableSet.of(ItemID.FAIRYTALE2_GORAK_CLAWS))
			.unfinishedPotion(ItemID.FAIRYTALE2_STARFLOWERVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEMAGICESS, ItemID._2DOSEMAGICESS, ItemID._3DOSEMAGICESS, ItemID._4DOSEMAGICESS))
			.build()),

	PRAYER_REGENERATION_POTION(Potion.builder()
			.level(58)
			.name("Prayer regeneration potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.HUASCA)
			.grimyHerb(ItemID.UNIDENTIFIED_HUASCA)
			.primarySeed(ItemID.HUASCA_SEED)
			.secondaries(ImmutableSet.of(ItemID.ALDARIUM))
			.unfinishedPotion(ItemID.HUASCAVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE1PRAYER_REGENERATION, ItemID._2DOSE1PRAYER_REGENERATION, ItemID._3DOSE1PRAYER_REGENERATION, ItemID._4DOSE1PRAYER_REGENERATION))
			.build()),

	WEAPON_POISON(Potion.builder()
			.level(60)
			.name("Weapon poison")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.KWUARM)
			.grimyHerb(ItemID.UNIDENTIFIED_KWUARM)
			.primarySeed(ItemID.KWUARM_SEED)
			.secondaries(ImmutableSet.of(ItemID.DRAGON_SCALE_DUST))
			.secondariesAlt(ImmutableSet.of(ItemID.BLUE_DRAGON_SCALE))
			.unfinishedPotion(ItemID.KWUARMVIAL)
			.ids(ImmutableSet.of(ItemID.WEAPON_POISON))
			.build()),

	SUPER_RESTORE(Potion.builder()
			.level(63)
			.name("Super restore")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.SNAPDRAGON)
			.grimyHerb(ItemID.UNIDENTIFIED_SNAPDRAGON)
			.primarySeed(ItemID.SNAPDRAGON_SEED)
			.secondaries(ImmutableSet.of(ItemID.RED_SPIDERS_EGGS))
			.unfinishedPotion(ItemID.SNAPDRAGONVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE2RESTORE, ItemID._2DOSE2RESTORE, ItemID._3DOSE2RESTORE, ItemID._4DOSE2RESTORE))
			.build()),

	SUPER_DEFENCE(Potion.builder()
			.level(66)
			.name("Super defence")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.CADANTINE)
			.grimyHerb(ItemID.UNIDENTIFIED_CADANTINE)
			.primarySeed(ItemID.CADANTINE_SEED)
			.secondaries(ImmutableSet.of(ItemID.WHITE_BERRIES))
			.unfinishedPotion(ItemID.CADANTINEVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE2DEFENSE, ItemID._2DOSE2DEFENSE, ItemID._3DOSE2DEFENSE, ItemID._4DOSE2DEFENSE))
			.build()),

	ANTIDOTE_PLUS(Potion.builder()
			.level(68)
			.name("Antidote+")
			.basicBase(ItemID.VIAL_COCONUT_MILK)
			.primary(ItemID.TOADFLAX)
			.grimyHerb(ItemID.UNIDENTIFIED_TOADFLAX)
			.primarySeed(ItemID.TOADFLAX_SEED)
			.secondaries(ImmutableSet.of(ItemID.YEW_ROOTS))
			.unfinishedPotion(ItemID.TOADFLAXVIAL)
			.ids(ImmutableSet.of(ItemID.ANTIDOTE_1, ItemID.ANTIDOTE_2, ItemID.ANTIDOTE_3, ItemID.ANTIDOTE_4))
			.build()),

	ANTIFIRE_POTION(Potion.builder()
			.level(69)
			.name("Antifire potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.LANTADYME)
			.grimyHerb(ItemID.UNIDENTIFIED_LANTADYME)
			.primarySeed(ItemID.LANTADYME_SEED)
			.secondaries(ImmutableSet.of(ItemID.DRAGON_SCALE_DUST))
			.secondariesAlt(ImmutableSet.of(ItemID.BLUE_DRAGON_SCALE))
			.unfinishedPotion(ItemID.LANTADYMEVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE1ANTIDRAGON, ItemID._2DOSE1ANTIDRAGON, ItemID._3DOSE1ANTIDRAGON, ItemID._4DOSE1ANTIDRAGON))
			.build()),

	RANGING_POTION(Potion.builder()
			.level(72)
			.name("Ranging potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.DWARF_WEED)
			.grimyHerb(ItemID.UNIDENTIFIED_DWARF_WEED)
			.primarySeed(ItemID.DWARF_WEED_SEED)
			.secondaries(ImmutableSet.of(ItemID.WINE_OF_ZAMORAK))
			.unfinishedPotion(ItemID.DWARFWEEDVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSERANGERSPOTION, ItemID._2DOSERANGERSPOTION, ItemID._3DOSERANGERSPOTION, ItemID._4DOSERANGERSPOTION))
			.build()),

	WEAPON_POISON_PLUS(Potion.builder()
			.level(73)
			.name("Weapon poison+")
			.basicBase(ItemID.VIAL_COCONUT_MILK)
			.primary(ItemID.CACTUS_SPINE)
			.secondaries(ImmutableSet.of(ItemID.RED_SPIDERS_EGGS))
			.unfinishedPotion(ItemID.UNFINISHED_WEAPON_POISON_)
			.ids(ImmutableSet.of(ItemID.WEAPON_POISON_))
			.build()),

	MAGIC_POTION(Potion.builder()
			.level(76)
			.name("Magic potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.LANTADYME)
			.grimyHerb(ItemID.UNIDENTIFIED_LANTADYME)
			.primarySeed(ItemID.LANTADYME_SEED)
			.secondaries(ImmutableSet.of(ItemID.CACTUS_POTATO))
			.secondariesAlt(ImmutableSet.of(ItemID.POTATO_CACTUS_SEED))
			.unfinishedPotion(ItemID.LANTADYMEVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE1MAGIC, ItemID._2DOSE1MAGIC, ItemID._3DOSE1MAGIC, ItemID._4DOSE1MAGIC))
			.build()),

	ZAMORAK_BREW(Potion.builder()
			.level(78)
			.name("Zamorak brew")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.TORSTOL)
			.grimyHerb(ItemID.UNIDENTIFIED_TORSTOL)
			.primarySeed(ItemID.TORSTOL_SEED)
			.secondaries(ImmutableSet.of(ItemID.JANGERBERRIES))
			.secondariesAlt(ImmutableSet.of(ItemID.JANGERBERRY_BUSH_SEED))
			.unfinishedPotion(ItemID.TORSTOLVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEPOTIONOFZAMORAK, ItemID._2DOSEPOTIONOFZAMORAK, ItemID._3DOSEPOTIONOFZAMORAK, ItemID._4DOSEPOTIONOFZAMORAK))
			.build()),

	ANTIDOTE_PLUS2(Potion.builder()
			.level(79)
			.name("Antidote++")
			.basicBase(ItemID.VIAL_COCONUT_MILK)
			.primary(ItemID.IRIT_LEAF)
			.grimyHerb(ItemID.UNIDENTIFIED_IRIT)
			.primarySeed(ItemID.IRIT_SEED)
			.secondaries(ImmutableSet.of(ItemID.MAGIC_ROOTS))
			.unfinishedPotion(ItemID.IRITVIAL)
			.ids(ImmutableSet.of(ItemID.ANTIDOTE__1, ItemID.ANTIDOTE__2, ItemID.ANTIDOTE__3, ItemID.ANTIDOTE__4))
			.build()),

	BASTION_POTION(Potion.builder()
			.level(80)
			.name("Bastion potion")
			.basicBase(ItemID.MYQ4_BLOOD_VIAL)
			.primary(ItemID.CADANTINE)
			.grimyHerb(ItemID.UNIDENTIFIED_CADANTINE)
			.primarySeed(ItemID.CADANTINE_SEED)
			.secondaries(ImmutableSet.of(ItemID.WINE_OF_ZAMORAK))
			.unfinishedPotion(ItemID.CADANTINE_BLOODVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEBASTION, ItemID._2DOSEBASTION, ItemID._3DOSEBASTION, ItemID._4DOSEBASTION))
			.build()),

	BATTLEMAGE_POTION(Potion.builder()
			.level(80)
			.name("Battlemage potion")
			.basicBase(ItemID.MYQ4_BLOOD_VIAL)
			.primary(ItemID.CADANTINE)
			.grimyHerb(ItemID.UNIDENTIFIED_CADANTINE)
			.primarySeed(ItemID.CADANTINE_SEED)
			.secondaries(ImmutableSet.of(ItemID.CACTUS_POTATO))
			.secondariesAlt(ImmutableSet.of(ItemID.POTATO_CACTUS_SEED))
			.unfinishedPotion(ItemID.CADANTINE_BLOODVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEBATTLEMAGE, ItemID._2DOSEBATTLEMAGE, ItemID._3DOSEBATTLEMAGE, ItemID._4DOSEBATTLEMAGE))
			.build()),

	SARADOMIN_BREW(Potion.builder()
			.level(81)
			.name("Saradomin brew")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.TOADFLAX)
			.grimyHerb(ItemID.UNIDENTIFIED_TOADFLAX)
			.primarySeed(ItemID.TOADFLAX_SEED)
			.secondaries(ImmutableSet.of(ItemID.CRUSHED_BIRD_NEST))
			.unfinishedPotion(ItemID.TOADFLAXVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEPOTIONOFSARADOMIN, ItemID._2DOSEPOTIONOFSARADOMIN, ItemID._3DOSEPOTIONOFSARADOMIN, ItemID._4DOSEPOTIONOFSARADOMIN))
			.build()),

	WEAPON_POISON_PLUS2(Potion.builder()
			.level(82)
			.name("Weapon poison++")
			.basicBase(ItemID.VIAL_COCONUT_MILK)
			.primary(ItemID.STACKABLE_NIGHTSHADE)
			.primaryAlt(ItemID.STACKABLE_NIGHTSHADE)
			.secondaries(ImmutableSet.of(ItemID.POISONIVY_BERRIES))
			.secondariesAlt(ImmutableSet.of(ItemID.POISONIVY_BUSH_SEED))
			.unfinishedPotion(ItemID.UNFINISHED_WEAPON_POISON__)
			.ids(ImmutableSet.of(ItemID.WEAPON_POISON__))
			.build()),

	ANCIENT_BREW(Potion.builder()
			.level(85)
			.name("Ancient brew")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.DWARF_WEED)
			.grimyHerb(ItemID.UNIDENTIFIED_DWARF_WEED)
			.primarySeed(ItemID.DWARF_WEED_SEED)
			.secondaries(ImmutableSet.of(ItemID.NIHIL_DUST))
			.secondariesAlt(ImmutableSet.of(ItemID.NIHIL_SHARD))
			.unfinishedPotion(ItemID.DWARFWEEDVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEANCIENTBREW, ItemID._2DOSEANCIENTBREW, ItemID._3DOSEANCIENTBREW, ItemID._4DOSEANCIENTBREW))
			.build()),

	MENAPHITE_REMEDY(Potion.builder()
			.level(88)
			.name("Menaphite Remedy")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.DWARF_WEED)
			.grimyHerb(ItemID.UNIDENTIFIED_DWARF_WEED)
			.primarySeed(ItemID.DWARF_WEED_SEED)
			.secondaries(ImmutableSet.of(ItemID.LILY_OF_THE_SANDS))
			.unfinishedPotion(ItemID.DWARFWEEDVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSESTATRENEWAL, ItemID._2DOSESTATRENEWAL, ItemID._3DOSESTATRENEWAL, ItemID._4DOSESTATRENEWAL))
			.build()),

	// COMPLEX POTIONS
	GUTHIX_BALANCE(Potion.builder()
			.level(22)
			.name("Guthix balance")
			.complexBase(ImmutableSet.of(RESTORE_POTION.potion))
			.primary(ItemID.GARLIC)
			.secondaries(ImmutableSet.of(ItemID.SILVER_DUST))
			.ids(ImmutableSet.of(ItemID.BURGH_GUTHIX_BALANCE_1, ItemID.BURGH_GUTHIX_BALANCE_2, ItemID.BURGH_GUTHIX_BALANCE_3, ItemID.BURGH_GUTHIX_BALANCE_4))
			.build()),

	SANFEW_SERUM(Potion.builder()
			.level(65)
			.name("Sanfew serum")
			.complexBase(ImmutableSet.of(SUPER_RESTORE.potion))
			.primary(ItemID.UNICORN_HORN_DUST)
			.primaryAlt(ItemID.UNICORN_HORN)
			.secondaries(ImmutableSet.of(ItemID.SNAKE_WEED, ItemID.NAIL_BEAST_NAIL))
			.ids(ImmutableSet.of(ItemID.SANFEW_SALVE_1_DOSE, ItemID.SANFEW_SALVE_2_DOSE, ItemID.SANFEW_SALVE_3_DOSE, ItemID.SANFEW_SALVE_4_DOSE))
			.build()),

	DIVINE_SUPER_ATTACK(Potion.builder()
			.level(70)
			.name("Divine super attack potion")
			.complexBase(ImmutableSet.of(SUPER_ATTACK.potion))
			.primary(ItemID.SOTE_CRYSTAL_DUST)
			.primaryAlt(ItemID.PRIF_CRYSTAL_SHARD)
			.ids(ImmutableSet.of(ItemID._1DOSEDIVINEATTACK, ItemID._2DOSEDIVINEATTACK, ItemID._3DOSEDIVINEATTACK, ItemID._4DOSEDIVINEATTACK))
			.build()),

	DIVINE_SUPER_DEFENCE(Potion.builder()
			.level(70)
			.name("Divine super defence potion")
			.complexBase(ImmutableSet.of(SUPER_DEFENCE.potion))
			.primary(ItemID.SOTE_CRYSTAL_DUST)
			.primaryAlt(ItemID.PRIF_CRYSTAL_SHARD)
			.ids(ImmutableSet.of(ItemID._1DOSEDIVINEDEFENCE, ItemID._2DOSEDIVINEDEFENCE, ItemID._3DOSEDIVINEDEFENCE, ItemID._4DOSEDIVINEDEFENCE))
			.build()),

	DIVINE_SUPER_STRENGTH(Potion.builder()
			.level(70)
			.name("Divine super strength potion")
			.complexBase(ImmutableSet.of(SUPER_STRENGTH.potion))
			.primary(ItemID.SOTE_CRYSTAL_DUST)
			.primaryAlt(ItemID.PRIF_CRYSTAL_SHARD)
			.ids(ImmutableSet.of(ItemID._1DOSEDIVINESTRENGTH, ItemID._2DOSEDIVINESTRENGTH, ItemID._3DOSEDIVINESTRENGTH, ItemID._4DOSEDIVINESTRENGTH))
			.build()),

	DIVINE_RANGING(Potion.builder()
			.level(74)
			.name("Divine ranging potion")
			.complexBase(ImmutableSet.of(RANGING_POTION.potion))
			.primary(ItemID.SOTE_CRYSTAL_DUST)
			.primaryAlt(ItemID.PRIF_CRYSTAL_SHARD)
			.ids(ImmutableSet.of(ItemID._1DOSEDIVINERANGE, ItemID._2DOSEDIVINERANGE, ItemID._3DOSEDIVINERANGE, ItemID._4DOSEDIVINERANGE))
			.build()),

	STAMINA_POTION(Potion.builder()
			.level(77)
			.name("Stamina potion")
			.complexBase(ImmutableSet.of(SUPER_ENERGY.potion))
			.primary(ItemID.AMYLASE)
			.primaryAlt(ItemID.PACK_AMYLASE)
			.ids(ImmutableSet.of(ItemID._1DOSESTAMINA, ItemID._2DOSESTAMINA, ItemID._3DOSESTAMINA, ItemID._4DOSESTAMINA))
			.build()),

	DIVINE_MAGIC(Potion.builder()
			.level(78)
			.name("Divine magic potion")
			.complexBase(ImmutableSet.of(MAGIC_POTION.potion))
			.primary(ItemID.SOTE_CRYSTAL_DUST)
			.primaryAlt(ItemID.PRIF_CRYSTAL_SHARD)
			.ids(ImmutableSet.of(ItemID._1DOSEDIVINEMAGIC, ItemID._2DOSEDIVINEMAGIC, ItemID._3DOSEDIVINEMAGIC, ItemID._4DOSEDIVINEMAGIC))
			.build()),

	EXTENDED_ANTIFIRE(Potion.builder()
			.level(84)
			.name("Extended antifire")
			.complexBase(ImmutableSet.of(ANTIFIRE_POTION.potion))
			.primary(ItemID.LAVA_SHARD)
			.primaryAlt(ItemID.LAVA_SCALE)
			.ids(ImmutableSet.of(ItemID._1DOSE2ANTIDRAGON, ItemID._2DOSE2ANTIDRAGON, ItemID._3DOSE2ANTIDRAGON, ItemID._4DOSE2ANTIDRAGON))
			.build()),

	DIVINE_BASTION(Potion.builder()
			.level(86)
			.name("Divine bastion potion")
			.complexBase(ImmutableSet.of(BASTION_POTION.potion))
			.primary(ItemID.SOTE_CRYSTAL_DUST)
			.primaryAlt(ItemID.PRIF_CRYSTAL_SHARD)
			.ids(ImmutableSet.of(ItemID._1DOSEDIVINEBASTION, ItemID._2DOSEDIVINEBASTION, ItemID._3DOSEDIVINEBASTION, ItemID._4DOSEDIVINEBASTION))
			.build()),

	DIVINE_BATTLEMAGE(Potion.builder()
			.level(86)
			.name("Divine battlemage potion")
			.complexBase(ImmutableSet.of(BATTLEMAGE_POTION.potion))
			.primary(ItemID.SOTE_CRYSTAL_DUST)
			.primaryAlt(ItemID.PRIF_CRYSTAL_SHARD)
			.ids(ImmutableSet.of(ItemID._1DOSEDIVINEBATTLEMAGE, ItemID._2DOSEDIVINEBATTLEMAGE, ItemID._3DOSEDIVINEBATTLEMAGE, ItemID._4DOSEDIVINEBATTLEMAGE))
			.build()),

	ANTIVENOM(Potion.builder()
			.level(87)
			.name("Anti-venom")
			.complexBase(ImmutableSet.of(ANTIDOTE_PLUS2.potion))
			.primary(ItemID.SNAKEBOSS_SCALE)
			.ids(ImmutableSet.of(ItemID.ANTIVENOM1, ItemID.ANTIVENOM2, ItemID.ANTIVENOM3, ItemID.ANTIVENOM4))
			.build()),

	SUPER_COMBAT_POTION(Potion.builder()
			.level(90)
			.name("Super combat potion")
			.complexBase(ImmutableSet.of(SUPER_ATTACK.potion, SUPER_STRENGTH.potion, SUPER_DEFENCE.potion))
			.primary(ItemID.TORSTOL)
			.primaryAlt(ItemID.UNIDENTIFIED_TORSTOL)
			.ids(ImmutableSet.of(ItemID._1DOSE2COMBAT, ItemID._2DOSE2COMBAT, ItemID._3DOSE2COMBAT, ItemID._4DOSE2COMBAT))
			.build()),

	FORGOTTEN_BREW(Potion.builder()
			.level(91)
			.name("Forgotten brew")
			.complexBase(ImmutableSet.of(ANCIENT_BREW.potion))
			.primary(ItemID.ANCIENT_ESSENCE)
			.ids(ImmutableSet.of(ItemID._1DOSEFORGOTTENBREW, ItemID._2DOSEFORGOTTENBREW, ItemID._3DOSEFORGOTTENBREW, ItemID._4DOSEFORGOTTENBREW))
			.build()),

	SUPER_ANTIFIRE_POTION(Potion.builder()
			.level(92)
			.name("Super antifire potion")
			.complexBase(ImmutableSet.of(ANTIFIRE_POTION.potion))
			.primary(ItemID.CRUSHED_DRAGON_BONES)
			.ids(ImmutableSet.of(ItemID._1DOSE3ANTIDRAGON, ItemID._2DOSE3ANTIDRAGON, ItemID._3DOSE3ANTIDRAGON, ItemID._4DOSE3ANTIDRAGON))
			.build()),

	ANTIVENOM_PLUS(Potion.builder()
			.level(94)
			.name("Anti-venom+")
			.complexBase(ImmutableSet.of(ANTIVENOM.potion))
			.primary(ItemID.TORSTOL)
			.grimyHerb(ItemID.UNIDENTIFIED_TORSTOL)
			.primarySeed(ItemID.TORSTOL_SEED)
			.ids(ImmutableSet.of(ItemID.ANTIVENOM_1, ItemID.ANTIVENOM_2, ItemID.ANTIVENOM_3, ItemID.ANTIVENOM_4))
			.build()),

	EXTENDED_ANTIVENOM_PLUS(Potion.builder()
			.level(94)
			.name("Extended anti-venom+")
			.complexBase(ImmutableSet.of(ANTIVENOM_PLUS.potion))
			.primary(ItemID.ARAXYTE_VENOM_SACK)
			.ids(ImmutableSet.of(ItemID.EXTENDED_ANTIVENOM_1, ItemID.EXTENDED_ANTIVENOM_2, ItemID.EXTENDED_ANTIVENOM_3, ItemID.EXTENDED_ANTIVENOM_4))
			.build()),

	DIVINE_SUPER_COMBAT(Potion.builder()
			.level(97)
			.name("Divine super combat potion")
			.complexBase(ImmutableSet.of(SUPER_COMBAT_POTION.potion))
			.primary(ItemID.SOTE_CRYSTAL_DUST)
			.primaryAlt(ItemID.PRIF_CRYSTAL_SHARD)
			.ids(ImmutableSet.of(ItemID._1DOSEDIVINECOMBAT, ItemID._2DOSEDIVINECOMBAT, ItemID._3DOSEDIVINECOMBAT, ItemID._4DOSEDIVINECOMBAT))
			.build()),

	EXTENDED_SUPER_ANTIFIRE(Potion.builder()
			.level(98)
			.name("Extended super antifire")
			.complexBase(ImmutableSet.of(ANTIFIRE_POTION.potion))
			.primary(ItemID.CRUSHED_DRAGON_BONES)
			.ids(ImmutableSet.of(ItemID._1DOSE4ANTIDRAGON, ItemID._2DOSE4ANTIDRAGON, ItemID._3DOSE4ANTIDRAGON, ItemID._4DOSE4ANTIDRAGON))
			.build()),

	// TARS
	GUAM_TAR(Potion.builder()
			.level(19)
			.name("Guam tar")
			.primary(ItemID.GUAM_LEAF)
			.grimyHerb(ItemID.UNIDENTIFIED_GUAM)
			.secondaries(ImmutableSet.of(ItemID.SWAMP_TAR))
			.ids(ImmutableSet.of(ItemID.SALAMANDER_TAR_GREEN))
			.build()),

	MARRENTILL_TAR(Potion.builder()
			.level(31)
			.name("Marrentill tar")
			.primary(ItemID.MARENTILL)
			.grimyHerb(ItemID.UNIDENTIFIED_MARENTILL)
			.secondaries(ImmutableSet.of(ItemID.SWAMP_TAR))
			.ids(ImmutableSet.of(ItemID.SALAMANDER_TAR_ORANGE))
			.build()),

	TARROMIN_TAR(Potion.builder()
			.level(39)
			.name("Tarromin tar")
			.primary(ItemID.TARROMIN)
			.grimyHerb(ItemID.UNIDENTIFIED_TARROMIN)
			.secondaries(ImmutableSet.of(ItemID.SWAMP_TAR))
			.ids(ImmutableSet.of(ItemID.SALAMANDER_TAR_RED))
			.build()),

	HARRALANDER_TAR(Potion.builder()
			.level(44)
			.name("Harralander tar")
			.primary(ItemID.HARRALANDER)
			.grimyHerb(ItemID.UNIDENTIFIED_HARRALANDER)
			.secondaries(ImmutableSet.of(ItemID.SWAMP_TAR))
			.ids(ImmutableSet.of(ItemID.SALAMANDER_TAR_BLACK))
			.build()),

	IRIT_TAR(Potion.builder()
			.level(55)
			.name("Irit tar")
			.primary(ItemID.IRIT_LEAF)
			.grimyHerb(ItemID.UNIDENTIFIED_IRIT)
			.secondaries(ImmutableSet.of(ItemID.SWAMP_TAR))
			.ids(ImmutableSet.of(ItemID.SALAMANDER_TAR_MOUNTAIN))
			.build()),

	// MIXOLOGY PASTES
	GUAM_LEAF_MOX(Potion.builder()
			.level(1)
			.name("Mox paste")
			.primary(ItemID.GUAM_LEAF)
			.grimyHerb(ItemID.UNIDENTIFIED_GUAM)
			.ids(ImmutableSet.of(ItemID.MM_MOX_PASTE))
			.build()),

	MARRENTILL_MOX(Potion.builder()
			.level(1)
			.name("Mox paste")
			.primary(ItemID.MARENTILL)
			.grimyHerb(ItemID.UNIDENTIFIED_MARENTILL)
			.ids(ImmutableSet.of(ItemID.MM_MOX_PASTE))
			.build()),

	TARROMIN_MOX(Potion.builder()
			.level(1)
			.name("Mox paste")
			.primary(ItemID.TARROMIN)
			.grimyHerb(ItemID.UNIDENTIFIED_TARROMIN)
			.ids(ImmutableSet.of(ItemID.MM_MOX_PASTE))
			.build()),

	HARRALANDER_MOX(Potion.builder()
			.level(1)
			.name("Mox paste")
			.primary(ItemID.HARRALANDER)
			.grimyHerb(ItemID.UNIDENTIFIED_HARRALANDER)
			.ids(ImmutableSet.of(ItemID.MM_MOX_PASTE))
			.build()),

	RANARR_WEED_LYE(Potion.builder()
			.level(1)
			.name("Lye paste")
			.primary(ItemID.RANARR_WEED)
			.grimyHerb(ItemID.UNIDENTIFIED_RANARR)
			.ids(ImmutableSet.of(ItemID.MM_LYE_PASTE))
			.build()),

	TOADFLAX_LYE(Potion.builder()
			.level(1)
			.name("Lye paste")
			.primary(ItemID.TOADFLAX)
			.grimyHerb(ItemID.UNIDENTIFIED_TOADFLAX)
			.ids(ImmutableSet.of(ItemID.MM_LYE_PASTE))
			.build()),

	IRIT_LEAF_AGA(Potion.builder()
			.level(1)
			.name("Aga paste")
			.primary(ItemID.IRIT_LEAF)
			.grimyHerb(ItemID.UNIDENTIFIED_IRIT)
			.ids(ImmutableSet.of(ItemID.MM_AGA_PASTE))
			.build()),

	AVANTOE_LYE(Potion.builder()
			.level(1)
			.name("Lye paste")
			.primary(ItemID.AVANTOE)
			.grimyHerb(ItemID.UNIDENTIFIED_AVANTOE)
			.ids(ImmutableSet.of(ItemID.MM_LYE_PASTE))
			.build()),

	KWUARM_LYE(Potion.builder()
			.level(1)
			.name("Lye paste")
			.primary(ItemID.KWUARM)
			.grimyHerb(ItemID.UNIDENTIFIED_KWUARM)
			.ids(ImmutableSet.of(ItemID.MM_LYE_PASTE))
			.build()),

	HUASCA_AGA(Potion.builder()
			.level(60)
			.name("Aga paste")
			.primary(ItemID.HUASCA)
			.grimyHerb(ItemID.UNIDENTIFIED_HUASCA)
			.ids(ImmutableSet.of(ItemID.MM_AGA_PASTE))
			.build()),

	SNAPDRAGON_LYE(Potion.builder()
			.level(1)
			.name("Lye paste")
			.primary(ItemID.SNAPDRAGON)
			.grimyHerb(ItemID.UNIDENTIFIED_SNAPDRAGON)
			.ids(ImmutableSet.of(ItemID.MM_LYE_PASTE))
			.build()),

	CADANTINE_AGA(Potion.builder()
			.level(1)
			.name("Aga paste")
			.primary(ItemID.CADANTINE)
			.grimyHerb(ItemID.UNIDENTIFIED_CADANTINE)
			.ids(ImmutableSet.of(ItemID.MM_AGA_PASTE))
			.build()),

	LANTADYME_AGA(Potion.builder()
			.level(1)
			.name("Aga paste")
			.primary(ItemID.LANTADYME)
			.grimyHerb(ItemID.UNIDENTIFIED_LANTADYME)
			.ids(ImmutableSet.of(ItemID.MM_AGA_PASTE))
			.build()),

	DWARF_WEED_AGA(Potion.builder()
			.level(60)
			.name("Aga paste")
			.primary(ItemID.DWARF_WEED)
			.grimyHerb(ItemID.UNIDENTIFIED_DWARF_WEED)
			.ids(ImmutableSet.of(ItemID.MM_AGA_PASTE))
			.build()),

	TORSTOL_AGA(Potion.builder()
			.level(1)
			.name("Aga paste")
			.primary(ItemID.TORSTOL)
			.grimyHerb(ItemID.UNIDENTIFIED_TORSTOL)
			.ids(ImmutableSet.of(ItemID.MM_AGA_PASTE))
			.build()),

	// NEW POTIONS (2025)
	SURGE_POTION(Potion.builder()
			.level(81)
			.name("Surge potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.TORSTOL)
			.grimyHerb(ItemID.UNIDENTIFIED_TORSTOL)
			.primarySeed(ItemID.TORSTOL_SEED)
			.secondaries(ImmutableSet.of(ItemID.DEMONIC_TALLOW))
			.unfinishedPotion(ItemID.TORSTOLVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSESURGE, ItemID._2DOSESURGE, ItemID._3DOSESURGE, ItemID._4DOSESURGE))
			.build()),

	// SAILING POTIONS
	HAEMOSTATIC_POULTICE(Potion.builder()
			.level(56)
			.name("Haemostatic poultice")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.CORAL_ELKHORN)
			.secondaries(ImmutableSet.of(ItemID.SQUID_PASTE))
			.unfinishedPotion(ItemID.ELKHORNVIAL)
			.ids(ImmutableSet.of(ItemID.HAEMOSTATIC_POULTICE))
			.build()),

	HAEMOSTATIC_DRESSING(Potion.builder()
			.level(56)
			.name("Haemostatic dressing")
			.complexBase(ImmutableSet.of(HAEMOSTATIC_POULTICE.potion))
			.secondaries(ImmutableSet.of(ItemID.COTTON_YARN))
			.ids(ImmutableSet.of(ItemID._1DOSEHAEMOSTATICDRESSING, ItemID._2DOSEHAEMOSTATICDRESSING, ItemID._3DOSEHAEMOSTATICDRESSING, ItemID._4DOSEHAEMOSTATICDRESSING))
			.build()),

	SUPER_FISHING_POTION(Potion.builder()
			.level(62)
			.name("Super fishing potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.CORAL_PILLAR)
			.secondaries(ImmutableSet.of(ItemID.HADDOCK_EYE))
			.unfinishedPotion(ItemID.PILLARVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE2FISHERSPOTION, ItemID._2DOSE2FISHERSPOTION, ItemID._3DOSE2FISHERSPOTION, ItemID._4DOSE2FISHERSPOTION))
			.build()),

	EXTREME_ENERGY_POTION(Potion.builder()
			.level(66)
			.name("Extreme energy potion")
			.complexBase(ImmutableSet.of(SUPER_ENERGY.potion))
			.secondaries(ImmutableSet.of(ItemID.YELLOW_FIN))
			.ids(ImmutableSet.of(ItemID._1DOSE3ENERGY, ItemID._2DOSE3ENERGY, ItemID._3DOSE3ENERGY, ItemID._4DOSE3ENERGY))
			.build()),

	SUPER_HUNTER_POTION(Potion.builder()
			.level(67)
			.name("Super hunter potion")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.CORAL_PILLAR)
			.secondaries(ImmutableSet.of(ItemID.CRAB_PASTE))
			.unfinishedPotion(ItemID.PILLARVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSE2HUNTING, ItemID._2DOSE2HUNTING, ItemID._3DOSE2HUNTING, ItemID._4DOSE2HUNTING))
			.build()),

	EXTENDED_STAMINA_POTION(Potion.builder()
			.level(85)
			.name("Extended stamina potion")
			.complexBase(ImmutableSet.of(STAMINA_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.MARLIN_SCALES))
			.ids(ImmutableSet.of(ItemID._1DOSE2STAMINA, ItemID._2DOSE2STAMINA, ItemID._3DOSE2STAMINA, ItemID._4DOSE2STAMINA))
			.build()),

	ARMADYL_BREW(Potion.builder()
			.level(89)
			.name("Armadyl brew")
			.basicBase(ItemID.VIAL_WATER)
			.primary(ItemID.CORAL_UMBRAL)
			.secondaries(ImmutableSet.of(ItemID.RAINBOW_CRAB_PASTE))
			.unfinishedPotion(ItemID.UMBRALVIAL)
			.ids(ImmutableSet.of(ItemID._1DOSEARMADYLBREW, ItemID._2DOSEARMADYLBREW, ItemID._3DOSEARMADYLBREW, ItemID._4DOSEARMADYLBREW))
			.build()),

	ANTI_ODOUR_SALT(Potion.builder()
			.level(49)
			.name("Anti-odour salt")
			.primary(ItemID.CORAL_ELKHORN)
			.secondaries(ImmutableSet.of(ItemID.CRAB_PASTE))
			.ids(ImmutableSet.of(ItemID.ANTI_ODOUR_SALT))
			.build()),

	// BARBARIAN MIXES
	ATTACK_MIX(Potion.builder()
			.level(4)
			.name("Attack mix")
			.complexBase(ImmutableSet.of(ATTACK_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_ROE))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE1ATTACK, ItemID.BRUTAL_2DOSE1ATTACK))
			.build()),

	ANTIPOISON_MIX(Potion.builder()
			.level(6)
			.name("Antipoison mix")
			.complexBase(ImmutableSet.of(ANTIPOISON.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_ROE))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSEANTIPOISON, ItemID.BRUTAL_2DOSEANTIPOISON))
			.build()),

	RELICYMS_MIX(Potion.builder()
			.level(9)
			.name("Relicym's mix")
			.complexBase(ImmutableSet.of(RELICYMS_BALM.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_ROE))
			.ids(ImmutableSet.of(ItemID.BRUTAL_RELICYMS_BALM1, ItemID.BRUTAL_RELICYMS_BALM2))
			.build()),

	STRENGTH_MIX(Potion.builder()
			.level(14)
			.name("Strength mix")
			.complexBase(ImmutableSet.of(STRENGTH_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_ROE))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE1STRENGTH, ItemID.BRUTAL_2DOSE1STRENGTH))
			.build()),

	RESTORE_MIX(Potion.builder()
			.level(24)
			.name("Restore mix")
			.complexBase(ImmutableSet.of(RESTORE_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_ROE))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSESTATRESTORE, ItemID.BRUTAL_2DOSESTATRESTORE))
			.build()),

	ENERGY_MIX(Potion.builder()
			.level(29)
			.name("Energy mix")
			.complexBase(ImmutableSet.of(ENERGY_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE1ENERGY, ItemID.BRUTAL_2DOSE1ENERGY))
			.build()),

	DEFENCE_MIX(Potion.builder()
			.level(33)
			.name("Defence mix")
			.complexBase(ImmutableSet.of(DEFENCE_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE1DEFENSE, ItemID.BRUTAL_2DOSE1DEFENSE))
			.build()),

	AGILITY_MIX(Potion.builder()
			.level(37)
			.name("Agility mix")
			.complexBase(ImmutableSet.of(AGILITY_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE1AGILITY, ItemID.BRUTAL_2DOSE1AGILITY))
			.build()),

	COMBAT_MIX(Potion.builder()
			.level(40)
			.name("Combat mix")
			.complexBase(ImmutableSet.of(COMBAT_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSECOMBAT, ItemID.BRUTAL_2DOSECOMBAT))
			.build()),

	PRAYER_MIX(Potion.builder()
			.level(42)
			.name("Prayer mix")
			.complexBase(ImmutableSet.of(PRAYER_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSEPRAYERRESTORE, ItemID.BRUTAL_2DOSEPRAYERRESTORE))
			.build()),

	SUPERATTACK_MIX(Potion.builder()
			.level(47)
			.name("Superattack mix")
			.complexBase(ImmutableSet.of(SUPER_ATTACK.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE2ATTACK, ItemID.BRUTAL_2DOSE2ATTACK))
			.build()),

	ANTIPOISON_SUPERMIX(Potion.builder()
			.level(51)
			.name("Anti-poison supermix")
			.complexBase(ImmutableSet.of(SUPERANTIPOISON.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE2ANTIPOISON, ItemID.BRUTAL_2DOSE2ANTIPOISON))
			.build()),

	FISHING_MIX(Potion.builder()
			.level(53)
			.name("Fishing mix")
			.complexBase(ImmutableSet.of(FISHING_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSEFISHERSPOTION, ItemID.BRUTAL_2DOSEFISHERSPOTION))
			.build()),

	SUPER_ENERGY_MIX(Potion.builder()
			.level(56)
			.name("Super energy mix")
			.complexBase(ImmutableSet.of(SUPER_ENERGY.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE2ENERGY, ItemID.BRUTAL_2DOSE2ENERGY))
			.build()),

	HUNTING_MIX(Potion.builder()
			.level(58)
			.name("Hunting mix")
			.complexBase(ImmutableSet.of(HUNTER_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE1HUNTING, ItemID.BRUTAL_2DOSE1HUNTING))
			.build()),

	SUPER_STR_MIX(Potion.builder()
			.level(59)
			.name("Super strength mix")
			.complexBase(ImmutableSet.of(SUPER_STRENGTH.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE2STRENGTH, ItemID.BRUTAL_2DOSE2STRENGTH))
			.build()),

	MAGIC_ESSENCE_MIX(Potion.builder()
			.level(61)
			.name("Magic essence mix")
			.complexBase(ImmutableSet.of(MAGIC_ESSENCE.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSEMAGICESS, ItemID.BRUTAL_2DOSEMAGICESS))
			.build()),

	SUPER_RESTORE_MIX(Potion.builder()
			.level(67)
			.name("Super restore mix")
			.complexBase(ImmutableSet.of(SUPER_RESTORE.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE2RESTORE, ItemID.BRUTAL_2DOSE2RESTORE))
			.build()),

	SUPER_DEF_MIX(Potion.builder()
			.level(71)
			.name("Super defence mix")
			.complexBase(ImmutableSet.of(SUPER_DEFENCE.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE2DEFENSE, ItemID.BRUTAL_2DOSE2DEFENSE))
			.build()),

	ANTIDOTE_MIX(Potion.builder()
			.level(74)
			.name("Antidote+ mix")
			.complexBase(ImmutableSet.of(ANTIDOTE_PLUS.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_ANTIDOTE_1, ItemID.BRUTAL_ANTIDOTE_2))
			.build()),

	ANTIFIRE_MIX(Potion.builder()
			.level(75)
			.name("Antifire mix")
			.complexBase(ImmutableSet.of(ANTIFIRE_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE1ANTIDRAGON, ItemID.BRUTAL_2DOSE1ANTIDRAGON))
			.build()),

	RANGING_MIX(Potion.builder()
			.level(80)
			.name("Ranging mix")
			.complexBase(ImmutableSet.of(RANGING_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSERANGERSPOTION, ItemID.BRUTAL_2DOSERANGERSPOTION))
			.build()),

	MAGIC_MIX(Potion.builder()
			.level(83)
			.name("Magic mix")
			.complexBase(ImmutableSet.of(MAGIC_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE1MAGIC, ItemID.BRUTAL_2DOSE1MAGIC))
			.build()),

	ZAMORAK_MIX(Potion.builder()
			.level(85)
			.name("Zamorak mix")
			.complexBase(ImmutableSet.of(ZAMORAK_BREW.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSEPOTIONOFZAMORAK, ItemID.BRUTAL_2DOSEPOTIONOFZAMORAK))
			.build()),

	STAMINA_MIX(Potion.builder()
			.level(86)
			.name("Stamina mix")
			.complexBase(ImmutableSet.of(STAMINA_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSESTAMINA, ItemID.BRUTAL_2DOSESTAMINA))
			.build()),

	EXTENDED_ANTIFIRE_MIX(Potion.builder()
			.level(91)
			.name("Extended antifire mix")
			.complexBase(ImmutableSet.of(EXTENDED_ANTIFIRE.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE2ANTIDRAGON, ItemID.BRUTAL_2DOSE2ANTIDRAGON))
			.build()),

	ANCIENT_MIX(Potion.builder()
			.level(92)
			.name("Ancient mix")
			.complexBase(ImmutableSet.of(ANCIENT_BREW.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSEANCIENTBREW, ItemID.BRUTAL_2DOSEANCIENTBREW))
			.build()),

	SUPER_ANTIFIRE_MIX(Potion.builder()
			.level(98)
			.name("Super antifire mix")
			.complexBase(ImmutableSet.of(SUPER_ANTIFIRE_POTION.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE3ANTIDRAGON, ItemID.BRUTAL_2DOSE3ANTIDRAGON))
			.build()),

	EXTENDED_SUPER_ANTIFIRE_MIX(Potion.builder()
			.level(99)
			.name("Extended super antifire mix")
			.complexBase(ImmutableSet.of(EXTENDED_SUPER_ANTIFIRE.potion))
			.secondaries(ImmutableSet.of(ItemID.BRUT_CAVIAR))
			.ids(ImmutableSet.of(ItemID.BRUTAL_1DOSE4ANTIDRAGON, ItemID.BRUTAL_2DOSE4ANTIDRAGON))
			.build());

	private static final Map<Integer, List<Potion>> primaryToPotion = new HashMap<>();
	private static final Map<Integer, List<Potion>> secondariesToPotion = new HashMap<>();
	private static final Map<Integer, List<Potion>> complexBaseToPotion = new HashMap<>();
	private static final Map<Integer, List<Potion>> unfinishedToPotion = new HashMap<>();
	private static final Map<Integer, List<Potion>> seedsToPotion = new HashMap<>();
	private static final Map<Integer, List<Potion>> grimyToPotion = new HashMap<>();

	private static final Map<Integer, List<Potion>> allIdsToPotion = new HashMap<>();

	private static final Set<Integer> allPotionIds = new HashSet<>();

	static
	{
		buildPrimariesAndGrimyMaps();
		buildSecondariesMap();
		buildComplexBaseMap();
		buildUnfinishedMap();
		buildSeedsMap();
		allIdsToPotion.putAll(primaryToPotion);
		allIdsToPotion.putAll(grimyToPotion);
		allIdsToPotion.putAll(secondariesToPotion);
		allIdsToPotion.putAll(complexBaseToPotion);
		allIdsToPotion.putAll(unfinishedToPotion);
		allIdsToPotion.putAll(seedsToPotion);
		allPotionIds.addAll(Arrays.stream(values()).map(p -> p.potion.getIds()).flatMap(Set::stream).collect(Collectors.toSet()));
	}

	public final Potion potion;

	Potions(Potion potion)
	{
		this.potion = potion;
	}

	public static Potion getPotion(int potionId)
	{
		return Arrays.stream(values())
				.map(p -> p.potion)
				.filter(potion -> potion.getIds().contains(potionId))
				.findFirst()
				.orElse(null);
	}

	public static List<Potion> getPastes(int pasteId)
	{
		return Arrays.stream(values())
				.map(p -> p.potion)
				.filter(p -> p.getName().contains(" paste"))
				.filter(p -> p.getIds().contains(pasteId))
				.collect(Collectors.toList());
	}

	private static void buildPrimariesAndGrimyMaps()
	{
		Arrays.stream(values()).map(p -> p.potion).forEach(p -> {
			// handle Imp Repellent
			if (IMP_REPELLENT.potion.equals(p))
			{
				// use this potion's secondaries as primary ingredients
				p.getSecondaries().forEach(secondary -> primaryToPotion.put(secondary, ImmutableList.of(p)));
				return;
			}

			// get primary ingredient id
			int primaryId = p.getPrimary();
			primaryToPotion.computeIfAbsent(primaryId, id -> new ArrayList<>()).add(p);
			// get alternate primary id in case it exists
			int altPrimaryId = p.getPrimaryAlt();
			if (altPrimaryId > 0)
			{
				primaryToPotion.computeIfAbsent(altPrimaryId, id -> new ArrayList<>()).add(p);
			}
			// also build grimy map
			int grimyHerbId = p.getGrimyHerb();
			if (grimyHerbId > 0)
			{
				grimyToPotion.computeIfAbsent(grimyHerbId, id -> new ArrayList<>()).add(p);
			}
		});
	}

	private static void buildSeedsMap()
	{
		Arrays.stream(values()).map(p -> p.potion).forEach(p -> {
			// also use herb seeds as primaries
			int seedId = p.getPrimarySeed();
			if (seedId > 0)
			{
				seedsToPotion.computeIfAbsent(seedId, id -> new ArrayList<>()).add(p);
			}
		});
	}

	private static void buildSecondariesMap()
	{
		Arrays.stream(values()).map(p -> p.potion)
				.filter(p -> p.getSecondaries() != null && !IMP_REPELLENT.potion.equals(p))
				.forEach(p -> p.getSecondaries()
						.forEach(id -> secondariesToPotion.computeIfAbsent(id, i -> new ArrayList<>()).add(p)));
		Arrays.stream(values()).map(p -> p.potion)
				.filter(p -> p.getSecondariesAlt() != null)
				.forEach(p -> p.getSecondariesAlt()
						.forEach(id -> secondariesToPotion.computeIfAbsent(id, i -> new ArrayList<>()).add(p)));
	}

	private static void buildComplexBaseMap()
	{
		Arrays.stream(values()).map(p -> p.potion)
				.filter(p -> p.getComplexBase() != null)
				.forEach(p -> p.getComplexBase()
						.forEach(base -> base.getIds()
								.forEach(baseId -> complexBaseToPotion.computeIfAbsent(baseId, i -> new ArrayList<>()).add(p))));
	}

	private static void buildUnfinishedMap()
	{
		Arrays.stream(values()).map(p -> p.potion)
				.filter(p -> p.getUnfinishedPotion() > 0)
				.forEach(p -> unfinishedToPotion.computeIfAbsent(p.getUnfinishedPotion(), id -> new ArrayList<>()).add(p));
	}

	public static boolean isPrimary(int id)
	{
		return primaryToPotion.containsKey(id);
	}

	public static boolean isSecondary(int id)
	{
		return secondariesToPotion.containsKey(id);
	}

	public static boolean isComplexBase(int id)
	{
		return complexBaseToPotion.containsKey(id);
	}

	public static boolean isSeed(int id)
	{
		return seedsToPotion.containsKey(id);
	}

	public static boolean isUnfinished(int id)
	{
		return unfinishedToPotion.containsKey(id);
	}

	public static boolean isPotion(int id)
	{
		return allPotionIds.contains(id);
	}

	public static boolean isGrimy(int id)
	{
		return grimyToPotion.containsKey(id);
	}

	public static Set<Integer> allIds()
	{
		Set<Integer> allIds = new HashSet<>();
		allIds.addAll(allIdsToPotion.keySet());
		allIds.addAll(allPotionIds);
		return allIds;
	}

	public static List<Potion> getByPrimary(int id)
	{
		return primaryToPotion.get(id);
	}

	public static List<Potion> getBySeed(int id)
	{
		return seedsToPotion.get(id);
	}

	public static List<Potion> getBySecondary(int id)
	{
		return secondariesToPotion.get(id);
	}

	public static List<Potion> getByComplex(int id)
	{
		return complexBaseToPotion.get(id);
	}

	public static List<Potion> getByUnfinished(int id)
	{
		return unfinishedToPotion.get(id);
	}

	public static List<Potion> getByGrimy(int id)
	{
		return grimyToPotion.get(id);
	}
}

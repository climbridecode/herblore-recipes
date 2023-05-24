package com.herblorerecipes.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.runelite.api.ItemID;

public enum Potions
{

	// BASIC POTIONS
	// Special case - only potion with many primary ingredients - using secondaryIngredients to support this.
	IMP_REPELLENT(Potion.builder()
		.level(3)
		.name("Imp repellent")
		.basicBase(ItemID.ANCHOVY_OIL)
		.secondaries(Set.of(ItemID.MARIGOLDS, ItemID.ROSEMARY, ItemID.NASTURTIUMS, ItemID.ASSORTED_FLOWERS,
			ItemID.RED_FLOWERS, ItemID.BLUE_FLOWERS, ItemID.YELLOW_FLOWERS, ItemID.PURPLE_FLOWERS,
			ItemID.ORANGE_FLOWERS, ItemID.MIXED_FLOWERS, ItemID.BLACK_FLOWERS, ItemID.WHITE_FLOWERS))
		.ids(Set.of(ItemID.IMP_REPELLENT))
		.build()),

	ATTACK_POTION(Potion.builder()
		.level(3)
		.name("Attack potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.GUAM_LEAF)
		.grimyHerb(ItemID.GRIMY_GUAM_LEAF)
		.primarySeed(ItemID.GUAM_SEED)
		.secondaries(Set.of(ItemID.EYE_OF_NEWT))
		.unfinishedPotion(ItemID.GUAM_POTION_UNF)
		.ids(Set.of(ItemID.ATTACK_POTION1, ItemID.ATTACK_POTION2, ItemID.ATTACK_POTION3, ItemID.ATTACK_POTION4))
		.build()),

	ANTIPOISON(Potion.builder()
		.level(5)
		.name("Antipoison")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.MARRENTILL)
		.grimyHerb(ItemID.GRIMY_MARRENTILL)
		.primarySeed(ItemID.MARRENTILL_SEED)
		.secondaries(Set.of(ItemID.UNICORN_HORN_DUST))
		.secondariesAlt(Set.of(ItemID.UNICORN_HORN))
		.unfinishedPotion(ItemID.MARRENTILL_POTION_UNF)
		.ids(Set.of(ItemID.ANTIPOISON1, ItemID.ANTIPOISON2, ItemID.ANTIPOISON3, ItemID.ANTIPOISON4))
		.build()),

	RELICYMS_BALM(Potion.builder()
		.level(8)
		.name("Relicym's balm")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.ROGUES_PURSE)
		.grimyHerb(ItemID.GRIMY_ROGUES_PURSE)
		.secondaries(Set.of(ItemID.SNAKE_WEED))
		.secondariesAlt(Set.of(ItemID.GRIMY_SNAKE_WEED))
		.unfinishedPotion(ItemID.UNFINISHED_POTION_4840)
		.ids(Set.of(ItemID.RELICYMS_BALM1, ItemID.RELICYMS_BALM2, ItemID.RELICYMS_BALM3, ItemID.RELICYMS_BALM4))
		.build()),

	STRENGTH_POTION(Potion.builder()
		.level(12)
		.name("Strength potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.TARROMIN)
		.grimyHerb(ItemID.GRIMY_TARROMIN)
		.primarySeed(ItemID.TARROMIN_SEED)
		.secondaries(Set.of(ItemID.LIMPWURT_ROOT))
		.secondariesAlt(Set.of(ItemID.LIMPWURT_SEED))
		.unfinishedPotion(ItemID.TARROMIN_POTION_UNF)
		.ids(Set.of(ItemID.STRENGTH_POTION1, ItemID.STRENGTH_POTION2, ItemID.STRENGTH_POTION3, ItemID.STRENGTH_POTION4))
		.build()),

	SERUM_207(Potion.builder()
		.level(15)
		.name("Serum 207")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.TARROMIN)
		.grimyHerb(ItemID.GRIMY_TARROMIN)
		.primarySeed(ItemID.TARROMIN_SEED)
		.secondaries(Set.of(ItemID.ASHES))
		.unfinishedPotion(ItemID.TARROMIN_POTION_UNF)
		.ids(Set.of(ItemID.SERUM_207_1, ItemID.SERUM_207_2, ItemID.SERUM_207_3, ItemID.SERUM_207_4))
		.build()),

	GUTHIX_REST_TEA(Potion.builder()
		.level(18)
		.name("Guthix rest tea")
		.basicBase(ItemID.CUP_OF_HOT_WATER)
		.primary(ItemID.HARRALANDER)
		.grimyHerb(ItemID.GRIMY_HARRALANDER)
		.primarySeed(ItemID.HARRALANDER_SEED)
		.secondaries(Set.of(ItemID.GUAM_LEAF, ItemID.MARRENTILL))
		.secondariesAlt(Set.of(ItemID.GRIMY_GUAM_LEAF, ItemID.GRIMY_MARRENTILL))
		.unfinishedPotion(ItemID.HARRALANDER_POTION_UNF)
		.ids(Set.of(ItemID.GUTHIX_REST1, ItemID.GUTHIX_REST2, ItemID.GUTHIX_REST3, ItemID.GUTHIX_REST4))
		.build()),

	COMPOST_POTION(Potion.builder()
		.level(22)
		.name("Compost potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.HARRALANDER)
		.grimyHerb(ItemID.GRIMY_HARRALANDER)
		.primarySeed(ItemID.HARRALANDER_SEED)
		.secondaries(Set.of(ItemID.VOLCANIC_ASH))
		.unfinishedPotion(ItemID.HARRALANDER_POTION_UNF)
		.ids(Set.of(ItemID.COMPOST_POTION1, ItemID.COMPOST_POTION2, ItemID.COMPOST_POTION3, ItemID.COMPOST_POTION4))
		.build()),

	RESTORE_POTION(Potion.builder()
		.level(22)
		.name("Restore potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.HARRALANDER)
		.grimyHerb(ItemID.GRIMY_HARRALANDER)
		.primarySeed(ItemID.HARRALANDER_SEED)
		.secondaries(Set.of(ItemID.RED_SPIDERS_EGGS))
		.unfinishedPotion(ItemID.HARRALANDER_POTION_UNF)
		.ids(Set.of(ItemID.RESTORE_POTION1, ItemID.RESTORE_POTION2, ItemID.RESTORE_POTION3, ItemID.RESTORE_POTION4))
		.build()),

	BLAMISH_OIL(Potion.builder()
		.level(25)
		.name("Blamish oil")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.HARRALANDER)
		.grimyHerb(ItemID.GRIMY_HARRALANDER)
		.primarySeed(ItemID.HARRALANDER_SEED)
		.secondaries(Set.of(ItemID.BLAMISH_SNAIL_SLIME))
		.unfinishedPotion(ItemID.HARRALANDER_POTION_UNF)
		.ids(Set.of(ItemID.BLAMISH_OIL))
		.build()),

	ENERGY_POTION(Potion.builder()
		.level(26)
		.name("Energy potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.HARRALANDER)
		.grimyHerb(ItemID.GRIMY_HARRALANDER)
		.primarySeed(ItemID.HARRALANDER_SEED)
		.secondaries(Set.of(ItemID.CHOCOLATE_DUST))
		.secondariesAlt(Set.of(ItemID.CHOCOLATE_BAR))
		.unfinishedPotion(ItemID.HARRALANDER_POTION_UNF)
		.ids(Set.of(ItemID.ENERGY_POTION1, ItemID.ENERGY_POTION2, ItemID.ENERGY_POTION3, ItemID.ENERGY_POTION4))
		.build()),

	DEFENCE_POTION(Potion.builder()
		.level(30)
		.name("Defence potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.RANARR_WEED)
		.grimyHerb(ItemID.GRIMY_RANARR_WEED)
		.primarySeed(ItemID.RANARR_SEED)
		.secondaries(Set.of(ItemID.WHITE_BERRIES))
		.unfinishedPotion(ItemID.RANARR_POTION_UNF)
		.ids(Set.of(ItemID.DEFENCE_POTION1, ItemID.DEFENCE_POTION2, ItemID.DEFENCE_POTION3, ItemID.DEFENCE_POTION4))
		.build()),

	AGILITY_POTION(Potion.builder()
		.level(34)
		.name("Agility potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.TOADFLAX)
		.grimyHerb(ItemID.GRIMY_TOADFLAX)
		.primarySeed(ItemID.TOADFLAX_SEED)
		.secondaries(Set.of(ItemID.TOADS_LEGS))
		.unfinishedPotion(ItemID.TOADFLAX_POTION_UNF)
		.ids(Set.of(ItemID.AGILITY_POTION1, ItemID.AGILITY_POTION2, ItemID.AGILITY_POTION3, ItemID.AGILITY_POTION4))
		.build()),

	COMBAT_POTION(Potion.builder()
		.level(36)
		.name("Combat potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.HARRALANDER)
		.grimyHerb(ItemID.GRIMY_HARRALANDER)
		.primarySeed(ItemID.HARRALANDER_SEED)
		.secondaries(Set.of(ItemID.GOAT_HORN_DUST))
		.secondariesAlt(Set.of(ItemID.DESERT_GOAT_HORN))
		.unfinishedPotion(ItemID.HARRALANDER_POTION_UNF)
		.ids(Set.of(ItemID.COMBAT_POTION1, ItemID.COMBAT_POTION2, ItemID.COMBAT_POTION3, ItemID.COMBAT_POTION4))
		.build()),

	PRAYER_POTION(Potion.builder()
		.level(38)
		.name("Prayer potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.RANARR_WEED)
		.grimyHerb(ItemID.GRIMY_RANARR_WEED)
		.primarySeed(ItemID.RANARR_SEED)
		.secondaries(Set.of(ItemID.SNAPE_GRASS))
		.secondariesAlt(Set.of(ItemID.SNAPE_GRASS_SEED))
		.unfinishedPotion(ItemID.RANARR_POTION_UNF)
		.ids(Set.of(ItemID.PRAYER_POTION1, ItemID.PRAYER_POTION2, ItemID.PRAYER_POTION3, ItemID.PRAYER_POTION4))
		.build()),

	SUPER_ATTACK(Potion.builder()
		.level(45)
		.name("Super attack")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.IRIT_LEAF)
		.grimyHerb(ItemID.GRIMY_IRIT_LEAF)
		.primarySeed(ItemID.IRIT_SEED)
		.secondaries(Set.of(ItemID.EYE_OF_NEWT))
		.unfinishedPotion(ItemID.IRIT_POTION_UNF)
		.ids(Set.of(ItemID.SUPER_ATTACK1, ItemID.SUPER_ATTACK2, ItemID.SUPER_ATTACK3, ItemID.SUPER_ATTACK4))
		.build()),

	GOBLIN_POTION(Potion.builder()
		.level(47)
		.name("Goblin potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.TOADFLAX)
		.grimyHerb(ItemID.GRIMY_TOADFLAX)
		.primarySeed(ItemID.TOADFLAX_SEED)
		.secondaries(Set.of(ItemID.PHARMAKOS_BERRIES))
		.unfinishedPotion(ItemID.TOADFLAX_POTION_UNF)
		.ids(Set.of(ItemID.GOBLIN_POTION1, ItemID.GOBLIN_POTION2, ItemID.GOBLIN_POTION3, ItemID.GOBLIN_POTION4))
		.build()),

	SUPERANTIPOISON(Potion.builder()
		.level(48)
		.name("Superantipoison")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.IRIT_LEAF)
		.grimyHerb(ItemID.GRIMY_IRIT_LEAF)
		.primarySeed(ItemID.IRIT_SEED)
		.secondaries(Set.of(ItemID.UNICORN_HORN))
		.secondariesAlt(Set.of(ItemID.UNICORN_HORN_DUST))
		.unfinishedPotion(ItemID.IRIT_POTION_UNF)
		.ids(Set.of(ItemID.SUPERANTIPOISON1, ItemID.SUPERANTIPOISON2, ItemID.SUPERANTIPOISON3, ItemID.SUPERANTIPOISON4))
		.build()),

	FISHING_POTION(Potion.builder()
		.level(50)
		.name("Fishing potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.AVANTOE)
		.grimyHerb(ItemID.GRIMY_AVANTOE)
		.primarySeed(ItemID.AVANTOE_SEED)
		.secondaries(Set.of(ItemID.SNAPE_GRASS))
		.secondariesAlt(Set.of(ItemID.SNAPE_GRASS_SEED))
		.unfinishedPotion(ItemID.AVANTOE_POTION_UNF)
		.ids(Set.of(ItemID.FISHING_POTION1, ItemID.FISHING_POTION2, ItemID.FISHING_POTION3, ItemID.FISHING_POTION4))
		.build()),

	SUPER_ENERGY(Potion.builder()
		.level(52)
		.name("Super energy")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.AVANTOE)
		.grimyHerb(ItemID.GRIMY_AVANTOE)
		.primarySeed(ItemID.AVANTOE_SEED)
		.secondaries(Set.of(ItemID.MORT_MYRE_FUNGUS))
		.unfinishedPotion(ItemID.AVANTOE_POTION_UNF)
		.ids(Set.of(ItemID.SUPER_ENERGY1, ItemID.SUPER_ENERGY2, ItemID.SUPER_ENERGY3, ItemID.SUPER_ENERGY4))
		.build()),

	SHRINK_ME_QUICK(Potion.builder()
		.level(52)
		.name("Shrink-me-quick")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.TARROMIN)
		.grimyHerb(ItemID.GRIMY_TARROMIN)
		.primarySeed(ItemID.TARROMIN_SEED)
		.secondaries(Set.of(ItemID.SHRUNK_OGLEROOT))
		.unfinishedPotion(ItemID.TARROMIN_POTION_UNF)
		.ids(Set.of(ItemID.SHRINKMEQUICK))
		.build()),

	HUNTER_POTION(Potion.builder()
		.level(53)
		.name("Hunter potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.AVANTOE)
		.grimyHerb(ItemID.GRIMY_AVANTOE)
		.primarySeed(ItemID.AVANTOE_SEED)
		.secondaries(Set.of(ItemID.KEBBIT_TEETH_DUST))
		.secondariesAlt(Set.of(ItemID.KEBBIT_TEETH))
		.unfinishedPotion(ItemID.AVANTOE_POTION_UNF)
		.ids(Set.of(ItemID.HUNTER_POTION1, ItemID.HUNTER_POTION2, ItemID.HUNTER_POTION3, ItemID.HUNTER_POTION4))
		.build()),

	SUPER_STRENGTH(Potion.builder()
		.level(55)
		.name("Super strength")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.KWUARM)
		.grimyHerb(ItemID.GRIMY_KWUARM)
		.primarySeed(ItemID.KWUARM_SEED)
		.secondaries(Set.of(ItemID.LIMPWURT_ROOT))
		.secondariesAlt(Set.of(ItemID.LIMPWURT_SEED))
		.unfinishedPotion(ItemID.KWUARM_POTION_UNF)
		.ids(Set.of(ItemID.SUPER_STRENGTH1, ItemID.SUPER_STRENGTH2, ItemID.SUPER_STRENGTH3, ItemID.SUPER_STRENGTH4))
		.build()),

	MAGIC_ESSENCE(Potion.builder()
		.level(57)
		.name("Magic essence")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.STAR_FLOWER)
		.secondaries(Set.of(ItemID.GORAK_CLAW_POWDER))
		.secondariesAlt(Set.of(ItemID.GORAK_CLAWS))
		.unfinishedPotion(ItemID.MAGIC_ESSENCE_UNF)
		.ids(Set.of(ItemID.MAGIC_ESSENCE1, ItemID.MAGIC_ESSENCE2, ItemID.MAGIC_ESSENCE3, ItemID.MAGIC_ESSENCE4))
		.build()),

	WEAPON_POISON(Potion.builder()
		.level(60)
		.name("Weapon poison")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.KWUARM)
		.grimyHerb(ItemID.GRIMY_KWUARM)
		.primarySeed(ItemID.KWUARM_SEED)
		.secondaries(Set.of(ItemID.DRAGON_SCALE_DUST))
		.secondariesAlt(Set.of(ItemID.BLUE_DRAGON_SCALE))
		.unfinishedPotion(ItemID.KWUARM_POTION_UNF)
		.ids(Set.of(ItemID.WEAPON_POISON))
		.build()),

	SUPER_RESTORE(Potion.builder()
		.level(63)
		.name("Super restore")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.SNAPDRAGON)
		.grimyHerb(ItemID.GRIMY_SNAPDRAGON)
		.primarySeed(ItemID.SNAPDRAGON_SEED)
		.secondaries(Set.of(ItemID.RED_SPIDERS_EGGS))
		.unfinishedPotion(ItemID.SNAPDRAGON_POTION_UNF)
		.ids(Set.of(ItemID.SUPER_RESTORE1, ItemID.SUPER_RESTORE2, ItemID.SUPER_RESTORE3, ItemID.SUPER_RESTORE4))
		.build()),

	SUPER_DEFENCE(Potion.builder()
		.level(66)
		.name("Super defence")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.CADANTINE)
		.grimyHerb(ItemID.GRIMY_CADANTINE)
		.primarySeed(ItemID.CADANTINE_SEED)
		.secondaries(Set.of(ItemID.WHITE_BERRIES))
		.unfinishedPotion(ItemID.CADANTINE_POTION_UNF)
		.ids(Set.of(ItemID.SUPER_DEFENCE1, ItemID.SUPER_DEFENCE2, ItemID.SUPER_DEFENCE3, ItemID.SUPER_DEFENCE4))
		.build()),

	ANTIDOTE_PLUS(Potion.builder()
		.level(68)
		.name("Antidote+")
		.basicBase(ItemID.COCONUT_MILK)
		.primary(ItemID.TOADFLAX)
		.grimyHerb(ItemID.GRIMY_TOADFLAX)
		.primarySeed(ItemID.TOADFLAX_SEED)
		.secondaries(Set.of(ItemID.YEW_ROOTS))
		.unfinishedPotion(ItemID.TOADFLAX_POTION_UNF)
		.ids(Set.of(ItemID.ANTIDOTE1, ItemID.ANTIDOTE2, ItemID.ANTIDOTE3, ItemID.ANTIDOTE4))
		.build()),

	ANTIFIRE_POTION(Potion.builder()
		.level(69)
		.name("Antifire potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.LANTADYME)
		.grimyHerb(ItemID.GRIMY_LANTADYME)
		.primarySeed(ItemID.LANTADYME_SEED)
		.secondaries(Set.of(ItemID.DRAGON_SCALE_DUST))
		.secondariesAlt(Set.of(ItemID.BLUE_DRAGON_SCALE))
		.unfinishedPotion(ItemID.LANTADYME_POTION_UNF)
		.ids(Set.of(ItemID.ANTIFIRE_POTION1, ItemID.ANTIFIRE_POTION2, ItemID.ANTIFIRE_POTION3, ItemID.ANTIFIRE_POTION4))
		.build()),

	RANGING_POTION(Potion.builder()
		.level(72)
		.name("Ranging potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.DWARF_WEED)
		.grimyHerb(ItemID.GRIMY_DWARF_WEED)
		.primarySeed(ItemID.DWARF_WEED_SEED)
		.secondaries(Set.of(ItemID.WINE_OF_ZAMORAK))
		.unfinishedPotion(ItemID.DWARF_WEED_POTION_UNF)
		.ids(Set.of(ItemID.RANGING_POTION1, ItemID.RANGING_POTION2, ItemID.RANGING_POTION3, ItemID.RANGING_POTION4))
		.build()),

	WEAPON_POISON_PLUS(Potion.builder()
		.level(73)
		.name("Weapon poison+")
		.basicBase(ItemID.COCONUT_MILK)
		.primary(ItemID.CACTUS_SPINE)
		.secondaries(Set.of(ItemID.RED_SPIDERS_EGGS))
		.unfinishedPotion(ItemID.WEAPON_POISON_UNF)
		.ids(Set.of(ItemID.WEAPON_POISON_5937))
		.build()),

	MAGIC_POTION(Potion.builder()
		.level(76)
		.name("Magic potion")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.LANTADYME)
		.grimyHerb(ItemID.GRIMY_LANTADYME)
		.primarySeed(ItemID.LANTADYME_SEED)
		.secondaries(Set.of(ItemID.POTATO_CACTUS))
		.secondariesAlt(Set.of(ItemID.POTATO_CACTUS_SEED))
		.unfinishedPotion(ItemID.LANTADYME_POTION_UNF)
		.ids(Set.of(ItemID.MAGIC_POTION1, ItemID.MAGIC_POTION2, ItemID.MAGIC_POTION3, ItemID.MAGIC_POTION4))
		.build()),

	ZAMORAK_BREW(Potion.builder()
		.level(78)
		.name("Zamorak brew")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.TORSTOL)
		.grimyHerb(ItemID.GRIMY_TORSTOL)
		.primarySeed(ItemID.TORSTOL_SEED)
		.secondaries(Set.of(ItemID.JANGERBERRIES))
		.secondariesAlt(Set.of(ItemID.JANGERBERRY_SEED))
		.unfinishedPotion(ItemID.TORSTOL_POTION_UNF)
		.ids(Set.of(ItemID.ZAMORAK_BREW1, ItemID.ZAMORAK_BREW2, ItemID.ZAMORAK_BREW3, ItemID.ZAMORAK_BREW4))
		.build()),

	ANTIDOTE_PLUS2(Potion.builder()
		.level(79)
		.name("Antidote++")
		.basicBase(ItemID.COCONUT_MILK)
		.primary(ItemID.IRIT_LEAF)
		.grimyHerb(ItemID.GRIMY_IRIT_LEAF)
		.primarySeed(ItemID.IRIT_SEED)
		.secondaries(Set.of(ItemID.MAGIC_ROOTS))
		.unfinishedPotion(ItemID.IRIT_POTION_UNF)
		.ids(Set.of(ItemID.ANTIDOTE1_5958, ItemID.ANTIDOTE2_5956, ItemID.ANTIDOTE3_5954, ItemID.ANTIDOTE4_5952))
		.build()),

	BASTION_POTION(Potion.builder()
		.level(80)
		.name("Bastion potion")
		.basicBase(ItemID.VIAL_OF_BLOOD)
		.primary(ItemID.CADANTINE)
		.grimyHerb(ItemID.GRIMY_CADANTINE)
		.primarySeed(ItemID.CADANTINE_SEED)
		.secondaries(Set.of(ItemID.WINE_OF_ZAMORAK))
		.unfinishedPotion(ItemID.CADANTINE_BLOOD_POTION_UNF)
		.ids(Set.of(ItemID.BASTION_POTION1, ItemID.BASTION_POTION2, ItemID.BASTION_POTION3, ItemID.BASTION_POTION4))
		.build()),

	BATTLEMAGE_POTION(Potion.builder()
		.level(80)
		.name("Battlemage potion")
		.basicBase(ItemID.VIAL_OF_BLOOD)
		.primary(ItemID.CADANTINE)
		.grimyHerb(ItemID.GRIMY_CADANTINE)
		.primarySeed(ItemID.CADANTINE_SEED)
		.secondaries(Set.of(ItemID.POTATO_CACTUS))
		.secondariesAlt(Set.of(ItemID.POTATO_CACTUS_SEED))
		.unfinishedPotion(ItemID.CADANTINE_BLOOD_POTION_UNF)
		.ids(Set.of(ItemID.BATTLEMAGE_POTION1, ItemID.BATTLEMAGE_POTION2, ItemID.BATTLEMAGE_POTION3, ItemID.BATTLEMAGE_POTION4))
		.build()),

	SARADOMIN_BREW(Potion.builder()
		.level(81)
		.name("Saradomin brew")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.TOADFLAX)
		.grimyHerb(ItemID.GRIMY_TOADFLAX)
		.primarySeed(ItemID.TOADFLAX_SEED)
		.secondaries(Set.of(ItemID.CRUSHED_NEST))
		.unfinishedPotion(ItemID.TOADFLAX_POTION_UNF)
		.ids(Set.of(ItemID.SARADOMIN_BREW1, ItemID.SARADOMIN_BREW2, ItemID.SARADOMIN_BREW3, ItemID.SARADOMIN_BREW4))
		.build()),

	WEAPON_POISON_PLUS2(Potion.builder()
		.level(82)
		.name("Weapon poison++")
		.basicBase(ItemID.COCONUT_MILK)
		.primary(ItemID.CAVE_NIGHTSHADE)
		.primaryAlt(ItemID.NIGHTSHADE)
		.secondaries(Set.of(ItemID.POISON_IVY_BERRIES))
		.secondariesAlt(Set.of(ItemID.POISON_IVY_SEED))
		.unfinishedPotion(ItemID.WEAPON_POISON_UNF_5939)
		.ids(Set.of(ItemID.WEAPON_POISON_5940))
		.build()),

	ANCIENT_BREW(Potion.builder()
		.level(85)
		.name("Ancient brew")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.DWARF_WEED)
		.grimyHerb(ItemID.GRIMY_DWARF_WEED)
		.primarySeed(ItemID.DWARF_WEED_SEED)
		.secondaries(Set.of(ItemID.NIHIL_DUST))
		.secondariesAlt(Set.of(ItemID.NIHIL_SHARD))
		.unfinishedPotion(ItemID.DWARF_WEED_POTION_UNF)
		.ids(Set.of(ItemID.ANCIENT_BREW1, ItemID.ANCIENT_BREW2, ItemID.ANCIENT_BREW3, ItemID.ANCIENT_BREW4))
		.build()),

	MENAPHITE_REMEDY(Potion.builder()
		.level(88)
		.name("Menaphite Remedy")
		.basicBase(ItemID.VIAL_OF_WATER)
		.primary(ItemID.DWARF_WEED)
		.grimyHerb(ItemID.GRIMY_DWARF_WEED)
		.primarySeed(ItemID.DWARF_WEED_SEED)
		.secondaries(Set.of(ItemID.LILY_OF_THE_SANDS))
		.unfinishedPotion(ItemID.DWARF_WEED_POTION_UNF)
		.ids(Set.of(ItemID.MENAPHITE_REMEDY1, ItemID.MENAPHITE_REMEDY2, ItemID.MENAPHITE_REMEDY3, ItemID.MENAPHITE_REMEDY4))
		.build()),

	// COMPLEX POTIONS
	GUTHIX_BALANCE(Potion.builder()
		.level(22)
		.name("Guthix balance")
		.complexBase(Set.of(RESTORE_POTION.potion))
		.primary(ItemID.GARLIC)
		.secondaries(Set.of(ItemID.SILVER_DUST))
		.ids(Set.of(ItemID.GUTHIX_BALANCE1, ItemID.GUTHIX_BALANCE2, ItemID.GUTHIX_BALANCE3, ItemID.GUTHIX_BALANCE4))
		.build()),

	SANFEW_SERUM(Potion.builder()
		.level(65)
		.name("Sanfew serum")
		.complexBase(Set.of(SUPER_RESTORE.potion))
		.primary(ItemID.UNICORN_HORN_DUST)
		.primaryAlt(ItemID.UNICORN_HORN)
		.secondaries(Set.of(ItemID.SNAKE_WEED, ItemID.NAIL_BEAST_NAILS))
		.ids(Set.of(ItemID.SANFEW_SERUM1, ItemID.SANFEW_SERUM2, ItemID.SANFEW_SERUM3, ItemID.SANFEW_SERUM4))
		.build()),

	DIVINE_SUPER_ATTACK(Potion.builder()
		.level(70)
		.name("Divine super attack potion")
		.complexBase(Set.of(SUPER_ATTACK.potion))
		.primary(ItemID.CRYSTAL_DUST)
		.primaryAlt(ItemID.CRYSTAL_SHARD)
		.ids(Set.of(ItemID.DIVINE_SUPER_ATTACK_POTION1, ItemID.DIVINE_SUPER_ATTACK_POTION2, ItemID.DIVINE_SUPER_ATTACK_POTION3, ItemID.DIVINE_SUPER_ATTACK_POTION4))
		.build()),

	DIVINE_SUPER_DEFENCE(Potion.builder()
		.level(70)
		.name("Divine super defence potion")
		.complexBase(Set.of(SUPER_DEFENCE.potion))
		.primary(ItemID.CRYSTAL_DUST)
		.primaryAlt(ItemID.CRYSTAL_SHARD)
		.ids(Set.of(ItemID.DIVINE_SUPER_DEFENCE_POTION1, ItemID.DIVINE_SUPER_DEFENCE_POTION2, ItemID.DIVINE_SUPER_DEFENCE_POTION3, ItemID.DIVINE_SUPER_DEFENCE_POTION4))
		.build()),

	DIVINE_SUPER_STRENGTH(Potion.builder()
		.level(70)
		.name("Divine super strength potion")
		.complexBase(Set.of(SUPER_STRENGTH.potion))
		.primary(ItemID.CRYSTAL_DUST)
		.primaryAlt(ItemID.CRYSTAL_SHARD)
		.ids(Set.of(ItemID.DIVINE_SUPER_STRENGTH_POTION1, ItemID.DIVINE_SUPER_STRENGTH_POTION2, ItemID.DIVINE_SUPER_STRENGTH_POTION3, ItemID.DIVINE_SUPER_STRENGTH_POTION4))
		.build()),

	DIVINE_RANGING(Potion.builder()
		.level(74)
		.name("Divine ranging potion")
		.complexBase(Set.of(RANGING_POTION.potion))
		.primary(ItemID.CRYSTAL_DUST)
		.primaryAlt(ItemID.CRYSTAL_SHARD)
		.ids(Set.of(ItemID.DIVINE_RANGING_POTION1, ItemID.DIVINE_RANGING_POTION2, ItemID.DIVINE_RANGING_POTION3, ItemID.DIVINE_RANGING_POTION4))
		.build()),

	STAMINA_POTION(Potion.builder()
		.level(77)
		.name("Stamina potion")
		.complexBase(Set.of(SUPER_ENERGY.potion))
		.primary(ItemID.AMYLASE_CRYSTAL)
		.primaryAlt(ItemID.AMYLASE_PACK)
		.ids(Set.of(ItemID.STAMINA_POTION1, ItemID.STAMINA_POTION2, ItemID.STAMINA_POTION3, ItemID.STAMINA_POTION4))
		.build()),

	DIVINE_MAGIC(Potion.builder()
		.level(78)
		.name("Divine magic potion")
		.complexBase(Set.of(MAGIC_POTION.potion))
		.primary(ItemID.CRYSTAL_DUST)
		.primaryAlt(ItemID.CRYSTAL_SHARD)
		.ids(Set.of(ItemID.DIVINE_MAGIC_POTION1, ItemID.DIVINE_MAGIC_POTION2, ItemID.DIVINE_MAGIC_POTION3, ItemID.DIVINE_MAGIC_POTION4))
		.build()),

	EXTENDED_ANTIFIRE(Potion.builder()
		.level(84)
		.name("Extended antifire")
		.complexBase(Set.of(ANTIFIRE_POTION.potion))
		.primary(ItemID.LAVA_SCALE_SHARD)
		.primaryAlt(ItemID.LAVA_SCALE)
		.ids(Set.of(ItemID.EXTENDED_ANTIFIRE1, ItemID.EXTENDED_ANTIFIRE2, ItemID.EXTENDED_ANTIFIRE3, ItemID.EXTENDED_ANTIFIRE4))
		.build()),

	DIVINE_BASTION(Potion.builder()
		.level(86)
		.name("Divine bastion potion")
		.complexBase(Set.of(BASTION_POTION.potion))
		.primary(ItemID.CRYSTAL_DUST)
		.primaryAlt(ItemID.CRYSTAL_SHARD)
		.ids(Set.of(ItemID.DIVINE_BASTION_POTION1, ItemID.DIVINE_BASTION_POTION2, ItemID.DIVINE_BASTION_POTION3, ItemID.DIVINE_BASTION_POTION4))
		.build()),

	DIVINE_BATTLEMAGE(Potion.builder()
		.level(86)
		.name("Divine battlemage potion")
		.complexBase(Set.of(BATTLEMAGE_POTION.potion))
		.primary(ItemID.CRYSTAL_DUST)
		.primaryAlt(ItemID.CRYSTAL_SHARD)
		.ids(Set.of(ItemID.DIVINE_BATTLEMAGE_POTION1, ItemID.DIVINE_BATTLEMAGE_POTION2, ItemID.DIVINE_BATTLEMAGE_POTION3, ItemID.DIVINE_BATTLEMAGE_POTION4))
		.build()),

	ANTIVENOM(Potion.builder()
		.level(87)
		.name("Anti-venom")
		.complexBase(Set.of(ANTIDOTE_PLUS2.potion))
		.primary(ItemID.ZULRAHS_SCALES)
		.ids(Set.of(ItemID.ANTIVENOM1, ItemID.ANTIVENOM2, ItemID.ANTIVENOM3, ItemID.ANTIVENOM4))
		.build()),

	SUPER_COMBAT_POTION(Potion.builder()
		.level(90)
		.name("Super combat potion")
		.complexBase(Set.of(SUPER_ATTACK.potion, SUPER_STRENGTH.potion, SUPER_DEFENCE.potion))
		.primary(ItemID.TORSTOL)
		.primaryAlt(ItemID.GRIMY_TORSTOL)
		.ids(Set.of(ItemID.SUPER_COMBAT_POTION1, ItemID.SUPER_COMBAT_POTION2, ItemID.SUPER_COMBAT_POTION3, ItemID.SUPER_COMBAT_POTION4))
		.build()),

	FORGOTTEN_BREW(Potion.builder()
		.level(91)
		.name("Forgotten brew")
		.complexBase(Set.of(ANCIENT_BREW.potion))
		.primary(ItemID.ANCIENT_ESSENCE)
		.ids(Set.of(ItemID.FORGOTTEN_BREW1, ItemID.FORGOTTEN_BREW2, ItemID.FORGOTTEN_BREW3, ItemID.FORGOTTEN_BREW4))
		.build()),

	SUPER_ANTIFIRE_POTION(Potion.builder()
		.level(92)
		.name("Super antifire potion")
		.complexBase(Set.of(ANTIFIRE_POTION.potion))
		.primary(ItemID.CRUSHED_SUPERIOR_DRAGON_BONES)
		.ids(Set.of(ItemID.SUPER_ANTIFIRE_POTION1, ItemID.SUPER_ANTIFIRE_POTION2, ItemID.SUPER_ANTIFIRE_POTION3, ItemID.SUPER_ANTIFIRE_POTION4))
		.build()),

	ANTIVENOM_PLUS(Potion.builder()
		.level(94)
		.name("Anti-venom+")
		.complexBase(Set.of(ANTIVENOM.potion))
		.primary(ItemID.TORSTOL)
		.grimyHerb(ItemID.GRIMY_TORSTOL)
		.primarySeed(ItemID.TORSTOL_SEED)
		.ids(Set.of(ItemID.ANTIVENOM1_12919, ItemID.ANTIVENOM2_12917, ItemID.ANTIVENOM3_12915, ItemID.ANTIVENOM4_12913))
		.build()),

	DIVINE_SUPER_COMBAT(Potion.builder()
		.level(97)
		.name("Divine super combat potion")
		.complexBase(Set.of(SUPER_COMBAT_POTION.potion))
		.primary(ItemID.CRYSTAL_DUST)
		.primaryAlt(ItemID.CRYSTAL_SHARD)
		.ids(Set.of(ItemID.DIVINE_SUPER_COMBAT_POTION1, ItemID.DIVINE_SUPER_COMBAT_POTION2, ItemID.DIVINE_SUPER_COMBAT_POTION3, ItemID.DIVINE_SUPER_COMBAT_POTION4))
		.build()),

	EXTENDED_SUPER_ANTIFIRE(Potion.builder()
		.level(98)
		.name("Extended super antifire")
		.complexBase(Set.of(ANTIFIRE_POTION.potion))
		.primary(ItemID.CRUSHED_SUPERIOR_DRAGON_BONES)
		.ids(Set.of(ItemID.EXTENDED_SUPER_ANTIFIRE1, ItemID.EXTENDED_SUPER_ANTIFIRE2, ItemID.EXTENDED_SUPER_ANTIFIRE3, ItemID.EXTENDED_SUPER_ANTIFIRE4))
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

	private static void buildPrimariesAndGrimyMaps()
	{
		Arrays.stream(values()).map(p -> p.potion).forEach(p -> {
			// handle Imp Repellent
			if (IMP_REPELLENT.potion.equals(p))
			{
				// use this potion's secondaries as primary ingredients
				p.getSecondaries().forEach(secondary -> primaryToPotion.put(secondary, List.of(p)));
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

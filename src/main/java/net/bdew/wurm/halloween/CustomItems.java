package net.bdew.wurm.halloween;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.bodys.BodyTemplate;
import com.wurmonline.server.combat.ArmourTemplate;
import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.IconConstants;
import com.wurmonline.shared.constants.ItemMaterials;
import com.wurmonline.shared.util.MaterialUtilities;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.gotti.wurmunlimited.modsupport.items.ModItems;
import org.gotti.wurmunlimited.modsupport.items.ModelNameProvider;

import java.io.IOException;

public class CustomItems {
    public static int gravestoneId, hatId, maskId, pumpkinHelmId, candleId, candleInfId;
    public static int cauldronEmptyId, cauldronStewId, cauldronCreepyId, witchWandId, coffinId, wingsId;
    public static int pumpkinLamp, pumpkinHanging, twistedLamp;
    public static int skullShoulders = 1063, humanShoulders = 1064;

    static void registerGravestone() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("bdew.halloween.gravestone")
                .name("ominous gravestone", "ominous gravestone", "An ominous gravestone. Investigating it might yield a treat... or a trick.")
                .modelName("model.decoration.gravestone.buried.")
                .imageNumber((short) IconConstants.ICON_DECO_GRAVESTONE)
                .weightGrams(100000)
                .dimensions(20, 20, 400)
                .decayTime(Long.MAX_VALUE)
                .material(ItemMaterials.MATERIAL_STONE)
                .behaviourType((short) 1)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NOMOVE,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                })
                .build();

        gravestoneId = temp.getTemplateId();
    }

    static void registerHat() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("bdew.halloween.hat")
                .name("witch hat", "witch hats", "A conical hat with a wide brim, enchanted with magic to provide extra protection.")
                .imageNumber((short) IconConstants.ICON_ARMOR_SUMMERHAT)
                .modelName("model.armour.head.hat.witch.")
                .weightGrams(500)
                .dimensions(15, 20, 20)
                .decayTime(3024000L)
                .value(10000)
                .material(ItemMaterials.MATERIAL_COTTON)
                .behaviourType((short) 1)
                .bodySpaces(new byte[]{BodyTemplate.head, BodyTemplate.secondHead})
                .primarySkill(SkillList.CLOTHTAILORING)
                .difficulty(60)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_ARMOUR,
                        ItemTypes.ITEM_TYPE_CLOTH,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_IMPROVEITEM,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION
                })
                .build();

        hatId = temp.getTemplateId();

        new ArmourTemplate(hatId, ArmourTemplate.ARMOUR_TYPE_PLATE, 0.005f);
    }

    static void registerMask() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("bdew.halloween.mask")
                .name("skull mask", "skull masks", "A spooky mask made from a skull.")
                .imageNumber((short) IconConstants.ICON_MASKS)
                .modelName("model.armour.head.mask.skull.")
                .weightGrams(500)
                .dimensions(1, 10, 20)
                .decayTime(3024000L)
                .value(10000)
                .behaviourType((short) 1)
                .bodySpaces(new byte[]{BodyTemplate.face})
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_ARMOUR,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_NO_IMPROVE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION
                })
                .build();

        maskId = temp.getTemplateId();
    }

    static void registerPumpkinHelm() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("bdew.halloween.helm")
                .name("pumpkin helmet", "pumpkin helmet", "A spooky pumpkin attached to a metal helmet.")
                .imageNumber((short) IconConstants.ICON_FOOD_PUMPKIN)
                .modelName("model.armour.head.pumpkin.")
                .weightGrams(1000)
                .dimensions(5, 20, 20)
                .decayTime(3024000L)
                .value(10000)
                .material(ItemMaterials.MATERIAL_IRON)
                .behaviourType((short) 1)
                .bodySpaces(new byte[]{BodyTemplate.head, BodyTemplate.secondHead})
                .primarySkill(SkillList.SMITHING_ARMOUR_PLATE)
                .difficulty(50)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_ARMOUR,
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_IMPROVEITEM,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION
                })
                .build();

        pumpkinHelmId = temp.getTemplateId();

        new ArmourTemplate(pumpkinHelmId, ArmourTemplate.ARMOUR_TYPE_PLATE, 0.01F);

        if (ModConfig.craftablePumpkinHelm)
            CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_ARMOUR_PLATE, ItemList.pumpkinHalloween, ItemList.helmetGreat, pumpkinHelmId, true, true, 0f, false, false, CreationCategories.ARMOUR);
    }

    static void registerCandles() throws IOException {
        ItemTemplate candle = new ItemTemplateBuilder("bdew.halloween.candle")
                .name("skull candle", "skull candle", "A candle made from tallow repeatedly dipped around a cloth wicker, it's placed on a skull.")
                .modelName("model.light.candle.skull.")
                .imageNumber((short) IconConstants.ICON_TOOL_CANDLE_LIT)
                .weightGrams(50)
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .behaviourType((short) 1)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_LIGHT,
                        ItemTypes.ITEM_TYPE_PLANTABLE,
                        ItemTypes.ITEM_TYPE_LIGHT_BRIGHT,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_ALWAYSPOLL
                })
                .build();

        candleId = candle.getTemplateId();

        ItemTemplate candleInf = new ItemTemplateBuilder("bdew.halloween.candle.inf")
                .name("magic skull candle", "magic skull candles", "A magic candle placed on a skull, it will burn for eternity.")
                .modelName("model.light.candle.skull.")
                .imageNumber((short) IconConstants.ICON_TOOL_CANDLE_LIT)
                .weightGrams(50)
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .behaviourType((short) 1)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_LIGHT,
                        ItemTypes.ITEM_TYPE_ALWAYS_LIT,
                        ItemTypes.ITEM_TYPE_PLANTABLE,
                        ItemTypes.ITEM_TYPE_LIGHT_BRIGHT,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION
                })
                .build();

        candleInfId = candleInf.getTemplateId();

        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.candle, ItemList.skullGoblin, candleId, true, true, 0f, false, false, CreationCategories.LIGHTS_AND_LAMPS);

        if (ModConfig.craftablePumpkinHelm)
            CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, candleId, ItemList.sourceCrystal, candleInfId, true, true, 0f, false, false, CreationCategories.LIGHTS_AND_LAMPS);
    }

    static void registerCauldron() throws IOException {
        ItemTemplate cauldronEmpty = new ItemTemplateBuilder("bdew.halloween.cauldron.empty")
                .name("witch's cauldron", "witch's cauldrons", "A creepy cauldron adorned with skulls.")
                .modelName("model.tool.cauldron.witch.")
                .imageNumber((short) IconConstants.ICON_TOOL_CAULDRON)
                .weightGrams(5000)
                .dimensions(40, 40, 40)
                .decayTime(TimeConstants.DECAYTIME_STEEL)
                .behaviourType((short) 1)
                .difficulty(30)
                .material(Materials.MATERIAL_IRON)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_PLANTABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE
                })
                .build();

        cauldronEmptyId = cauldronEmpty.getTemplateId();

        ItemTemplate cauldronStew = new ItemTemplateBuilder("bdew.halloween.cauldron.stew")
                .name("witch's stew cauldron", "witch's stew cauldrons", "A creepy cauldron adorned with skulls. It's full of pumpkin stew.")
                .modelName("model.tool.cauldron.witch.stew.")
                .imageNumber((short) IconConstants.ICON_TOOL_CAULDRON)
                .weightGrams(5000)
                .dimensions(40, 40, 40)
                .decayTime(TimeConstants.DECAYTIME_STEEL)
                .behaviourType((short) 1)
                .difficulty(30)
                .material(Materials.MATERIAL_IRON)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_PLANTABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE
                })
                .build();

        cauldronStewId = cauldronStew.getTemplateId();

        ItemTemplate cauldronCreepy = new ItemTemplateBuilder("bdew.halloween.cauldron.creepy")
                .name("disgusting stew cauldron", "disgusting stew cauldrons", "A creepy cauldron adorned with skulls. It contains something disgusting and smelly.")
                .modelName("model.tool.cauldron.witch.creepy.")
                .imageNumber((short) IconConstants.ICON_TOOL_CAULDRON)
                .weightGrams(5000)
                .dimensions(40, 40, 40)
                .decayTime(TimeConstants.DECAYTIME_STEEL)
                .behaviourType((short) 1)
                .difficulty(30)
                .material(Materials.MATERIAL_IRON)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_PLANTABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE
                })
                .build();

        cauldronCreepyId = cauldronCreepy.getTemplateId();

        CreationEntryCreator.createAdvancedEntry(SkillList.SMITHING_BLACKSMITHING, ItemList.cauldron, ItemList.skullGoblin, cauldronEmptyId, false, false, 0f, true, false, CreationCategories.DECORATION)
                .addRequirement(new CreationRequirement(1, ItemList.skullGoblin, 1, true))
                .addRequirement(new CreationRequirement(2, ItemList.nailsIronSmall, 2, true));

        CreationEntryCreator.createAdvancedEntry(SkillList.SMITHING_BLACKSMITHING, cauldronEmptyId, ItemList.water, cauldronStewId, false, false, 0f, true, false, CreationCategories.DECORATION)
                .addRequirement(new CreationRequirement(1, ItemList.pumpkin, 3, true))
                .addRequirement(new CreationRequirement(2, ItemList.sugar, 2, true));

        CreationEntryCreator.createAdvancedEntry(SkillList.SMITHING_BLACKSMITHING, cauldronEmptyId, ItemList.water, cauldronCreepyId, false, false, 0f, true, false, CreationCategories.DECORATION)
                .addRequirement(new CreationRequirement(1, ItemList.skullGoblin, 1, true))
                .addRequirement(new CreationRequirement(2, ItemList.eye, 2, true))
                .addRequirement(new CreationRequirement(3, ItemList.heart, 3, true))
                .addRequirement(new CreationRequirement(4, ItemList.meat, 2, true));
    }

    static void registerWand() throws IOException {
        ItemTemplate witchWand = new ItemTemplateBuilder("bdew.halloween.wand")
                .name("witch's wand", "witch's wands", "A magic wand enchanted with dark magic.")
                .modelName("model.wand.witch.")
                .imageNumber((short) IconConstants.ICON_WAND_SILVER)
                .weightGrams(100)
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .behaviourType((short) 1)
                .material(Materials.MATERIAL_WOOD_BIRCH)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_PLANTABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE
                })
                .build();

        witchWandId = witchWand.getTemplateId();
    }

    static void registerCoffin() throws IOException {
        ItemTemplate coffin = new ItemTemplateBuilder("bdew.halloween.coffin")
                .name("coffin", "coffins", "A coffin made from wood, adorned with skulls and silver.")
                .modelName("model.furniture.coffin.wood.")
                .imageNumber((short) IconConstants.ICON_CONTAINER_CHEST_LARGE)
                .weightGrams(20000)
                .dimensions(50, 51, 201)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .behaviourType((short) 1)
                .material(Materials.MATERIAL_WOOD_BIRCH)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_PLANTABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE
                })
                .build();

        coffinId = coffin.getTemplateId();

        CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.plank, ItemList.nailsIronLarge, coffinId, false, false, 0f, true, false, CreationCategories.DECORATION)
                .addRequirement(new CreationRequirement(1, ItemList.nailsIronLarge, 6, true))
                .addRequirement(new CreationRequirement(2, ItemList.skullGoblin, 4, true))
                .addRequirement(new CreationRequirement(3, ItemList.plank, 20, true))
                .addRequirement(new CreationRequirement(4, ItemList.silverBar, 5, true));
    }

    static void registerWings() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("bdew.halloween.wings")
                .name("bat wings", "bat wings", "A set of decorative wings made to resemble a bat.")
                .imageNumber((short) IconConstants.ICON_LEATHER_SKIN)
                .modelName("model.armour.wings.bat.")
                .weightGrams(1000)
                .dimensions(5, 5, 5)
                .decayTime(TimeConstants.DECAYTIME_LEATHER)
                .behaviourType((short) 1)
                .bodySpaces(new byte[]{BodyTemplate.backSlot})
                .material(Materials.MATERIAL_LEATHER)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_LEATHER,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION
                })
                .build();

        wingsId = temp.getTemplateId();

        ModItems.addModelNameProvider(wingsId, new ModelNameProvider() {
            @Override
            public String getModelName(Item item) {
                if (item.getOwnerId() == -10L)
                    return item.getTemplate().getModelName() + "ground.";
                else
                    return item.getTemplate().getModelName();
            }
        });
    }

    static void regiesterLamps() throws IOException {
        ItemTemplate pumpkinTpl = new ItemTemplateBuilder("bdew.halloween.lamp.pumpkin")
                .name("pumpkin lamp", "pumpkin lamps", "A decorative pumpkin lamp on a wooden post.")
                .imageNumber((short) IconConstants.ICON_DECO_METAL_LAMP)
                .modelName("model.light.lamp.street.pumpkin.")
                .weightGrams(5000)
                .dimensions(2, 20, 200)
                .decayTime(4838400L)
                .behaviourType((short) 1)
                .material(Materials.MATERIAL_IRON)
                .difficulty(25)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_STREETLAMP,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_OILCONSUMING,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_DESTROYABLE,
                        ItemTypes.ITEM_TYPE_LIGHT_BRIGHT,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_PLANTABLE
                })
                .build();
        pumpkinTpl.setDyeAmountGrams(100);
        pumpkinLamp = pumpkinTpl.getTemplateId();

        ItemTemplate hangingTpl = new ItemTemplateBuilder("bdew.halloween.lamp.hanging")
                .name("hanging pumpkin lamp", "hanging pumpkin lamps", "A decorative pumpkin lamp hanging from a wooden pole.")
                .imageNumber((short) IconConstants.ICON_DECO_METAL_LAMP)
                .modelName("model.light.lamp.street.pole.pumpkin.")
                .weightGrams(5000)
                .dimensions(2, 20, 200)
                .decayTime(4838400L)
                .behaviourType((short) 1)
                .material(Materials.MATERIAL_IRON)
                .difficulty(25)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_STREETLAMP,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_OILCONSUMING,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_DESTROYABLE,
                        ItemTypes.ITEM_TYPE_LIGHT_BRIGHT,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_PLANTABLE
                })
                .build();
        hangingTpl.setDyeAmountGrams(100);
        pumpkinHanging = hangingTpl.getTemplateId();

        ItemTemplate twistedTpl = new ItemTemplateBuilder("bdew.halloween.lamp.twisted")
                .name("twisted pumpkin lamp", "twisted pumpkin lamps", "A twisted pumpkin lamp mounted on twisted of rotten wood.")
                .imageNumber((short) IconConstants.ICON_DECO_METAL_LAMP)
                .modelName("model.light.lamp.street.twisted.")
                .weightGrams(5000)
                .dimensions(2, 20, 200)
                .decayTime(4838400L)
                .behaviourType((short) 1)
                .material(Materials.MATERIAL_WOOD_BIRCH)
                .difficulty(25)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_STREETLAMP,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_OILCONSUMING,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_DESTROYABLE,
                        ItemTypes.ITEM_TYPE_LIGHT_BRIGHT,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_PLANTABLE
                })
                .build();
        twistedTpl.setDyeAmountGrams(100);
        twistedLamp = twistedTpl.getTemplateId();

        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_BLACKSMITHING, ItemList.pumpkinHalloween, ItemList.streetLamp, pumpkinLamp, true, true, 0f, false, false, CreationCategories.LIGHTS_AND_LAMPS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_BLACKSMITHING, ItemList.pumpkinHalloween, ItemList.streetLampHanging, pumpkinHanging, true, true, 0f, false, false, CreationCategories.LIGHTS_AND_LAMPS);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.pumpkinHalloween, ItemList.log, twistedLamp, true, true, 0f, false, false, CreationCategories.LIGHTS_AND_LAMPS);

        ModelNameProvider litModelProvider = item -> {
            StringBuilder sb = new StringBuilder(item.getTemplate().getModelName());
            if (item.isOnFire()) sb.append("lit.");
            sb.append(MaterialUtilities.getMaterialString(item.getMaterial()));
            return sb.toString();
        };

        ModItems.addModelNameProvider(pumpkinLamp, litModelProvider);
        ModItems.addModelNameProvider(pumpkinHanging, litModelProvider);
        ModItems.addModelNameProvider(twistedLamp, litModelProvider);
    }
}

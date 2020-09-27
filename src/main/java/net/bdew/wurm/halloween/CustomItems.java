package net.bdew.wurm.halloween;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.bodys.BodyTemplate;
import com.wurmonline.server.combat.ArmourTemplate;
import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.IconConstants;
import com.wurmonline.shared.constants.ItemMaterials;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;

import java.io.IOException;

public class CustomItems {
    public static int gravestoneId, hatId, maskId, pumpkinHelmId, candleId, candleInfId;

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
}

package net.bdew.wurm.halloween;

import com.wurmonline.server.bodys.BodyTemplate;
import com.wurmonline.server.combat.ArmourTemplate;
import com.wurmonline.server.creatures.CreatureTemplate;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.CreatureTypes;
import com.wurmonline.shared.constants.ItemMaterials;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.CreatureTemplateBuilder;

public class CustomCreatures {
    public static int treeId, pumpkinId;

    static void createEvilTreeTemplate() throws NoSuchFieldException, IllegalAccessException {
        final CreatureTemplate temp = new CreatureTemplateBuilder("bdew.halloween.eviltree")
                .name("Evil Tree")
                .description("A monstrous tree animated by dark magic.")
                .modelName("model.creature.eviltree")
                .types(new int[]{
                        CreatureTypes.C_TYPE_MOVE_LOCAL,
                        CreatureTypes.C_TYPE_AGG_HUMAN,
                        CreatureTypes.C_TYPE_MONSTER,
                        CreatureTypes.C_TYPE_HUNTING,
                        CreatureTypes.C_TYPE_CARNIVORE,
                        CreatureTypes.C_TYPE_NON_NEWBIE,
                        CreatureTypes.C_TYPE_NO_REBIRTH
                })
                .bodyType(BodyTemplate.TYPE_HUMAN)
                .defaultSkills()
                .skill(SkillList.BODY_STRENGTH, 25f)
                .skill(SkillList.BODY_CONTROL, 35f)
                .skill(SkillList.BODY_STAMINA, 30f)
                .skill(SkillList.MIND_LOGICAL, 12f)
                .skill(SkillList.MIND_SPEED, 14f)
                .skill(SkillList.SOUL_STRENGTH, 20f)
                .skill(SkillList.SOUL_DEPTH, 5f)
                .skill(SkillList.WEAPONLESS_FIGHTING, 50f)
                .vision((short) 5)
                .dimension((short) 50, (short) 50, (short) 300)
                .deathSounds("sound.combat.death.zombie", "sound.combat.death.zombie")
                .hitSounds("sound.combat.hit.zombie", "sound.combat.hit.zombie")
                .naturalArmour(ModConfig.evilTreeArmor)
                .damages(ModConfig.evilTreeDamage, ModConfig.evilTreeDamage, ModConfig.evilTreeDamage, 0f, 0f)
                .speed(2f)
                .moveRate(1500)
                .itemsButchered(new int[]{})
                .maxHuntDist(10)
                .aggressive(34)
                .meatMaterial(ItemMaterials.MATERIAL_MEAT_TOUGH)
                .handDamString("whip")
                .maxAge(200)
                .armourType(ArmourTemplate.ARMOUR_TYPE_STUDDED)
                .baseCombatRating(ModConfig.evilTreeCR)
                .combatDamageType((byte) 4)
                .alignment(-50f)
                .maxPercentOfCreatures(0.03f)
                .build();

        temp.setCreatureAI(new GuardianCreatureAI());

        ReflectionUtil.setPrivateField(temp, ReflectionUtil.getField(CreatureTemplate.class, "corpsename"), "horse.butchered.");

        treeId = temp.getTemplateId();
    }

    static void createPumpkinMonsterTemplate() throws NoSuchFieldException, IllegalAccessException {
        final CreatureTemplate temp = new CreatureTemplateBuilder("bdew.halloween.pumpkinmonster")
                .name("Pumpkin Monster")
                .description("A monstrous pumpkin animated by dark magic.")
                .modelName("model.creature.pumpkinmonster")
                .types(new int[]{
                        CreatureTypes.C_TYPE_MOVE_LOCAL,
                        CreatureTypes.C_TYPE_AGG_HUMAN,
                        CreatureTypes.C_TYPE_MONSTER,
                        CreatureTypes.C_TYPE_HUNTING,
                        CreatureTypes.C_TYPE_CARNIVORE,
                        CreatureTypes.C_TYPE_NON_NEWBIE,
                        CreatureTypes.C_TYPE_NO_REBIRTH
                })
                .bodyType(BodyTemplate.TYPE_HUMAN)
                .defaultSkills()
                .skill(SkillList.BODY_STRENGTH, 25f)
                .skill(SkillList.BODY_CONTROL, 35f)
                .skill(SkillList.BODY_STAMINA, 30f)
                .skill(SkillList.MIND_LOGICAL, 12f)
                .skill(SkillList.MIND_SPEED, 14f)
                .skill(SkillList.SOUL_STRENGTH, 20f)
                .skill(SkillList.SOUL_DEPTH, 5f)
                .skill(SkillList.WEAPONLESS_FIGHTING, 50f)
                .vision((short) 5)
                .dimension((short) 50, (short) 50, (short) 50)
                .deathSounds("sound.combat.death.zombie", "sound.combat.death.zombie")
                .hitSounds("sound.combat.hit.zombie", "sound.combat.hit.zombie")
                .naturalArmour(ModConfig.pumpkinMonsterArmor)
                .damages(ModConfig.pumpkinMonsterDamage, ModConfig.pumpkinMonsterDamage, ModConfig.pumpkinMonsterDamage, 0f, 0f)
                .speed(2f)
                .moveRate(1500)
                .itemsButchered(new int[]{})
                .maxHuntDist(10)
                .aggressive(34)
                .meatMaterial(ItemMaterials.MATERIAL_MEAT_TOUGH)
                .handDamString("whip")
                .maxAge(200)
                .armourType(ArmourTemplate.ARMOUR_TYPE_STUDDED)
                .baseCombatRating(ModConfig.pumpkinMonsterCR)
                .combatDamageType((byte) 4)
                .alignment(-50f)
                .maxPercentOfCreatures(0.03f)
                .build();

        temp.setCreatureAI(new GuardianCreatureAI());

        ReflectionUtil.setPrivateField(temp, ReflectionUtil.getField(CreatureTemplate.class, "corpsename"), "horse.butchered.");

        pumpkinId = temp.getTemplateId();
    }

}

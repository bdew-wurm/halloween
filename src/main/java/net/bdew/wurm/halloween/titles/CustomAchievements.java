package net.bdew.wurm.halloween.titles;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.Achievement;
import com.wurmonline.server.players.AchievementTemplate;
import com.wurmonline.server.players.Achievements;
import com.wurmonline.server.players.Titles;
import net.bdew.wurm.halloween.ModConfig;
import net.bdew.wurm.tools.server.ModAchievements;

public class CustomAchievements {
    public static AchievementTemplate pumpkinKiller, treeKiller, gravestoneLooter, spiderKiller, broomRider,
            journalKillPumpkins, journalKillTrees, journalKillSpiders, journalLootGravestones,
            journalBroomRider, journalEquipItems, journalUseWand, journalTeleport, journalLightning, journalTransform;

    public static void register() {
        pumpkinKiller = ModAchievements.build(ModConfig.pumpkinKillerAchId)
                .name("Smashing pumpkins")
                .description("You smashed a monstrous pumpkin")
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .achievementsTriggered(ModConfig.journalKillPumpkinsAchId)
                .buildAndRegister();

        treeKiller = ModAchievements.build(ModConfig.treeKillerAchId)
                .name("What a releaf")
                .description("You chopped down an evil tree")
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .achievementsTriggered(ModConfig.journalKillTreesAchId)
                .buildAndRegister();

        spiderKiller = ModAchievements.build(ModConfig.spiderKillerAchId)
                .name("8 legs too many")
                .description("You rid the world of a Jack O Spider")
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .achievementsTriggered(ModConfig.journalKillSpidersAchId)
                .buildAndRegister();

        gravestoneLooter = ModAchievements.build(ModConfig.gravestoneLooterAchId)
                .name("Trick or treat")
                .description("You investigated an ominous gravestone")
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .achievementsTriggered(ModConfig.journalLootGravestonesAchId)
                .buildAndRegister();

        journalKillPumpkins = ModAchievements.build(ModConfig.journalKillPumpkinsAchId)
                .name("Kill 100 pumpkin monsters")
                .requirement("You can find them guarding ominous gravestones in the wilderness.")
                .description("Smashed them all.")
                .invisible(true)
                .oneTimer(true)
                .triggerOn(100)
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();

        journalKillSpiders = ModAchievements.build(ModConfig.journalKillSpidersAchId)
                .name("Kill 100 Jack O Spiders")
                .requirement("You can find them guarding ominous gravestones in the wilderness.")
                .description("That's 800 legs...")
                .invisible(true)
                .oneTimer(true)
                .triggerOn(100)
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();

        journalKillTrees = ModAchievements.build(ModConfig.journalKillTreesAchId)
                .name("Kill 50 evil trees")
                .requirement("You can find them guarding ominous gravestones in the wilderness.")
                .description("Chop Chop Chop!")
                .invisible(true)
                .oneTimer(true)
                .triggerOn(50)
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();

        journalLootGravestones = ModAchievements.build(ModConfig.journalLootGravestonesAchId)
                .name("Investigate 20 ominous gravestones")
                .requirement("You can find them in the wilderness, beware of evil creatures around them.")
                .description("Evil be gone!")
                .invisible(true)
                .oneTimer(true)
                .triggerOn(20)
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();

        broomRider = ModAchievements.build(ModConfig.broomRideHiddenAchId)
                .name("Ride a magic broom")
                .invisible(true)
                .achievementsTriggered(ModConfig.journalBroomRideAchId)
                .buildAndRegister();

        journalBroomRider = ModAchievements.build(ModConfig.journalBroomRideAchId)
                .name("Ride a magic broom for 10km")
                .requirement("Make the broom go vroom.")
                .description("Wheee!")
                .invisible(true)
                .oneTimer(true)
                .triggerOn(2500)
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();

        journalUseWand = ModAchievements.build(ModConfig.journalUseWandAchId)
                .name("Use a witch's wand on another player")
                .requirement("It's mostly harmless...")
                .description("Did i do that?")
                .invisible(true)
                .oneTimer(true)
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();

        journalEquipItems = ModAchievements.build(ModConfig.journalEquipItemsAchId)
                .name("Wear a proper attire for the occasion")
                .requirement("Equip a skull or troll mask, a witch hat, and skull or pumpkin shoulders.")
                .description("You're now dressed to kill.")
                .invisible(true)
                .oneTimer(true)
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();

        journalTeleport = ModAchievements.build(ModConfig.journalTeleportAchId)
                .name("Get blown away by an ominous gravestone")
                .requirement("Investigate ominous gravestones until you find one that just blows you away.")
                .description("I've a feeling we're not in Kansas anymore.")
                .invisible(true)
                .oneTimer(true)
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();

        journalLightning = ModAchievements.build(ModConfig.journalLightningAchId)
                .name("Get hit by lightning")
                .requirement("Some say dark magic attracts lightning better than rods.")
                .description("That was a shocking experience.")
                .invisible(true)
                .oneTimer(true)
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();

        journalTransform = ModAchievements.build(ModConfig.journalTransformAchId)
                .name("Experience transformation")
                .requirement("A witch's wand or some other dark magic can cause you to change form.")
                .description("I'm what now?")
                .invisible(true)
                .oneTimer(true)
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();
    }

    private static Titles.Title getAwardedTitle(Achievement ach) {
        int count = ach.getCounter();
        if (ach.getTemplate() == gravestoneLooter) {
            if (count >= 20)
                return Titles.Title.getTitle(ModConfig.graveRobberTitleId);
        } else if (ach.getTemplate() == pumpkinKiller) {
            if (count >= 100)
                return Titles.Title.getTitle(ModConfig.pumpkinSmasherTitleId);
        }
        return null;
    }

    public static void triggerAchievement(Creature player, AchievementTemplate tpl) {
        Achievements.triggerAchievement(player.getWurmId(), tpl.getNumber());
        Achievements achievments = Achievements.getAchievementObject(player.getWurmId());
        Achievement achievment = achievments.getAchievement(tpl.getNumber());
        if (achievment != null) {
            Titles.Title title = getAwardedTitle(achievment);
            if (title != null) {
                player.addTitle(title);
            }
        }
    }
}

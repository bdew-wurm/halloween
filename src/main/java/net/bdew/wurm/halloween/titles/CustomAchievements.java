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
    public static AchievementTemplate pumpkinKiller, treeKiller, gravestoneLooter;

    public static void register() {
        pumpkinKiller = ModAchievements.build(ModConfig.pumpkinKillerAchId)
                .name("Smashing pumpkins")
                .description("You smashed a monstrous pumpkin")
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();

        treeKiller = ModAchievements.build(ModConfig.treeKillerAchId)
                .name("What a releaf")
                .description("You chopped down an evil tree")
                .achievementType(MiscConstants.A_TYPE_SILVER)
                .buildAndRegister();

        gravestoneLooter = ModAchievements.build(ModConfig.gravestoneLooterAchId)
                .name("Trick or treat")
                .description("You investigated an ominous gravestone")
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

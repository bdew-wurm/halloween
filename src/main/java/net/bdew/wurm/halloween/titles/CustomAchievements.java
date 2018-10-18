package net.bdew.wurm.halloween.titles;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.Achievement;
import com.wurmonline.server.players.AchievementTemplate;
import com.wurmonline.server.players.Achievements;
import com.wurmonline.server.players.Titles;
import net.bdew.wurm.halloween.ModConfig;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;

public class CustomAchievements {
    public static AchievementTemplate pumpkinKiller, treeKiller, gravestoneLooter;

    public static void register() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        pumpkinKiller = addTemplate(ModConfig.pumpkinKillerAchId, "Smashing pumpkins", "You smashed a monstrous pumpkin", false, 1, MiscConstants.A_TYPE_SILVER, false, false);
        treeKiller = addTemplate(ModConfig.treeKillerAchId, "What a releaf", "You chopped down and evil tree", false, 1, MiscConstants.A_TYPE_SILVER, false, false);
        gravestoneLooter = addTemplate(ModConfig.gravestoneLooterAchId, "Trick or treat", "You investigated an ominous gravestone", false, 1, MiscConstants.A_TYPE_SILVER, false, false);
    }

    private static Titles.Title getAwardedTitle(Achievement ach) {
        int count = ach.getCounter();
        if (ach.getTemplate() == gravestoneLooter) {
            if (count >= 20)
                return TitlesExtended.GraveRobber;
        } else if (ach.getTemplate() == pumpkinKiller) {
            if (count >= 100)
                return TitlesExtended.PumpkinSmasher;
        }
        return null;
    }

    private static AchievementTemplate addTemplate(int id, String name, String description, boolean isInvisible, int triggerOn, byte achievementType, boolean playUpdateSound, boolean isOneTimer) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        AchievementTemplate ach = new AchievementTemplate(id, name, isInvisible, triggerOn, achievementType, playUpdateSound, isOneTimer);
        ach.setDescription(description);
        ReflectionUtil.callPrivateMethod(null, ReflectionUtil.getMethod(Achievement.class, "addTemplate", new Class<?>[]{AchievementTemplate.class}), ach);
        return ach;
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

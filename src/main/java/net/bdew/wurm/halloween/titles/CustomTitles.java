package net.bdew.wurm.halloween.titles;

import net.bdew.wurm.halloween.ModConfig;
import net.bdew.wurm.tools.server.ModTitles;

public class CustomTitles {
    public static void register() {
        ModTitles.addTitle(ModConfig.pumpkinSmasherTitleId, "Pumpkin Smasher", "Pumpkin Smasher", -1, "NORMAL");
        ModTitles.addTitle(ModConfig.graveRobberTitleId, "Grave Robber", "Grave Robber", -1, "NORMAL");
    }
}

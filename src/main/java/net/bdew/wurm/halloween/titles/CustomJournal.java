package net.bdew.wurm.halloween.titles;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Titles;
import net.bdew.wurm.halloween.CustomItems;
import net.bdew.wurm.halloween.ModConfig;
import net.bdew.wurm.tools.server.journal.ModJournal;
import net.bdew.wurm.tools.server.journal.ModJournalTier;

public class CustomJournal {
    public static void register() {
        ModJournalTier tier = new ModJournalTier((byte) ModConfig.customJournalId, "Trick or Treat", (byte) (-1), (byte) (-1), 3,
                "Hollow Title and a set of bat wings",
                player -> {
                    try {
                        Item wings = ItemFactory.createItem(CustomItems.wingsId, 99f, (byte) 0, player.getName());
                        player.getInventory().insertItem(wings, true);
                        player.addTitle(Titles.Title.getTitle(ModConfig.hollowTitleId));
                    } catch (FailedException | NoSuchTemplateException e) {
                        throw new RuntimeException(e);
                    }
                },
                ModConfig.journalLootGravestonesAchId,
                ModConfig.journalKillPumpkinsAchId,
                ModConfig.journalKillSpidersAchId,
                ModConfig.journalKillTreesAchId,
                ModConfig.journalBroomRideAchId,
                ModConfig.journalEquipItemsAchId,
                ModConfig.journalUseWandAchId,
                ModConfig.journalTransformAchId,
                ModConfig.journalLightningAchId,
                ModConfig.journalTeleportAchId
        );
        ModJournal.addJournalTier(tier);
    }
}

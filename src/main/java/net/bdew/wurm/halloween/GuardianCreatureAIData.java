package net.bdew.wurm.halloween;

import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.ai.CreatureAIData;
import com.wurmonline.server.items.Item;

public class GuardianCreatureAIData extends CreatureAIData {
    public Item guarded;
    public int pathAttempts;

    public static GuardianCreatureAIData get(Creature creature) {
        return (GuardianCreatureAIData) creature.getCreatureAIData();
    }

    public boolean tryFindGuarded() {
        for (Item gravestone : GravestoneTracker.gravestones) {
            if (!gravestone.deleted && gravestone.getPos2f().distance(getCreature().getPos2f()) < 16) {
                guarded = gravestone;
                GravestoneTracker.addGuard(guarded, getCreature());
                Halloween.logInfo(String.format("Associated %s (%d) with %s (%d)", getCreature().getName(), getCreature().getWurmId(), guarded.getName(), guarded.getWurmId()));
                return false;
            }
        }
        Halloween.logInfo(String.format("Failed to find guarded object for %s (%d) - despawning", getCreature().getName(), getCreature().getWurmId()));
        getCreature().destroy();
        return true;
    }
}

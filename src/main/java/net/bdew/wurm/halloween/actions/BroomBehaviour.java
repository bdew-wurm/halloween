package net.bdew.wurm.halloween.actions;

import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import net.bdew.wurm.halloween.Broom;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BroomBehaviour implements BehaviourProvider {
    private List<ActionEntry> embark = Collections.singletonList(new ActionEntry(Actions.EMBARK_DRIVER, "Ride", ""));
    private List<ActionEntry> disembark = Collections.singletonList(new ActionEntry(Actions.DISEMBARK, "Disembark", ""));

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
        if (target.getTemplateId() == Broom.broomId) {
            if (MountBroomPerformer.canUse(performer, target))
                return new ArrayList<>(embark);
            else if (DismountBroomPerformer.canUse(performer, target))
                return new ArrayList<>(disembark);
        }
        return null;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        return getBehavioursFor(performer, target);
    }
}

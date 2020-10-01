package net.bdew.wurm.halloween.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.SpellEffects;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.spells.SpellEffect;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import net.bdew.wurm.halloween.CustomItems;
import net.bdew.wurm.halloween.RandomUtils;
import org.gotti.wurmunlimited.modsupport.actions.*;

import java.util.Collections;
import java.util.List;

public class UseWandAction implements ModAction, ActionPerformer, BehaviourProvider {
    private ActionEntry actionEntry;

    public UseWandAction() {
        actionEntry = new ActionEntryBuilder((short) ModActions.getNextActionId(), "Use wand", "waving wand", new int[]{
                48 /* ACTION_TYPE_ENEMY_ALWAYS */,
                36 /* ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM */
        }).range(20).animationString("spell").build();
        ModActions.registerAction(actionEntry);
    }

    @Override
    public short getActionId() {
        return actionEntry.getNumber();
    }

    public boolean canUse(Creature performer, Item source, Creature target) {
        return performer.isPlayer() && target.isPlayer()
                && source.getTemplateId() == CustomItems.witchWandId
                && target.getVehicle() == -10L;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Creature target) {
        if (canUse(performer, source, target))
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Creature target, short num, float counter) {
        if (!canUse(performer, source, target)) {
            performer.getCommunicator().sendAlertServerMessage("You are not allowed to do that!");
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION, ActionPropagation.NO_SERVER_PROPAGATION);
        }

        if (Zones.interruptedRange(performer, target))
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION, ActionPropagation.NO_SERVER_PROPAGATION);

        if (counter == 1.0f) {
            target.getCommunicator().sendAlertServerMessage(performer.getName() + " points a wand at you and starts chanting!", (byte) 1);
            performer.getCommunicator().sendNormalServerMessage(String.format("You start waving the wand at %s.", target.getName()));
            Server.getInstance().broadCastAction(String.format("%s waves a wand at %s.", performer.getName(), target.getName()), performer, target, 5);

            action.setTimeLeft(50);
            performer.sendActionControl("waving wand", true, action.getTimeLeft());
        } else if (counter * 10.0f > action.getTimeLeft()) {
            SpellEffects effs = target.getSpellEffects();
            if (effs == null) {
                effs = target.createSpellEffects();
            }
            SpellEffect eff = effs.getSpellEffect((byte) 72);
            if (eff == null) {
                target.getCommunicator().sendNormalServerMessage("You change appearance!");
                Server.getInstance().broadCastAction(target.getName() + " drinks a yellow potion.", target, 5);

                if (source.getQualityLevel() > 1) {
                    source.setQualityLevel(source.getQualityLevel() - 1);
                } else {
                    Items.destroyItem(source.getWurmId());
                    performer.getCommunicator().sendNormalServerMessage("The wand crumbles to dust as the last bit of magic is used.");
                }

                eff = new SpellEffect(target.getWurmId(), (byte) 72, 100.0f, (Server.rand.nextInt(100) + 50) * 20, (byte) 9, (byte) 0, true);
                effs.addSpellEffect(eff);

                target.getCommunicator().sendNormalServerMessage("Your feel yourself changing!");
                performer.getCommunicator().sendNormalServerMessage(String.format("%s jumps is surprise as your spell is completed!", target.getName()));
                Server.getInstance().broadCastAction(String.format("%s suddenly looks different.", target.getName()), performer, target, 5);

                target.setModelName(RandomUtils.randomIllusionModel());

                VolaTile tile = target.getCurrentTile();
                if (tile != null) {
                    tile.sendAttachCreatureEffect(target, (byte) 8, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
                }
            } else {
                performer.getCommunicator().sendNormalServerMessage(String.format("You feel that the existing illusion placed on %s interferes with your magic.", target.getName()));
            }
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION, ActionPropagation.NO_SERVER_PROPAGATION);
        }

        return propagate(action, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION, ActionPropagation.NO_SERVER_PROPAGATION);
    }
}

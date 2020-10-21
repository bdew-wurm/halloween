package net.bdew.wurm.halloween.actions;

import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.Server;
import com.wurmonline.server.ServerEntry;
import com.wurmonline.server.Servers;
import com.wurmonline.server.behaviours.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.MountAction;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import net.bdew.wurm.halloween.Broom;
import net.bdew.wurm.halloween.Halloween;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.ActionPropagation;

import java.lang.reflect.Method;

public class MountBroomPerformer implements ActionPerformer {
    private final Method mMethodsItemsTake;

    public MountBroomPerformer() {
        try {
            mMethodsItemsTake = ReflectionUtil.getMethod(MethodsItems.class, "take", new Class[]{Action.class, Creature.class, Item.class});
            mMethodsItemsTake.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public short getActionId() {
        return Actions.EMBARK_DRIVER;
    }

    public static boolean canUse(Creature performer, Item target) {
        return performer.isPlayer()
                && Vehicles.getVehicle(target) != null
                && !Vehicles.getVehicle(target).seats[0].isOccupied()
                && performer.getVehicle() == -10L
                && (target.getOwnerId() == performer.getWurmId() || !MethodsItems.checkIfStealing(target, performer, null));
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        return action(action, performer, target, num, counter);
    }

    @Override
    public boolean action(Action action, Creature performer, Item target, short num, float counter) {
        if (target.getTemplateId() != Broom.broomId)
            return propagate(action, ActionPropagation.SERVER_PROPAGATION, ActionPropagation.ACTION_PERFORMER_PROPAGATION);

        if (!canUse(performer, target)) {
            performer.getCommunicator().sendAlertServerMessage("You are not allowed to do that");
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }

        if (target.getQualityLevel() < 10f) {
            performer.getCommunicator().sendNormalServerMessage("The broom has lost it's magic and can no longer be used.");
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }

        try {
            if (target.getOwnerId() != performer.getWurmId()) {
                TakeResultEnum res = (TakeResultEnum) mMethodsItemsTake.invoke(null, action, performer, target);
                if (res != TakeResultEnum.SUCCESS) {
                    res.sendToPerformer(performer);
                    return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
            }

            Broom.setEmbarking(target, true);

            try {
                target.getParent().dropItem(target.getWurmId(), false);
            } catch (NoSuchItemException e) {
                Halloween.logException("Huh broom has no parent?", e);
            }

            target.setPosXYZRotation(performer.getPosX(), performer.getPosY(), performer.getPositionZ(), performer.getStatus().getRotation());
            target.setOnBridge(performer.getBridgeId());
            performer.getCurrentTile().addItem(target, false, false);

            Vehicle vehicle = Vehicles.getVehicle(target);

            vehicle.seats[0].occupy(vehicle, performer);
            vehicle.pilotId = performer.getWurmId();

            performer.setVehicleCommander(true);

            final MountAction m = new MountAction(null, target, vehicle, 0, true, vehicle.seats[0].offz);
            performer.setMountAction(m);

            performer.setVehicle(target.getWurmId(), true, (byte) 0);

            if (vehicle.hasDestinationSet()) {
                final ServerEntry entry = vehicle.getDestinationServer();
                if (!Servers.mayEnterServer(performer, entry) || ((entry.PVPSERVER || entry.isChaosServer()) && ((Player) performer).isBlockingPvP())) {
                    vehicle.clearDestination();
                    performer.getCommunicator().sendAlertServerMessage("The previous course is unavailable and has been cleared.");
                } else {
                    performer.getCommunicator().sendAlertServerMessage(String.format("The %s is on a course for %s.", target.getName(), entry.getName()));
                }
            }

            performer.getCommunicator().sendNormalServerMessage("You hop on a magic broom.");
            Server.getInstance().broadCastAction(String.format("%s hops on a magic broom.", performer.getName()), performer, 5);

        } catch (Throwable e) {
            Halloween.logException(String.format("Error mounting broom %d by %s", target.getWurmId(), performer.getName()), e);
            performer.getCommunicator().sendAlertServerMessage("Something went wrong, try again or contact staff.");
        }

        return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
    }
}

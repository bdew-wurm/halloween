package net.bdew.wurm.halloween;

import com.wurmonline.server.creatures.Communicator;
import org.gotti.wurmunlimited.modloader.interfaces.MessagePolicy;

public class CommandHandler {
    public static MessagePolicy handleCommand(Communicator communicator, String message) {
        if (message.startsWith("#spawnGravestone") && communicator.player.getPower() >= 2) {
            if (!communicator.player.isOnSurface()) {
                communicator.sendAlertServerMessage("Gravestones can't be spawned underground");
            } else {
                GravestoneTracker.spawnGravestoneAt(communicator.player.currentTile);
                communicator.sendNormalServerMessage("An ominous gravestone drops from the sky!");
            }
            return MessagePolicy.DISCARD;
        }
        return MessagePolicy.PASS;
    }
}

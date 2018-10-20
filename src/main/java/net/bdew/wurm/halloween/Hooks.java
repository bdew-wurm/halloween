package net.bdew.wurm.halloween;

import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.items.Item;

public class Hooks {
    public static void addItemLoading(Item item) {
        if (item.getTemplateId() == CustomItems.gravestoneId) {
            GravestoneTracker.addGravestone(item);
        }
    }

    public static void sendItemHook(Communicator comm, Item item) {
        if (item.getTemplateId() == CustomItems.gravestoneId) {
            comm.sendRemoveEffect(item.getWurmId());
            comm.sendRemoveEffect(item.getWurmId() + 1);
            comm.sendRepaint(item.getWurmId(), (byte) 1, (byte) 1, (byte) 1, (byte) 255, (byte) 0);
            comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 255, (byte) 1, (byte) 1, (byte) 255);
            comm.sendAddEffect(item.getWurmId(), item.getWurmId(), (short) 27, item.getPosX(), item.getPosY(), item.getPosZ(), (byte) 0, "fogGM", Float.MAX_VALUE, 0f);
            comm.sendAddEffect(item.getWurmId() + 1, item.getWurmId(), (short) 27, item.getPosX(), item.getPosY(), item.getPosZ() + 2.5f, (byte) 0, "magicfire", Float.MAX_VALUE, 0f);
        }
    }

    public static void removeItemHook(Communicator comm, Item item) {
        if (item.getTemplateId() == CustomItems.gravestoneId) {
            comm.sendRemoveEffect(item.getWurmId());
            comm.sendRemoveEffect(item.getWurmId() + 1);
        }
    }

    public static float getSizeMod(Item item) {
        if (item.getTemplateId() == CustomItems.gravestoneId)
            return 3f;
        return 1f;
    }
}

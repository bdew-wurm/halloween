package net.bdew.wurm.halloween;

import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.WurmId;
import com.wurmonline.server.behaviours.Vehicle;
import com.wurmonline.server.bodys.BodyHuman;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSpaceException;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.zones.NoSuchZoneException;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.CounterTypes;
import net.bdew.wurm.halloween.titles.CustomAchievements;
import net.bdew.wurm.tools.server.ServerThreadExecutor;

import java.util.Arrays;
import java.util.Optional;

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

    public static void setVehicle(Creature creature, long prevId, long newId) {
        if (prevId != -10L && WurmId.getType(prevId) == CounterTypes.COUNTER_TYPE_ITEMS) {
            Optional<Item> prevItem = Items.getItemOptional(prevId);
            prevItem.filter(x -> x.getTemplateId() == Broom.broomId).ifPresent(item -> {
                ServerThreadExecutor.INSTANCE.submit(() -> {
                    Broom.stopCounting(item.getWurmId(), creature);
                    try {
                        Zones.getZone(item.getTilePos(), item.isOnSurface()).removeItem(item);
                        creature.getInventory().insertItem(item, true, false);
                    } catch (NoSuchZoneException e) {
                        Halloween.logException(String.format("Zone not found when dismounting broom %d by %s", item.getWurmId(), creature.getName()), e);
                    }
                });
            });
        }
        if (newId != -10L && WurmId.getType(newId) == CounterTypes.COUNTER_TYPE_ITEMS) {
            Optional<Item> newItem = Items.getItemOptional(newId);
            newItem.filter(x -> x.getTemplateId() == Broom.broomId).ifPresent(item -> {
                ServerThreadExecutor.INSTANCE.submit(() -> {
                    Broom.setEmbarking(item, false);
                    Broom.startCounting(item.getWurmId(), creature);
                    creature.getCurrentTile().renameItem(item);
                });
            });
        }
    }

    public static boolean checkRelaxedZError(Creature creature, float input, float expected) {
        if (creature.getVehicle() == -10L || WurmId.getType(creature.getVehicle()) != CounterTypes.COUNTER_TYPE_ITEMS)
            return false;

        try {
            Item itm = Items.getItem(creature.getVehicle());
            if (itm.getTemplateId() == Broom.broomId) {
                if (Math.abs(input - expected) < 0.5f) return true;
            }
        } catch (NoSuchItemException e) {
            return false;
        }
        return false;
    }

    public static byte modifySpeed(Vehicle vehicle, byte speed) {
        try {
            Item item = Items.getItem(vehicle.wurmid);
            if (item.getTemplateId() == Broom.broomId) {
                float mod = 1 + (item.getSpellSpeedBonus() / 100f * ModConfig.broomSpeedEnchantBonus);
                speed = (byte) (mod * speed);
                if (speed < 0) speed = Byte.MAX_VALUE;
            }
        } catch (NoSuchItemException ignored) {
        }
        return speed;
    }

    public static void playerMovedTile(Player player) {
        if (player.getVehicle() != -10L)
            Broom.countMoveTile(player.getVehicle(), player);
    }

    private static boolean itemMatches(Item item, int... templateIds) {
        return Arrays.stream(templateIds).anyMatch(tpl -> item.getTemplateId() == tpl);
    }

    private static boolean isItemInSlot(Creature creature, byte slot, int... templateIds) {
        try {
            return creature.getBody().getBodyPart(slot).getItems().stream().anyMatch(item -> itemMatches(item, templateIds));
        } catch (NoSpaceException e) {
            return false;
        }
    }

    public static void sendWearHook(Item item, Creature creature) {
        if (itemMatches(item, CustomItems.maskId, ItemList.maskTrollHalloween, ItemList.shoulderPumpkinHalloween, CustomItems.skullShoulders, CustomItems.humanShoulders, CustomItems.hatId)) {
            ServerThreadExecutor.INSTANCE.execute(() -> {
                if (creature != null && creature.isPlayer()) {
                    if (isItemInSlot(creature, BodyHuman.face, CustomItems.maskId, ItemList.maskTrollHalloween)
                            && isItemInSlot(creature, BodyHuman.rShoulderSlot, ItemList.shoulderPumpkinHalloween, CustomItems.skullShoulders, CustomItems.humanShoulders)
                            && isItemInSlot(creature, BodyHuman.lShoulderSlot, ItemList.shoulderPumpkinHalloween, CustomItems.skullShoulders, CustomItems.humanShoulders)
                            && isItemInSlot(creature, BodyHuman.head, CustomItems.hatId))
                        CustomAchievements.triggerAchievement(creature, CustomAchievements.journalEquipItems);
                }
            });
        }
    }
}

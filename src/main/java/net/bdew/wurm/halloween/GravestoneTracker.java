package net.bdew.wurm.halloween;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.FailedException;
import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.CreatureTypes;

import java.util.*;

public class GravestoneTracker {
    public static final Set<Item> gravestones = new HashSet<>();
    public static final Map<Long, Set<Creature>> guards = new HashMap<>();

    private static long lastCheck = System.currentTimeMillis();

    public static void addGravestone(Item gravestone) {
        gravestones.add(gravestone);
        guards.put(gravestone.getWurmId(), new HashSet<>());
    }

    public static void addGuard(Item gravestone, Creature guard) {
        guards.get(gravestone.getWurmId()).add(guard);
    }

    public static void gravestoneRemoved(Item gravestone) {
        Halloween.logInfo(String.format("Removing gravestone %d", gravestone.getWurmId()));
        gravestones.remove(gravestone);
        Set<Creature> guardsToRemove = guards.remove(gravestone.getWurmId());
        if (guardsToRemove != null) {
            guardsToRemove.stream().filter(i -> !i.isDead()).forEach(Creature::destroy);
        }
    }

    public static void tick() {
        if (gravestones.size() < ModConfig.gravestoneCount)
            spawnGravestone();
        if (System.currentTimeMillis() - lastCheck > 10000) {
            lastCheck = System.currentTimeMillis();
            for (Item gravestone : new LinkedList<>(gravestones)) {
                if (gravestone.deleted) {
                    gravestoneRemoved(gravestone);
                } else {
                    Set<Creature> guardsToCheck = guards.get(gravestone.getWurmId());
                    if (guardsToCheck != null && !guardsToCheck.isEmpty()) {
                        guardsToCheck.removeIf(Creature::isDead);
                    }
                }
            }
        }
    }

    public static void started() {
        if (gravestones.size() > 0)
            Halloween.logInfo(String.format("Loaded %d gravestones", gravestones.size()));
    }

    private static void spawnGravestone() {
        int tileX = Server.rand.nextInt(Zones.worldTileSizeX);
        int tileY = Server.rand.nextInt(Zones.worldTileSizeY);
        int tile = Server.surfaceMesh.getTile(tileX, tileY);

        byte type = Tiles.decodeType(tile);

        if (Tiles.decodeHeight(tile) < 0 || Tiles.getTile(type).isRoad()) return;

        VolaTile vt = Zones.getOrCreateTile(tileX, tileY, true);

        if (vt.getVillage() != null || vt.getStructure() != null || vt.getItems().length > 0)
            return;

        int woodTiles = 0;

        for (int x = tileX - 2; x <= tileX + 2; x++) {
            for (int y = tileY - 2; y <= tileY + 2; y++) {
                if (x < 0 || y < 0 || x >= Zones.worldTileSizeX || y >= Zones.worldTileSizeY) return;
                Tiles.Tile check = Tiles.getTile(Tiles.decodeType(Server.surfaceMesh.getTile(x, y)));
                if (check.isTree() || check.isBush()) woodTiles++;
            }
        }

        if (woodTiles < 5) return;

        try {
            Item gravestone = ItemFactory.createItem(CustomItems.gravestoneId, 99f, (byte) 0, null);
            vt.addItem(gravestone, false, false);
            Halloween.logInfo(String.format("Spawned gravestone at %d,%d", tileX, tileY));
            addGravestone(gravestone);

            for (int i = 0; i < ModConfig.guardianCount; i++) {
                try {
                    Creature spawned = Creature.doNew(Server.rand.nextInt(ModConfig.guardianChamps) == 0 ? CustomCreatures.treeId : CustomCreatures.pumpkinId, Server.rand.nextInt(4) == 0 ? CreatureTypes.C_MOD_CHAMPION : CreatureTypes.C_MOD_NONE, gravestone.getPosX() - 5f + Server.rand.nextFloat() * 10, gravestone.getPosY() - 5f + Server.rand.nextFloat() * 10, Server.rand.nextInt(360), gravestone.isOnSurface() ? 0 : -1, "", (byte) 0);
                    GuardianCreatureAIData.get(spawned).guarded = gravestone;
                    addGuard(gravestone, spawned);
                } catch (Exception e) {
                    Halloween.logException("Error spawning defenders for gravestone", e);
                }
            }
        } catch (FailedException | NoSuchTemplateException e) {
            Halloween.logException("Error spawning gravestone", e);
        }

    }
}

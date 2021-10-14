package net.bdew.wurm.halloween.actions;

import com.wurmonline.mesh.GrassData;
import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.*;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.bodys.DbWound;
import com.wurmonline.server.bodys.TempWound;
import com.wurmonline.server.bodys.Wound;
import com.wurmonline.server.creatures.*;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.spells.SpellEffect;
import com.wurmonline.server.zones.VirtualZone;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.CreatureTypes;
import com.wurmonline.shared.constants.ItemMaterials;
import net.bdew.wurm.halloween.*;
import net.bdew.wurm.halloween.titles.CustomAchievements;
import org.gotti.wurmunlimited.modsupport.actions.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class InvestigateGravestoneAction implements ModAction, ActionPerformer, BehaviourProvider {
    private ActionEntry actionEntry;

    public InvestigateGravestoneAction() {
        actionEntry = new ActionEntryBuilder((short) ModActions.getNextActionId(), "Investigate", "investigating", new int[]{
                1 /* ACTION_TYPE_NEED_FOOD */,
                4 /* ACTION_TYPE_FATIGUE */,
                6 /* ACTION_TYPE_NOMOVE */,
                48 /* ACTION_TYPE_ENEMY_ALWAYS */,
                37 /* ACTION_TYPE_NEVER_USE_ACTIVE_ITEM */
        }).range(4).build();
        ModActions.registerAction(actionEntry);
    }

    @Override
    public short getActionId() {
        return actionEntry.getNumber();
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return this;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
        return getBehavioursFor(performer, null, target);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (performer instanceof Player && target != null && target.getTemplateId() == CustomItems.gravestoneId)
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        return action(action, performer, target, num, counter);
    }


    @Override
    public boolean action(Action action, Creature performer, Item target, short num, float counter) {
        try {
            Communicator comm = performer.getCommunicator();

            Set<Creature> guards = GravestoneTracker.guards.get(target.getWurmId());
            if (guards != null && guards.size() > 0) {
                boolean attacked = false;
                for (Creature guard : guards) {
                    if (guard.isDead()) continue;
                    comm.sendAlertServerMessage(String.format("The %s doesn't like you touching the gravestone and immediately attacks!", guard.getName().toLowerCase()));
                    guard.setOpponent(performer);
                    attacked = true;
                }
                if (attacked)
                    return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION, ActionPropagation.NO_SERVER_PROPAGATION);
            }

            if (counter == 1.0f) {
                comm.sendNormalServerMessage("You start investigating the gravestone.");
                action.setTimeLeft(50);
                performer.sendActionControl("investigating", true, 50);
                performer.playAnimation("forage", false);
                Methods.sendSound(performer, "sound.work.digging1");
            } else {
                if (action.justTickedSecond() && Server.rand.nextBoolean()) {
                    Methods.sendSound(performer, Server.rand.nextBoolean() ? "sound.work.digging1" : "sound.work.digging2");
                }
                if (counter * 10.0f > action.getTimeLeft()) {
                    doLoot(performer, comm, target);
                    Methods.sendSound(performer, "sound.death.dragon");

                    VolaTile vt = Zones.getOrCreateTile(target.getTilePos(), target.isOnSurface());
                    for (VirtualZone vz : vt.getWatchers()) {
                        if (vz.getWatcher().isPlayer() && vz.getWatcher().hasLink())
                            vz.getWatcher().getCommunicator().sendAddEffect(target.getWurmId() + 3, target.getWurmId(), (short) 27, target.getPosX(), target.getPosY(), target.getPosZ(), (byte) 0, "tornado", 20f, 0f);
                    }

                    GravestoneTracker.gravestoneRemoved(target);
                    Items.destroyItem(target.getWurmId());

                    comm.sendNormalServerMessage("The ominous gravestone evaporates into a cloud of black smoke!");

                    CustomAchievements.triggerAchievement(performer, CustomAchievements.gravestoneLooter);

                    fixTerrain(target.getTileX(), target.getTileY());

                    return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION, ActionPropagation.NO_SERVER_PROPAGATION);
                }
            }
            return false;
        } catch (Exception e) {
            performer.getCommunicator().sendNormalServerMessage("Something went wrong, try again or open a support ticket.");
            Halloween.logException("Claim action error", e);
            return true;
        }
    }

    private void doLoot(Creature performer, Communicator comm, Item gravestone) throws NoSuchTemplateException, FailedException, NoSuchCreatureTemplateException {
        int roll = Server.rand.nextInt(100);
        if (roll < 10) {
            comm.sendAlertServerMessage(String.format("Run away little %s!", performer.getSex() == 0 ? "boy" : "girl"), (byte) 1);
            if (WurmCalendar.isNight())
                spawnAttackers(performer, CreatureTemplateIds.WRAITH_CID, 3, false);
            else
                spawnAttackers(performer, CreatureTemplateIds.SKELETON_CID, 5, false);
        } else if (roll < 20) {
            comm.sendAlertServerMessage("Braaaaainsssss!", (byte) 1);
            spawnAttackers(performer, CreatureTemplateIds.ZOMBIE_CID, 5, true);
        } else if (roll < 25) {
            comm.sendAlertServerMessage("BZZZZZZZZT!", (byte) 1);
            Methods.sendSound(performer, "sound.magicTurret.attack");
            Zones.flash(performer.getTileX(), performer.getTileY(), true);
            VolaTile vt = Zones.getOrCreateTile(gravestone.getTilePos(), gravestone.isOnSurface());
            for (VirtualZone vz : vt.getWatchers()) {
                if (vz.getWatcher().isPlayer() && vz.getWatcher().hasLink())
                    vz.getWatcher().getCommunicator().sendAddEffect(gravestone.getWurmId() + 2, performer.getWurmId(), (short) 27, performer.getPosX(), performer.getPosY(), performer.getPositionZ(), (byte) 0, "lightningBolt1", 5f, 0f);
            }
            performer.achievement(ModConfig.journalLightningAchId);
        } else if (roll < 28) {
            comm.sendAlertServerMessage("Is a trap!", (byte) 1);
            Methods.sendSound(performer, "sound.magicTurret.attack");
            VolaTile vt = Zones.getOrCreateTile(gravestone.getTilePos(), gravestone.isOnSurface());
            for (VirtualZone vz : vt.getWatchers()) {
                if (vz.getWatcher().isPlayer() && vz.getWatcher().hasLink())
                    vz.getWatcher().getCommunicator().sendAddEffect(gravestone.getWurmId() + 2, gravestone.getWurmId(), (short) 27, gravestone.getPosX(), gravestone.getPosY(), gravestone.getPosZ(), (byte) 0, "karmaFireball", 10f, 0f);
            }
            addMagicDamage(null, performer, 2, 10000, Wound.TYPE_BURN, false);
        } else if (roll < 32) {
            comm.sendAlertServerMessage("*POOF*", (byte) 1);
            performer.playPersonalSound("sound.emote.chuckle.female");

            int x, y;
            VolaTile vt;

            do {
                x = Server.rand.nextInt(Zones.worldTileSizeX - 100) + 50;
                y = Server.rand.nextInt(Zones.worldTileSizeY - 100) + 50;
                vt = Zones.getOrCreateTile(x, y, true);
            } while (vt.getVillage() != null || vt.getStructure() != null);

            performer.setTeleportPoints(x * 4 + 2, y * 4 + 2, 0, 0);
            if (performer.startTeleporting()) {
                performer.getCommunicator().sendTeleport(false);
            }

            performer.achievement(ModConfig.journalTeleportAchId);
        } else if (roll < 35) {
            Item hat = ItemFactory.createItem(Server.rand.nextBoolean() ? CustomItems.skullHelm1Id : CustomItems.skullHelm2Id, 90f, ItemMaterials.MATERIAL_UNDEFINED, RandomUtils.randomRarity(10, false), null);
            performer.getInventory().insertItem(hat);
            comm.sendNormalServerMessage("You find a spooky skull helmet!");
        } else if (roll < 40) {
            comm.sendAlertServerMessage("OoooOOoooOoooOoooo!", (byte) 1);
            SpellEffects effs = performer.getSpellEffects();
            if (effs == null) {
                effs = performer.createSpellEffects();
            }
            SpellEffect eff = new SpellEffect(performer.getWurmId(), (byte) 72, 100.0f, (int) (2000.0f * Server.rand.nextFloat()), (byte) 9, (byte) 0, true);
            effs.addSpellEffect(eff);
            performer.setModelName(RandomUtils.randomIllusionModel());
            performer.achievement(ModConfig.journalTransformAchId);
        } else if (roll < 45) {
            Item hat = ItemFactory.createItem(CustomItems.hatId, 90f + Server.rand.nextFloat() * 10f, ItemMaterials.MATERIAL_COTTON, RandomUtils.randomRarity(10, false), null);
            performer.getInventory().insertItem(hat);
            comm.sendNormalServerMessage("You find a funny looking hat!");
        } else if (roll < 50) {
            int roll2 = Server.rand.nextInt(10);
            byte material = Materials.MATERIAL_BONE;
            if (roll2 == 9)
                material = Materials.MATERIAL_GOLD;
            else if (roll2 == 8)
                material = Materials.MATERIAL_SILVER;
            else if (roll2 >= 6)
                material = Materials.MATERIAL_WOOD_OLEANDER;
            Item mask = ItemFactory.createItem(CustomItems.maskId, 90f + Server.rand.nextFloat() * 10f, material, RandomUtils.randomRarity(10, false), null);
            performer.getInventory().insertItem(mask);
            comm.sendNormalServerMessage("You find a creepy skull that someone fashioned into a mask!.. Is it human?!?");
        } else if (roll < 55 && ModConfig.lootableBrooms) {
            Item broom = ItemFactory.createItem(Broom.broomId, 99f, RandomUtils.randomWoodMaterial(), RandomUtils.randomRarity(20, false), null);
            performer.getInventory().insertItem(broom);
            comm.sendNormalServerMessage("You find an enchanted broom!");
        } else if (roll < 60) {
            Item wand = ItemFactory.createItem(CustomItems.witchWandId, 50f, RandomUtils.randomWoodMaterial(), RandomUtils.randomRarity(20, false), null);
            performer.getInventory().insertItem(wand);
            comm.sendNormalServerMessage("You find a magic wand!");
        } else if (roll < 65) {
            Item gem = ItemFactory.createItem(RandomUtils.randomGem(Server.rand.nextInt(4) == 0), 90f + Server.rand.nextFloat() * 10f, ItemMaterials.MATERIAL_CRYSTAL, RandomUtils.randomRarity(10, false), null);
            performer.getInventory().insertItem(gem);
            comm.sendNormalServerMessage(String.format("You find a shiny %s!", gem.getName()));
        } else if (roll < 70) {
            Item coin = ItemFactory.createItem(RandomUtils.randomCoin(8), 90f + Server.rand.nextFloat() * 10f, ItemMaterials.MATERIAL_UNDEFINED, RandomUtils.randomRarity(10, false), null);
            performer.getInventory().insertItem(coin);
            comm.sendNormalServerMessage("You find a shiny coin!");
        } else if (roll < 75) {
            Item shoulder = ItemFactory.createItem(ItemList.shoulderPumpkinHalloween, 85f + Server.rand.nextFloat() * 10f, ItemMaterials.MATERIAL_LEATHER, RandomUtils.randomRarity(30, false), null);
            performer.getInventory().insertItem(shoulder);
            comm.sendNormalServerMessage(String.format("You find a %s!", shoulder.getName()));
        } else if (roll < 80) {
            Item shoulder = ItemFactory.createItem(CustomItems.skullShoulders, 85f + Server.rand.nextFloat() * 10f, ItemMaterials.MATERIAL_LEATHER, RandomUtils.randomRarity(30, false), null);
            performer.getInventory().insertItem(shoulder);
            comm.sendNormalServerMessage(String.format("You find a %s!", shoulder.getName()));
        } else if (roll < 85) {
            Item shoulder = ItemFactory.createItem(CustomItems.humanShoulders, 85f + Server.rand.nextFloat() * 10f, ItemMaterials.MATERIAL_LEATHER, RandomUtils.randomRarity(30, false), null);
            performer.getInventory().insertItem(shoulder);
            comm.sendNormalServerMessage(String.format("You find a %s!", shoulder.getName()));
        } else if (roll < 90) {
            Item mask = ItemFactory.createItem(ItemList.maskTrollHalloween, 85f + Server.rand.nextFloat() * 10f, ItemMaterials.MATERIAL_LEATHER, RandomUtils.randomRarity(30, false), null);
            performer.getInventory().insertItem(mask);
            comm.sendNormalServerMessage(String.format("You find a %s!", mask.getName()));
        } else if (roll < 93) {
            Item bone = ItemFactory.createItem(ItemList.boneCollar, 85f + Server.rand.nextFloat() * 10f, ItemMaterials.MATERIAL_UNDEFINED, RandomUtils.randomRarity(30, true), null);
            performer.getInventory().insertItem(bone);
            comm.sendNormalServerMessage("You find a strange bone!");
        } else if (roll == 93) {
            Item item = ItemFactory.createItem(ItemList.rodTransmutation, 99f, ItemMaterials.MATERIAL_UNDEFINED, (byte) 0, null);
            performer.getInventory().insertItem(item);
            comm.sendNormalServerMessage(String.format("You find a %s!", item.getName()));
        } else if (roll == 94) {
            Item item = ItemFactory.createItem(ItemList.teleportationTwig, 99f, ItemMaterials.MATERIAL_UNDEFINED, (byte) 0, null);
            performer.getInventory().insertItem(item);
            comm.sendNormalServerMessage(String.format("You find a %s!", item.getName()));
        } else if (roll == 95) {
            Item item = ItemFactory.createItem(ItemList.teleportationStone, 99f, ItemMaterials.MATERIAL_UNDEFINED, (byte) 0, null);
            performer.getInventory().insertItem(item);
            comm.sendNormalServerMessage(String.format("You find a %s!", item.getName()));
        } else if (roll == 96) {
            Item item = ItemFactory.createItem(ItemList.potionWoodcutting, 99f, ItemMaterials.MATERIAL_UNDEFINED, (byte) 0, null);
            performer.getInventory().insertItem(item);
            comm.sendNormalServerMessage(String.format("You find a %s!", item.getName()));
        } else if (roll == 97) {
            Item item = ItemFactory.createItem(ItemList.potionMining, 99f, ItemMaterials.MATERIAL_UNDEFINED, (byte) 0, null);
            performer.getInventory().insertItem(item);
            comm.sendNormalServerMessage(String.format("You find a %s!", item.getName()));
        } else if (roll == 98) {
            Item item = ItemFactory.createItem(ItemList.potionButchery, 99f, ItemMaterials.MATERIAL_UNDEFINED, (byte) 0, null);
            performer.getInventory().insertItem(item);
            comm.sendNormalServerMessage(String.format("You find a %s!", item.getName()));
        } else if (roll == 99) {
            int template = 795 + Server.rand.nextInt(16);
            Item item = ItemFactory.createItem(template, 99f, ItemMaterials.MATERIAL_UNDEFINED, (byte) 0, null);
            item.setAuxData((byte) 2);
            performer.getInventory().insertItem(item);
            comm.sendNormalServerMessage(String.format("You find a %s!", item.getName()));

        }
    }

    private void spawnAttackers(Creature performer, int template, int num, boolean reborn) throws
            NoSuchCreatureTemplateException {
        CreatureTemplate tpl = CreatureTemplateFactory.getInstance().getTemplate(template);
        for (int i = 0; i < num; i++) {
            try {
                byte sex = tpl.getSex();
                if (sex == 0 && !tpl.keepSex && Server.rand.nextInt(2) == 0) {
                    sex = 1;
                }
                Creature spawned = Creature.doNew(template, true, performer.getPosX() - 5f + Server.rand.nextFloat() * 10, performer.getPosY() - 5f + Server.rand.nextFloat() * 10, Server.rand.nextInt(360), performer.isOnSurface() ? 0 : -1, "", sex, (byte) 0, Server.rand.nextInt(5) == 0 ? CreatureTypes.C_MOD_CHAMPION : CreatureTypes.C_MOD_NONE, reborn);
                spawned.setOpponent(performer);
            } catch (Exception e) {
                Halloween.logException("Error spawning attackers from gravestone", e);
            }
        }
    }

    public static void addMagicDamage(final Creature performer, final Creature defender, final int pos, double damage, byte type, boolean spell) {
        if (performer != null && performer.getTemplate().getCreatureAI() != null)
            damage = performer.getTemplate().getCreatureAI().causedWound(performer, defender, type, pos, 1, damage);
        if (defender != null && defender.getTemplate().getCreatureAI() != null)
            damage = defender.getTemplate().getCreatureAI().receivedWound(defender, performer, type, pos, 1, damage);
        if (damage > 500.0) {
            if (defender.getBody().getWounds() != null) {
                Wound wound = defender.getBody().getWounds().getWoundTypeAtLocation((byte) pos, type);
                if (wound != null && wound.getType() == type) {
                    defender.setWounded();
                    wound.setBandaged(false);
                    wound.modifySeverity((int) (damage), performer != null && performer.isPlayer(), spell);
                }
            }
            if (WurmId.getType(defender.getWurmId()) == 1) {
                defender.getBody().addWound(new TempWound(type, (byte) pos, (float) damage, defender.getWurmId(), 0.0f, 0.0f, spell));
            } else {
                defender.getBody().addWound(new DbWound(type, (byte) pos, (float) damage, defender.getWurmId(), 0.0f, 0.0f, performer != null && performer.isPlayer(), spell));
            }
        }
    }

    private void fixTerrain(int tileX, int tileY) {
        for (int x = tileX - 3; x <= tileX + 3; x++) {
            for (int y = tileY - 3; y <= tileY + 3; y++) {
                if (x < 0 || y < 0 || x >= Zones.worldTileSizeX || y >= Zones.worldTileSizeY || Zones.isTileProtected(x, y))
                    continue;
                int tile = Server.surfaceMesh.getTile(x, y);
                if (Tiles.decodeType(tile) == Tiles.TILE_TYPE_DIRT_PACKED) {
                    if (Zones.getKingdom(x, y) == 3)
                        Server.surfaceMesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(tile), (byte) Tiles.TILE_TYPE_MYCELIUM, GrassData.encodeGrassTileData(GrassData.GrowthStage.SHORT, GrassData.GrassType.GRASS, GrassData.FlowerType.NONE)));
                    else
                        Server.surfaceMesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(tile), (byte) Tiles.TILE_TYPE_GRASS, GrassData.encodeGrassTileData(GrassData.GrowthStage.SHORT, GrassData.GrassType.GRASS, GrassData.FlowerType.NONE)));
                    Players.getInstance().sendChangedTile(x, y, true, false);
                }
            }
        }

    }
}

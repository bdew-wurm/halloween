package net.bdew.wurm.halloween;

import com.wurmonline.server.creatures.Communicator;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.bdew.wurm.halloween.actions.*;
import net.bdew.wurm.halloween.titles.CustomAchievements;
import net.bdew.wurm.halloween.titles.CustomJournal;
import net.bdew.wurm.halloween.titles.CustomTitles;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.*;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviours;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Halloween implements WurmServerMod, Initable, PreInitable, Configurable, ItemTemplatesCreatedListener, ServerStartedListener, ServerPollListener, PlayerMessageListener {
    private static final Logger logger = Logger.getLogger("Halloween");

    public static void logException(String msg, Throwable e) {
        if (logger != null)
            logger.log(Level.SEVERE, msg, e);
    }

    public static void logWarning(String msg) {
        if (logger != null)
            logger.log(Level.WARNING, msg);
    }

    public static void logInfo(String msg) {
        if (logger != null)
            logger.log(Level.INFO, msg);
    }

    @Override
    public void configure(Properties properties) {
        ModConfig.gravestoneCount = Integer.parseInt(properties.getProperty("gravestoneCount", "0"));
        ModConfig.guardianCount = Integer.parseInt(properties.getProperty("guardianCount", "5"));
        ModConfig.guardianChamps = Integer.parseInt(properties.getProperty("guardianChamps", "3"));

        ModConfig.evilTreeCR = Float.parseFloat(properties.getProperty("evilTreeCR", "14"));
        ModConfig.evilTreeDamage = Float.parseFloat(properties.getProperty("evilTreeDamage", "15"));
        ModConfig.evilTreeArmor = Float.parseFloat(properties.getProperty("evilTreeArmor", "0.6"));

        ModConfig.pumpkinMonsterCR = Float.parseFloat(properties.getProperty("pumpkinMonsterCR", "12"));
        ModConfig.pumpkinMonsterDamage = Float.parseFloat(properties.getProperty("pumpkinMonsterDamage", "10"));
        ModConfig.pumpkinMonsterArmor = Float.parseFloat(properties.getProperty("pumpkinMonsterArmor", "0.7"));

        ModConfig.jackOSpiderCR = Float.parseFloat(properties.getProperty("jackOSpiderCR", "12"));
        ModConfig.jackOSpiderDamage = Float.parseFloat(properties.getProperty("jackOSpiderDamage", "20"));
        ModConfig.jackOSpiderArmor = Float.parseFloat(properties.getProperty("jackOSpiderArmor", "0.8"));

        ModConfig.pumpkinKillerAchId = Integer.parseInt(properties.getProperty("pumpkinKillerAchId", "4100"));
        ModConfig.treeKillerAchId = Integer.parseInt(properties.getProperty("treeKillerAchId", "4101"));
        ModConfig.gravestoneLooterAchId = Integer.parseInt(properties.getProperty("gravestoneLooterAchId", "4102"));
        ModConfig.spiderKillerAchId = Integer.parseInt(properties.getProperty("spiderKillerAchId", "4103"));
        ModConfig.broomRideHiddenAchId = Integer.parseInt(properties.getProperty("broomRideHiddenAchId", "4104"));


        ModConfig.customJournalId = Integer.parseInt(properties.getProperty("customJournalId", "40"));

        ModConfig.journalKillPumpkinsAchId = Integer.parseInt(properties.getProperty("journalKillPumpkinsAchId", "4110"));
        ModConfig.journalKillTreesAchId = Integer.parseInt(properties.getProperty("journalKillTreesAchId", "4111"));
        ModConfig.journalKillSpidersAchId = Integer.parseInt(properties.getProperty("journalKillSpidersAchId", "4112"));
        ModConfig.journalLootGravestonesAchId = Integer.parseInt(properties.getProperty("journalLootGravestonesAchId", "4113"));
        ModConfig.journalBroomRideAchId = Integer.parseInt(properties.getProperty("journalBroomRideAchId", "4114"));
        ModConfig.journalUseWandAchId = Integer.parseInt(properties.getProperty("journalUseWandAchId", "4115"));
        ModConfig.journalEquipItemsAchId = Integer.parseInt(properties.getProperty("journalEquipItemsAchId", "4116"));
        ModConfig.journalTeleportAchId = Integer.parseInt(properties.getProperty("journalTeleportAchId", "4117"));
        ModConfig.journalLightningAchId = Integer.parseInt(properties.getProperty("journalLightningAchId", "4118"));
        ModConfig.journalTransformAchId = Integer.parseInt(properties.getProperty("journalTransformAchId", "4119"));

        ModConfig.pumpkinSmasherTitleId = Integer.parseInt(properties.getProperty("pumpkinSmasherTitleId", "5100"));
        ModConfig.graveRobberTitleId = Integer.parseInt(properties.getProperty("graveRobberTitleId", "5101"));
        ModConfig.hollowTitleId = Integer.parseInt(properties.getProperty("hollowTitleId", "5102"));

        ModConfig.craftablePumpkinHelm = Boolean.parseBoolean(properties.getProperty("craftablePumpkinHelm", "true"));
        ModConfig.updateMaskMaterials = Boolean.parseBoolean(properties.getProperty("updateMaskMaterials", "true"));

        ModConfig.craftableMagicCandle = Boolean.parseBoolean(properties.getProperty("craftableMagicCandle", "true"));
        ModConfig.lootableBrooms = Boolean.parseBoolean(properties.getProperty("lootableBrooms", "true"));

        ModConfig.broomBaseSpeed = Float.parseFloat(properties.getProperty("broomBaseSpeed", "10"));
        ModConfig.broomSpeedEnchantBonus = Float.parseFloat(properties.getProperty("broomSpeedEnchantBonus", "1"));
        ModConfig.broomMaxSlope = Float.parseFloat(properties.getProperty("broomMaxSlope", "250"));
        ModConfig.broomQlLoss = Float.parseFloat(properties.getProperty("broomQlLoss", "0.0001"));
    }

    @Override
    public void preInit() {
        ModActions.init();
        ModVehicleBehaviours.init();

        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();


            CtClass ctCommunicator = classPool.getCtClass("com.wurmonline.server.creatures.Communicator");
            CtClass ctItem = classPool.getCtClass("com.wurmonline.server.items.Item");
            CtClass ctZone = classPool.getCtClass("com.wurmonline.server.zones.Zone");
            CtClass ctCreature = classPool.getCtClass("com.wurmonline.server.creatures.Creature");
            CtClass ctMovementScheme = classPool.getCtClass("com.wurmonline.server.creatures.MovementScheme");
            CtClass ctVehicle = classPool.getCtClass("com.wurmonline.server.behaviours.Vehicle");
            CtClass ctPlayer = classPool.getCtClass("com.wurmonline.server.players.Player");

            ctCommunicator.getMethod("sendItem", "(Lcom/wurmonline/server/items/Item;JZ)V")
                    .insertAfter("net.bdew.wurm.halloween.Hooks.sendItemHook(this, $1);");

            ctCommunicator.getMethod("sendRemoveItem", "(Lcom/wurmonline/server/items/Item;)V")
                    .insertAfter("net.bdew.wurm.halloween.Hooks.removeItemHook(this, $1);");

            ctItem.getMethod("getSizeMod", "()F")
                    .insertAfter("$_ = $_ * net.bdew.wurm.halloween.Hooks.getSizeMod(this);");

            ctZone.getMethod("addItem", "(Lcom/wurmonline/server/items/Item;ZZZ)V")
                    .insertAfter("if ($4) net.bdew.wurm.halloween.Hooks.addItemLoading($1);");

            ctCreature.getMethod("setVehicle", "(JZBII)V")
                    .insertBefore("net.bdew.wurm.halloween.Hooks.setVehicle(this, this.vehicle, $1);");

            ctMovementScheme.getMethod("handleZError", "(FF)Z")
                    .insertBefore("if (net.bdew.wurm.halloween.Hooks.checkRelaxedZError(this.creature, $1, $2)) return false;");

            ctVehicle.getMethod("calculateNewVehicleSpeed", "(Z)B")
                    .insertAfter("$_ = net.bdew.wurm.halloween.Hooks.modifySpeed(this, $_);");

            ctPlayer.getMethod("addTileMoved", "()V")
                    .insertAfter("net.bdew.wurm.halloween.Hooks.playerMovedTile(this);");

            ctItem.getMethod("sendWear", "(Lcom/wurmonline/server/items/Item;B)V")
                    .insertAfter("if (!item.isBodyPartAttached()) net.bdew.wurm.halloween.Hooks.sendWearHook(item, this.getOwnerOrNull());");

            CustomTitles.register();

        } catch (CannotCompileException | NotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void init() {
    }

    @Override
    public void onItemTemplatesCreated() {
        try {
            CustomItems.registerGravestone();
            CustomItems.registerHat();
            CustomItems.registerMask();
            CustomItems.registerPumpkinHelm();
            CustomItems.registerCandles();
            CustomItems.registerCauldron();
            CustomItems.registerWand();
            CustomItems.registerCoffin();
            CustomItems.registerWings();
            CustomItems.regiesterLamps();
            if (ModConfig.updateMaskMaterials)
                DbFix.fixMaskMaterial();
            CustomCreatures.createEvilTreeTemplate();
            CustomCreatures.createPumpkinMonsterTemplate();
            CustomCreatures.createJackOSpiderTemplate();
            Broom.register();
            CustomAchievements.register();
            CustomJournal.register();
        } catch (NoSuchFieldException | IllegalAccessException | IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onServerStarted() {
        GravestoneTracker.started();
        ModActions.registerAction(new InvestigateGravestoneAction());
        ModActions.registerAction(new UseWandAction());
        ModActions.registerBehaviourProvider(new BroomBehaviour());
        ModActions.registerActionPerformer(new MountBroomPerformer());
        ModActions.registerActionPerformer(new DismountBroomPerformer());
    }

    @Override
    public void onServerPoll() {
        GravestoneTracker.tick();
    }


    @Override
    @Deprecated
    public boolean onPlayerMessage(Communicator communicator, String message) {
        return false;
    }

    @Override
    public MessagePolicy onPlayerMessage(Communicator communicator, String message, String title) {
        if (communicator.player.getPower() > 0 && message.startsWith("#"))
            return CommandHandler.handleCommand(communicator, message);
        return MessagePolicy.PASS;
    }
}

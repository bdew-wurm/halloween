package net.bdew.wurm.halloween;

import com.wurmonline.server.Items;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.behaviours.Vehicle;
import com.wurmonline.server.behaviours.Vehicles;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.shared.constants.IconConstants;
import com.wurmonline.shared.constants.ItemMaterials;
import com.wurmonline.shared.util.MaterialUtilities;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.gotti.wurmunlimited.modsupport.items.ModItems;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviour;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviours;
import org.gotti.wurmunlimited.modsupport.vehicles.VehicleFacade;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Broom {
    public static int broomId;
    public static ItemTemplate template;

    private static Set<Long> embarkingIds = new HashSet<>();
    private static Map<Long, Integer> broomMoveCounter = new HashMap<>();

    public static void register() throws IOException {
        template = new ItemTemplateBuilder("bdew.halloween.broom")
                .name("magic broom", "magic brooms", "An enchanted broom, favorite transportation method of witches and crones.")
                .modelName("model.transports.broom.")
                .imageNumber((short) IconConstants.ICON_FLOAT_TWIG)
                .weightGrams(1000)
                .dimensions(15, 15, 100)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_VEHICLE,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_SUPPORTS_SECONDARY_COLOR,
                        ItemTypes.ITEM_TYPE_NO_IMPROVE,
                })
                .build();

        template.setSecondryItem("thatch");
        template.setDyeAmountGrams(100, 150);

        broomId = template.getTemplateId();

        ModItems.addModelNameProvider(broomId, item -> {
            StringBuilder name = new StringBuilder(template.getModelName());

            if (!embarkingIds.contains(item.getWurmId())) {
                Vehicle v = Vehicles.getVehicleForId(item.getWurmId());
                if (v == null || v.getPilotSeat() == null || v.getPilotSeat().getOccupant() == -10L)
                    name.append("standing.");
            }

            name.append(MaterialUtilities.getMaterialString(item.getMaterial()));

            return name.toString();
        });

        ModVehicleBehaviours.addItemVehicle(broomId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                facade.createPassengerSeats(0);
                facade.setSeatOffset(0, -0.43f, 0, 0f);
                facade.setSeatFightMod(0, 0.7f, 0.9f);
                facade.setMaxSpeed(ModConfig.broomBaseSpeed);
                facade.setMaxDepth(-2499.99f); // must be > -2500 due to wogic
                facade.setMaxHeightDiff(ModConfig.broomMaxSlope / 1000f);
                vehicle.setSkillNeeded(0);
            }
        });
    }

    public static void setEmbarking(Item item, boolean embarking) {
        if (embarking)
            embarkingIds.add(item.getWurmId());
        else
            embarkingIds.remove(item.getWurmId());
    }

    public static void startCounting(long id, Creature driver) {
        broomMoveCounter.put(id, 0);
    }

    public static void stopCounting(long id, Creature driver) {
        Integer counter = broomMoveCounter.remove(id);
        if (counter != null && counter > 0) tilesMoved(id, counter, driver);
    }

    public static void countMoveTile(long id, Creature driver) {
        if (broomMoveCounter.containsKey(id)) {
            int count = broomMoveCounter.get(id) + 1;
            if (count >= 100) {
                count = 0;
                tilesMoved(id, 100, driver);
            }
            broomMoveCounter.put(id, count);
        }
    }

    private static void tilesMoved(long id, int tiles, Creature driver) {
        if (ModConfig.broomQlLoss > 0) {
            Items.getItemOptional(id).ifPresent(item -> {
                float ql = item.getQualityLevel() - ModConfig.broomQlLoss * tiles;
                item.setQualityLevel(ql);
                if (ql < 10f) {
                    if (driver.hasLink())
                        driver.getCommunicator().sendAlertServerMessage("The broom slowly sinks to the ground as it's magic fizzles.", (byte) 1);
                    driver.disembark(true);
                }
            });
        }
    }
}

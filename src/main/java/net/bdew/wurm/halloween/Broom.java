package net.bdew.wurm.halloween;

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
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.gotti.wurmunlimited.modsupport.items.ModItems;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviour;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviours;
import org.gotti.wurmunlimited.modsupport.vehicles.VehicleFacade;

import java.io.IOException;

public class Broom {
    public static int broomId;
    public static ItemTemplate template;

    public static void register() throws IOException {
        template = new ItemTemplateBuilder("bdew.halloween.broom")
                .name("magic broom", "magic brooms", "An enchanted broom, favorite transportation method of witches and crones.")
                .modelName("model.transports.broom.")
                .imageNumber((short) IconConstants.ICON_FLOAT_TWIG)
                .weightGrams(1000)
                .dimensions(15, 15, 100)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.vehicleBehaviour)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_VEHICLE,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_SUPPORTS_SECONDARY_COLOR,
                        ItemTypes.ITEM_TYPE_FLOATING,
                })
                .build();

        template.setSecondryItem("thatch");
        template.setDyeAmountGrams(100, 150);

        broomId = template.getTemplateId();

        ModItems.addModelNameProvider(broomId, item -> {
            StringBuilder name = new StringBuilder(template.getModelName());

            Vehicle v = Vehicles.getVehicleForId(item.getWurmId());
            if (v == null || v.getPilotSeat() == null || v.getPilotSeat().getOccupant() == -10L)
                name.append("standing.");

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
                facade.setCommandType((byte) 1);
                facade.setSeatOffset(0, -0.43f, 0, 0f);
                facade.setSeatFightMod(0, 0.7f, 0.9f);
                facade.setMaxSpeed(4);
                try {
                    ReflectionUtil.setPrivateField(vehicle, ReflectionUtil.getField(Vehicle.class, "windImpact"), (byte) 1);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
                facade.setMaxDepth(-2499.99f); // must be > -2500 due to wogic
                facade.setMaxHeightDiff(0.12f);
                vehicle.setSkillNeeded(0);
            }
        });
    }
}

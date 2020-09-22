package net.bdew.wurm.halloween;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.behaviours.Vehicle;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.shared.constants.IconConstants;
import com.wurmonline.shared.constants.ItemMaterials;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
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
                        ItemTypes.ITEM_TYPE_OWNER_MOVEABLE,
                        ItemTypes.ITEM_TYPE_OWNER_TURNABLE,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                })
                .build();

        broomId = template.getTemplateId();

        ModVehicleBehaviours.addItemVehicle(broomId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                facade.createPassengerSeats(0);
                facade.setSeatOffset(0, 0, 0, 0);
                facade.setSeatFightMod(0, 0.7f, 0.9f);
                facade.setMaxSpeed(1);
                facade.setMaxDepth(-2500);
                facade.setMaxHeightDiff(0.04f);
                vehicle.setMaxAllowedLoadDistance(0);
                vehicle.setSkillNeeded(0);
            }
        });
    }
}

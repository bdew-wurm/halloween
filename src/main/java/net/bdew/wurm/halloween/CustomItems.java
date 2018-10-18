package net.bdew.wurm.halloween;

import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.shared.constants.IconConstants;
import com.wurmonline.shared.constants.ItemMaterials;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;

import java.io.IOException;

public class CustomItems {
    public static int gravestoneId;

    static void registerGravestone() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("bdew.halloween.gravestone")
                .name("ominous gravestone", "ominous gravestone", "An ominous gravestone. Investigating it might yield a treat... or a trick.")
                .modelName("model.decoration.gravestone.buried.")
                .imageNumber((short) IconConstants.ICON_DECO_GRAVESTONE)
                .weightGrams(100000)
                .dimensions(20, 20, 400)
                .decayTime(Long.MAX_VALUE)
                .material(ItemMaterials.MATERIAL_STONE)
                .behaviourType((short) 1)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NOMOVE,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                })
                .build();

        gravestoneId = temp.getTemplateId();
    }
}

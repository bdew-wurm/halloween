package net.bdew.wurm.halloween;

import com.wurmonline.server.Server;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.shared.constants.ItemMaterials;

import java.util.Arrays;
import java.util.List;

public class RandomUtils {
    private static List<Byte> woodMaterials = Arrays.asList(
            ItemMaterials.MATERIAL_WOOD_BIRCH,
            ItemMaterials.MATERIAL_WOOD_PINE,
            ItemMaterials.MATERIAL_WOOD_OAK,
            ItemMaterials.MATERIAL_WOOD_CEDAR,
            ItemMaterials.MATERIAL_WOOD_WILLOW,
            ItemMaterials.MATERIAL_WOOD_MAPLE,
            ItemMaterials.MATERIAL_WOOD_APPLE,
            ItemMaterials.MATERIAL_WOOD_LEMON,
            ItemMaterials.MATERIAL_WOOD_OLIVE,
            ItemMaterials.MATERIAL_WOOD_CHERRY,
            ItemMaterials.MATERIAL_WOOD_LAVENDER,
            ItemMaterials.MATERIAL_WOOD_ROSE,
            ItemMaterials.MATERIAL_WOOD_THORN,
            ItemMaterials.MATERIAL_WOOD_GRAPE,
            ItemMaterials.MATERIAL_WOOD_CAMELLIA,
            ItemMaterials.MATERIAL_WOOD_OLEANDER,
            ItemMaterials.MATERIAL_WOOD_CHESTNUT,
            ItemMaterials.MATERIAL_WOOD_WALNUT,
            ItemMaterials.MATERIAL_WOOD_FIR,
            ItemMaterials.MATERIAL_WOOD_LINDEN,
            ItemMaterials.MATERIAL_WOOD_IVY,
            ItemMaterials.MATERIAL_WOOD_HAZELNUT,
            ItemMaterials.MATERIAL_WOOD_ORANGE,
            ItemMaterials.MATERIAL_WOOD_RASPBERRY,
            ItemMaterials.MATERIAL_WOOD_BLUEBERRY,
            ItemMaterials.MATERIAL_WOOD_LINGONBERRY
    );

    private static List<String> illusionModels = Arrays.asList(
            "model.creature.deathcrawler",
            "model.creature.dragon.black",
            "model.creature.dragon.blue",
            "model.creature.dragon.green",
            "model.creature.dragon.red",
            "model.creature.dragon.white",
            "model.creature.drake.black",
            "model.creature.drake.blue",
            "model.creature.drake.green",
            "model.creature.drake.red",
            "model.creature.drake.spirit",
            "model.creature.drake.white",
            "model.creature.eagle.spirit",
            "model.creature.eviltree",
            "model.creature.fish.blue.whale",
            "model.creature.fish.dolphin",
            "model.creature.fish.octopus",
            "model.creature.fish.seal",
            "model.creature.fish.seal.cub",
            "model.creature.fish.shark.huge",
            "model.creature.humanoid.avenger.light",
            "model.creature.humanoid.chicken",
            "model.creature.humanoid.demon.sol",
            "model.creature.humanoid.giant.epiphany",
            "model.creature.humanoid.giant.forest",
            "model.creature.humanoid.giant.incarnation",
            "model.creature.humanoid.giant.juggernaut",
            "model.creature.humanoid.giant.manifestation",
            "model.creature.humanoid.goblin.leader",
            "model.creature.humanoid.goblin.standard",
            "model.creature.humanoid.gorilla.mountain",
            "model.creature.humanoid.hen",
            "model.creature.humanoid.human.bartender",
            "model.creature.humanoid.human.evilsanta",
            "model.creature.humanoid.human.guard.tower",
            "model.creature.humanoid.human.salesman",
            "model.creature.humanoid.human.santa",
            "model.creature.humanoid.human.skeleton",
            "model.creature.humanoid.human.spirit.wraith",
            "model.creature.humanoid.jackal.rift",
            "model.creature.humanoid.jackal.rift.caster",
            "model.creature.humanoid.jackal.rift.summoner",
            "model.creature.humanoid.kyklops",
            "model.creature.humanoid.lavacreature",
            "model.creature.humanoid.nogump.son",
            "model.creature.humanoid.ogre.rift",
            "model.creature.humanoid.ogre.rift.mage",
            "model.creature.humanoid.pheasant",
            "model.creature.humanoid.rooster",
            "model.creature.humanoid.troll.king",
            "model.creature.humanoid.troll.standard",
            "model.creature.humanoid.warmaster.rift",
            "model.creature.multiped.scorpion",
            "model.creature.multiped.scorpion.hell",
            "model.creature.multiped.spider.fog",
            "model.creature.multiped.spider.huge",
            "model.creature.multiped.spider.lava",
            "model.creature.pumpkinmonster",
            "model.creature.quadraped.bear.black",
            "model.creature.quadraped.bear.brown",
            "model.creature.quadraped.beast.rift",
            "model.creature.quadraped.bison",
            "model.creature.quadraped.boar.wild",
            "model.creature.quadraped.bull",
            "model.creature.quadraped.calf",
            "model.creature.quadraped.cat.wild",
            "model.creature.quadraped.cow",
            "model.creature.quadraped.crab",
            "model.creature.quadraped.crocodile",
            "model.creature.quadraped.deer",
            "model.creature.quadraped.dog",
            "model.creature.quadraped.dog.hell",
            "model.creature.quadraped.easterbunny",
            "model.creature.quadraped.foal",
            "model.creature.quadraped.horse",
            "model.creature.quadraped.horse.hell",
            "model.creature.quadraped.horse.hell.foal",
            "model.creature.quadraped.hyena.rabid",
            "model.creature.quadraped.insect.cavebug",
            "model.creature.quadraped.lamb",
            "model.creature.quadraped.lion.mountain",
            "model.creature.quadraped.pig",
            "model.creature.quadraped.rat.large",
            "model.creature.quadraped.sheep",
            "model.creature.quadraped.tortoise",
            "model.creature.quadraped.unicorn",
            "model.creature.quadraped.unicorn.foal",
            "model.creature.quadraped.wolf.black",
            "model.creature.quadraped.wolf.worg",
            "model.creature.snake.anaconda",
            "model.creature.snake.kingcobra",
            "model.creature.snake.serpent.sea",
            "model.creature.spawn.uttacha",
            "model.food.cake"
    );

    public static byte randomWoodMaterial() {
        return woodMaterials.get(Server.rand.nextInt(woodMaterials.size()));
    }

    public static String randomIllusionModel() {
        return illusionModels.get(Server.rand.nextInt(illusionModels.size()));
    }

    public static byte randomRarity(int upgradeChance, boolean alwaysRare) {
        byte rarity = 0;
        if (alwaysRare || Server.rand.nextInt(upgradeChance) == 0) {
            rarity = 1;
            if (Server.rand.nextInt(upgradeChance) == 0) {
                rarity = 2;
                if (Server.rand.nextInt(upgradeChance) == 0) {
                    rarity = 3;
                }
            }
        }
        return rarity;
    }

    public static int randomGem(boolean star) {
        switch (Server.rand.nextInt(5)) {
            case 0:
                if (star) return ItemList.diamondStar;
                else return ItemList.diamond;
            case 1:
                if (star) return ItemList.emeraldStar;
                else return ItemList.emerald;
            case 2:
                if (star) return ItemList.rubyStar;
                else return ItemList.ruby;
            case 3:
                if (star) return ItemList.opalBlack;
                else return ItemList.opal;
            case 4:
            default:
                if (star) return ItemList.sapphireStar;
                else return ItemList.sapphire;
        }
    }

    public static int randomCoin(int level) {
        switch (Server.rand.nextInt(level)) {
            case 0:
            case 1:
                return ItemList.coinCopperFive;
            case 2:
            case 3:
                return ItemList.coinCopperTwenty;
            case 4:
                return ItemList.coinSilver;
            case 5:
            default:
                return ItemList.coinSilverFive;
        }
    }

}

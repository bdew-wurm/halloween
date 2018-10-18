package net.bdew.wurm.halloween.titles;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import net.bdew.wurm.halloween.ModConfig;

public class CustomTitles {
    public static void register(ClassPool cp) throws NotFoundException, BadBytecode, CannotCompileException {
        TitleInjector injector = new TitleInjector(cp);
        injector.addTitle("PumpkinSmasher", ModConfig.pumpkinSmasherTitleId, "Pumpkin Smasher", "Pumpkin Smasher", -1, "NORMAL");
        injector.addTitle("GraveRobber", ModConfig.graveRobberTitleId, "Grave Robber", "Grave Robber", -1, "NORMAL");
    }
}

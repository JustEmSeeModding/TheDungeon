package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class ModCuriosDataProvider extends CuriosDataProvider {
    public ModCuriosDataProvider(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(TheDungeon.MOD_ID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        this.createSlot("trinket").size(1);
        this.createSlot("effigy").size(1);
        this.createEntities("player_default").addPlayer().addSlots("trinket", "effigy");
    }
}

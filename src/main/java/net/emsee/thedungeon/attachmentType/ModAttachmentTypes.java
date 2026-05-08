package net.emsee.thedungeon.attachmentType;

import com.mojang.serialization.Codec;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeonClass.ModClasses;
import net.emsee.thedungeon.dungeonClass.ModSubClasses;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ModAttachmentTypes {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TheDungeon.MOD_ID);

    /**
     * the gamemode saved before entering a dungeon
     */
    public static final Supplier<AttachmentType<Integer>> SAVED_GAMEMODE = ATTACHMENT_TYPES.register(
            "saved_gamemode", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<String>> PLAYER_CLASS = ATTACHMENT_TYPES.register(
            "player_dungeon_class", () -> AttachmentType.builder(() -> ModClasses.CLASSLESS.getId().getPath()).serialize(Codec.string(0,100)).build()
    );

    public static final Supplier<AttachmentType<String>> PLAYER_SUBCLASS = ATTACHMENT_TYPES.register(
            "player_dungeon_subclass", () -> AttachmentType.builder(() -> ModSubClasses.CLASSLESS.getId().getPath()).serialize(Codec.string(0,100)).build()
    );

    public static final Supplier<AttachmentType<PreDeathTotemInventorySave>> PRE_DEATH_TOTEM_INVENTORY_SAVE = ATTACHMENT_TYPES.register(
            "pre_death_totem_inventory_save", () -> AttachmentType.builder(() -> new PreDeathTotemInventorySave(null)).serialize(PreDeathTotemInventorySave.CODEC).build()
    );

    public static void register(IEventBus eventBus){
        ATTACHMENT_TYPES.register(eventBus);

    }
}

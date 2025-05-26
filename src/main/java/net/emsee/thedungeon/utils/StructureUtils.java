package net.emsee.thedungeon.utils;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public final class StructureUtils {
    @Nullable
    public static StructureTemplate getTemplate(final ServerLevel level, final ResourceLocation resourceLocation) {
        if (level == null) {
            return null;
        }
        StructureTemplateManager templateManager = level.getStructureManager();
        Optional<StructureTemplate> template = templateManager.get(resourceLocation);
        if (template.isEmpty()) {
            TheDungeon.LOGGER.error("Failed to load template for {}", resourceLocation);
        }
        return template.orElse(null);
    }

    public static Holder<StructureTemplatePool> getTemplatePoolHolder(final ServerLevel level, final ResourceLocation resourceLocation) {
        if (level == null) {
            return null;
        }
        RegistryAccess registryaccess = level.registryAccess();

        Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registries.TEMPLATE_POOL);
        Logger logger = TheDungeon.LOGGER;

        logger.info("searcing for {} in {}", resourceLocation, registry);
        Holder<StructureTemplatePool> holder = registry.getHolder(resourceLocation).orElse(null);
        if (holder != null) {
            logger.info("found poolkey: {}", registry.getKey(holder.value()));
            logger.info("found pool: {}", holder.value());
            logger.info("holder: {}", holder);
        } else {
            logger.error("could not find structure: {}", resourceLocation);
        }
        return holder;
    }
}

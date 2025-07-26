package net.emsee.thedungeon.events;

import net.emsee.thedungeon.attachmentType.ModAttachmentTypes;
import net.emsee.thedungeon.criterion.ModCriteriaTriggerTypes;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;


public final class ModDungeonCalledEvents {
    public static void playerTeleportDungeon(Player player, int portalID, DungeonRank rank) {
        Level level = player.level();
        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            MinecraftServer server = level.getServer();
            assert server != null;

            BlockPos teleportPos = new BlockPos(0, 0, 0);
            ResourceKey<Level> resourceKey = null;

            boolean returnFromDungeon = player.level().dimension().equals(ModDimensions.DUNGEON_LEVEL_KEY);
            if (returnFromDungeon) {
                teleportPos = serverPlayer.getRespawnPosition();
                if (teleportPos == null) {
                    teleportPos = server.overworld().getSharedSpawnPos();
                }
                resourceKey = serverPlayer.getRespawnDimension();
            } else if (rank != null) {
                if (!player.isCreative() && !CheckInventory(player)) return;
                teleportPos = GlobalDungeonManager.getPortalPosition(server, portalID, rank).above(2);
                resourceKey = ModDimensions.DUNGEON_LEVEL_KEY;
            }

            if (resourceKey == null) return;
            ServerLevel portalDimension = server.getLevel(resourceKey);

            teleport(player, portalDimension, teleportPos);
            setPlayerGameMode(player, returnFromDungeon);
        }
    }

    private static boolean CheckInventory(Player player) {
        boolean hasInvalidItem = false;
        for (ItemStack stack : player.getInventory().items) {
            if (hasInvalidItem) break;
            if (stack.getCount() == 0) continue;
            if (!(stack.getItem() instanceof IDungeonCarryItem ||
                    (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof IDungeonCarryItem))) {
                hasInvalidItem = true;
            }
        }
        if (CuriosApi.getCuriosInventory(player).isPresent()) {
            IItemHandlerModifiable curios = CuriosApi.getCuriosInventory(player).get().getEquippedCurios();
            for (int i = 0; i < curios.getSlots(); i++) {
                if (hasInvalidItem) break;
                ItemStack stack = curios.getStackInSlot(i);
                if (stack.getCount() == 0) continue;
                if (!(stack.getItem() instanceof IDungeonCarryItem ||
                        (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof IDungeonCarryItem))) {
                    hasInvalidItem = true;
                }
            }
        }

        if (hasInvalidItem) {
            player.displayClientMessage(Component.translatable("message.thedungeon.dungeon_portal.non_dungeon_items"), true);
            if (player instanceof ServerPlayer serverPlayer)
                ModCriteriaTriggerTypes.FAILED_DUNGEON_TRAVEL.get().trigger(serverPlayer);
        }
        return !hasInvalidItem;
    }

    private static void teleport(Player player, ServerLevel portalDimension, BlockPos teleportPos) {
        if (portalDimension != null/* && !eventPlayer.isPassenger()*/) {
            player.teleportTo(portalDimension, teleportPos.getX() + .5, teleportPos.getY(), teleportPos.getZ() + .5, RelativeMovement.ALL, player.getYRot(), player.getXRot());
            if (!player.isCreative()) {
                player.removeAllEffects();
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 60, 0), player);
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 5), player);
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 5), player);
            }
        }
    }

    public static void setPlayerGameMode(Player player, boolean returnFromDungeon) {
        if (player instanceof ServerPlayer serverPlayer && !player.isCreative()) {
            if (returnFromDungeon) {
                int gameMode = player.getData(ModAttachmentTypes.SAVED_GAMEMODE);
                serverPlayer.setGameMode(GameType.byId(gameMode));
            } else {
                player.setData(ModAttachmentTypes.SAVED_GAMEMODE, serverPlayer.gameMode.getGameModeForPlayer().getId());
                serverPlayer.setGameMode(GameType.ADVENTURE);
            }
        }
    }
}
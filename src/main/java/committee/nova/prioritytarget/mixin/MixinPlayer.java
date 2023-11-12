package committee.nova.prioritytarget.mixin;

import committee.nova.prioritytarget.api.ITargetablePlayer;
import committee.nova.prioritytarget.api.PriorityTargetPredicates;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static committee.nova.prioritytarget.PriorityTarget.*;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements ITargetablePlayer {
    @Unique
    private int prioritytarget$targets;

    protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void inject$tick(CallbackInfo ci) {
        final Level world = level();
        if (world.isClientSide) return;
        if (world.getGameTime() % refreshInterval.get() != 0) return;
        final int w = areaWidth.get();
        final int h = areaHeight.get();
        int targets = world.getEntitiesOfClass(Mob.class,
                new AABB(position().add(w, h, w), position().add(-w, -h, -w)),
                e -> (PriorityTargetPredicates.isTargeting(e, (Player) (Object) this))).size();
        final FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByte(0);
        buf.writeInt(targets);
        if ((Player) (Object) this instanceof ServerPlayer p) ServerPlayNetworking.send(p, PACKET, buf);
    }

    @Override
    public void prioritytarget$setTargets(int targets) {
        this.prioritytarget$targets = targets;
    }

    @Override
    public int prioritytarget$getTargets() {
        return prioritytarget$targets;
    }
}

package committee.nova.prioritytarget.common.network.msg;

import committee.nova.prioritytarget.client.network.handler.TargetPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TargetMessage {
    private final int targets;

    public TargetMessage(FriendlyByteBuf buf) {
        targets = buf.readInt();
    }

    public TargetMessage(int targets) {
        this.targets = targets;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(targets);
    }

    public void handler(Supplier<NetworkEvent.Context> sup) {
        final NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> TargetPacketHandler.handle(targets)));
        ctx.setPacketHandled(true);
    }
}

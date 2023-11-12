package committee.nova.prioritytarget.client;

import committee.nova.prioritytarget.PriorityTarget;
import committee.nova.prioritytarget.api.ITargetablePlayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.player.Player;

public class PriorityTargetClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(PriorityTarget.PACKET, (client, handler, buf, responseSender) -> {
            buf.readByte();
            final int target = buf.readInt();
            client.execute(() -> {
                final Player player = client.player;
                if (player == null) return;
                ((ITargetablePlayer) player).prioritytarget$setTargets(target);
            });
        });
    }
}

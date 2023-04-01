package committee.nova.prioritytarget.client.network.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class TargetPacketHandler {
    public static void handle(int targets) {
        final Player player = Minecraft.getInstance().player;
        if (player == null) return;
        player.getPersistentData().putInt("targets_pt", targets);
    }
}

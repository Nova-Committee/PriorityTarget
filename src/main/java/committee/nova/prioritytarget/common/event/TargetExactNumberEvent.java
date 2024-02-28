package committee.nova.prioritytarget.common.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class TargetExactNumberEvent extends PlayerEvent {
    private final int number;

    public TargetExactNumberEvent(Player player, int number) {
        super(player);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}

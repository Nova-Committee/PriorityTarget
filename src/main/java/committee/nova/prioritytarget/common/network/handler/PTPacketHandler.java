package committee.nova.prioritytarget.common.network.handler;

import committee.nova.prioritytarget.PriorityTarget;
import committee.nova.prioritytarget.common.network.msg.TargetMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PTPacketHandler {
    private static boolean registered = false;
    private static int id = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PriorityTarget.MODID, PriorityTarget.MODID),
            () -> NetworkRegistry.ACCEPTVANILLA,
            NetworkRegistry.ACCEPTVANILLA::equals,
            NetworkRegistry.ACCEPTVANILLA::equals
    );

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PTPacketHandler::register);
    }

    public static void register() {
        if (registered) return;
        INSTANCE.registerMessage(id++,
                TargetMessage.class,
                TargetMessage::toBytes,
                TargetMessage::new,
                TargetMessage::handler);
        registered = true;
    }
}

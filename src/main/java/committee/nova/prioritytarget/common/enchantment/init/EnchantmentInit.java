package committee.nova.prioritytarget.common.enchantment.init;

import committee.nova.prioritytarget.PriorityTarget;
import committee.nova.prioritytarget.common.enchantment.impl.PriorityTargetEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> Enchantments = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PriorityTarget.MODID);

    public static final RegistryObject<Enchantment> spiderSense = Enchantments.register("priority_target", PriorityTargetEnchantment::new);

    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Enchantments.register(bus);
    }
}

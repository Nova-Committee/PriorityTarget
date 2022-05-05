package committee.nova.prioritytarget;

import committee.nova.prioritytarget.api.PriorityTargetPredicates;
import committee.nova.prioritytarget.common.enchantment.init.EnchantmentInit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PriorityTarget.MODID)
public class PriorityTarget {
    public static final String MODID = "prioritytarget";
    public static final String PLAYER_UUID = "target_player_uuid";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ForgeConfigSpec.IntValue refreshInterval;
    public static final ForgeConfigSpec.IntValue areaWidth;
    public static final ForgeConfigSpec.IntValue areaHeight;
    public static final ForgeConfigSpec.BooleanValue treasure;
    public static final ForgeConfigSpec.BooleanValue omitDetected;

    static {
        final ForgeConfigSpec.Builder commonBuilder = new ForgeConfigSpec.Builder();
        commonBuilder.comment("PriorityTarget Common Configuration");
        refreshInterval = commonBuilder.comment("The time of the refresh interval. After each interval, the enchanted chest armor will check the amount of mobs targeting the player")
                .defineInRange("refreshInterval", 5, 1, 6000);
        areaWidth = commonBuilder.comment("The width of the area, in which the mobs targeting the player will be counted.", "Width = value * 2 + 1")
                .defineInRange("areaWidth", 32, 5, 100);
        areaHeight = commonBuilder.comment("The height of the area, in which the mobs targeting the player will be counted.", "Height = value * 2 + 1")
                .defineInRange("areaHeight", 10, 3, 192);
        treasure = commonBuilder.comment("If true, the priority target enchantment will be a treasure enchantment")
                .define("isTreasure", false);
        COMMON_CONFIG = commonBuilder.build();
        final ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();
        clientBuilder.comment("PriorityTarget Client Configuration");
        omitDetected = clientBuilder.comment("If true, when the number of priority targeting mobs is displayed, the \"! detected\" word will not be displayed")
                .define("omitDetected", false);
        CLIENT_CONFIG = clientBuilder.build();
    }


    public PriorityTarget() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
        EnchantmentInit.init();
        PriorityTargetPredicates.init();
        MinecraftForge.EVENT_BUS.addListener(this::onArmorTick);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onArmorTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) return;
        final Player player = event.player;
        final Level world = player.level;
        if (world.getGameTime() % refreshInterval.get() != 0) return;
        if (event.phase != TickEvent.Phase.END) return;
        final ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.spiderSense.get(), chestplate) < 1) return;
        final int rawWidth = areaWidth.get();
        final int rawHeight = areaHeight.get();
        final int target = world.getEntitiesOfClass(Mob.class,
                new AABB(player.position().add(rawWidth, rawHeight, rawWidth), player.position().add(-rawWidth, -rawHeight, -rawWidth)),
                e -> (PriorityTargetPredicates.isTargeting(e, player))).size();
        chestplate.getOrCreateTag().putInt("targeted_entities", target);
    }
}

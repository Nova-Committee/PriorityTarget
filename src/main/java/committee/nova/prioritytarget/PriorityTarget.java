package committee.nova.prioritytarget;

import committee.nova.prioritytarget.api.PriorityTargetPredicates;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PriorityTarget implements ModInitializer {
    public static final String MODID = "prioritytarget";
    public static final ResourceLocation PACKET = new ResourceLocation(MODID, MODID);
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ForgeConfigSpec.IntValue refreshInterval;
    public static final ForgeConfigSpec.IntValue areaWidth;
    public static final ForgeConfigSpec.IntValue areaHeight;
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
        COMMON_CONFIG = commonBuilder.build();
        final ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();
        clientBuilder.comment("PriorityTarget Client Configuration");
        omitDetected = clientBuilder.comment("If true, when the number of priority targeting mobs is displayed, the \"! detected\" word will not be displayed")
                .define("omitDetected", false);
        CLIENT_CONFIG = clientBuilder.build();
    }

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, COMMON_CONFIG);
        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.CLIENT, CLIENT_CONFIG);
        PriorityTargetPredicates.init();
    }
}

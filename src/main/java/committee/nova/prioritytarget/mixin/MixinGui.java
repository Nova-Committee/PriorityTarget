package committee.nova.prioritytarget.mixin;

import committee.nova.prioritytarget.client.overlay.Overlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderCrosshair(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void inject$render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        Overlay.indicatorRender(Minecraft.getInstance().getWindow(), guiGraphics);
    }
}

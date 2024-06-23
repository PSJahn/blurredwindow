package de.psjahn.blurredwindow.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import de.psjahn.blurredwindow.BlurredWindow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {
    @Shadow protected abstract void init();

    @Inject(method = "renderBackgroundTexture", at = @At("HEAD"), cancellable = true)
    private void removeBackgroundTexture(CallbackInfo info) {
        if(!MinecraftClient.getInstance().getWindow().isFullscreen()) info.cancel();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
    private void replaceFooter(DrawContext instance, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        RenderSystem.enableBlend();
        instance.drawTexture(BlurredWindow.FOOTER_SEPARATOR_TEXTURE, x, y, u, v, width, height, textureWidth, textureHeight);
        RenderSystem.disableBlend();
    }
}

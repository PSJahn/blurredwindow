package de.psjahn.blurredwindow.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow @Nullable protected MinecraftClient client;
    @Shadow public int width;
    @Shadow public int height;

    @Inject(method = "renderBackgroundTexture", at = @At(value = "HEAD"), cancellable = true)
    private void removeBackgroundTexture(DrawContext context, CallbackInfo ci) {
        if(!client.getWindow().isFullscreen()&&this.client.world == null) ci.cancel();
    }
}
package de.psjahn.blurredwindow.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(AccessibilityOnboardingScreen.class)
public abstract class AccessibilityOnboardingScreenMixin {

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    private void removeBackground(CallbackInfo ci) {
        if(!MinecraftClient.getInstance().getWindow().isFullscreen()) ci.cancel();
    }
}

package de.psjahn.blurredwindow.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(FF)V"))
    private void removeBackground(RotatingCubeMapRenderer instance, float delta, float alpha) {
        if(MinecraftClient.getInstance().getWindow().isFullscreen()) instance.render(delta, alpha);
    }
}
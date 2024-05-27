package de.psjahn.blurredwindow.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import de.psjahn.blurredwindow.client.Identifiers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ContainerWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@SuppressWarnings("unused")
@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin extends ContainerWidget {
    @Shadow @Final protected MinecraftClient client;

    public EntryListWidgetMixin(int i, int j, int k, int l, Text text) {
        super(i, j, k, l, text);
    }

    @ModifyArgs(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
    private void changeBackgroundTexture(Args args) {
        if(!client.getWindow().isFullscreen()&&this.client.world == null) args.set(0, Identifiers.MENU_BACKGROUND_TEXTURE);
    }

    @Redirect(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fillGradient(Lnet/minecraft/client/render/RenderLayer;IIIIIII)V"))
    private void removeGradient(DrawContext instance, RenderLayer layer, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) { }

    @Inject(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fillGradient(Lnet/minecraft/client/render/RenderLayer;IIIIIII)V", ordinal = 1))
    private void renderHeaderAndFooterSeparators(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        RenderSystem.enableBlend();
        Identifier identifier = this.client.world == null ? Identifiers.HEADER_SEPARATOR_TEXTURE : Identifiers.INWORLD_HEADER_SEPARATOR_TEXTURE;
        Identifier identifier2 = this.client.world == null ? Identifiers.FOOTER_SEPARATOR_TEXTURE : Identifiers.INWORLD_FOOTER_SEPARATOR_TEXTURE;
        context.drawTexture(identifier, this.getX(), this.getY() - 2, 0.0f, 0.0f, this.getWidth(), 2, 32, 2);
        context.drawTexture(identifier2, this.getX(), this.getBottom(), 0.0f, 0.0f, this.getWidth(), 2, 32, 2);
        RenderSystem.disableBlend();
    }
}

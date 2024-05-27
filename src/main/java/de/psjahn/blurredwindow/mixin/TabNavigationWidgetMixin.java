package de.psjahn.blurredwindow.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import de.psjahn.blurredwindow.client.Identifiers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TabButtonWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(TabNavigationWidget.class)
public abstract class TabNavigationWidgetMixin {
    @Shadow @Final private ImmutableList<TabButtonWidget> tabButtons;
    @Shadow @Final private GridWidget grid;
    @Shadow private int tabNavWidth;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    private void removeBackground(DrawContext instance, int x1, int y1, int x2, int y2, int color) {
        if(MinecraftClient.getInstance().getWindow().isFullscreen()) instance.fill(x1, y1, x2, y2, color);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
    private void removeHeader(DrawContext context, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        if(MinecraftClient.getInstance().getWindow().isFullscreen()) {
            context.drawTexture(texture, x, y, u, v, width, height, textureWidth, textureHeight);
            return;
        }
        RenderSystem.enableBlend();
        context.drawTexture(Identifiers.HEADER_SEPARATOR_TEXTURE, 0, this.grid.getY() + this.grid.getHeight() - 2, 0.0f, 0.0f, this.tabButtons.getFirst().getX(), 2, 32, 2);
        int i = this.tabButtons.get(this.tabButtons.size() - 1).getRight();
        context.drawTexture(Identifiers.HEADER_SEPARATOR_TEXTURE, i, this.grid.getY() + this.grid.getHeight() - 2, 0.0f, 0.0f, this.tabNavWidth, 2, 32, 2);
        RenderSystem.disableBlend();
    }
}

package de.psjahn.blurredwindow.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import de.psjahn.blurredwindow.BlurredWindow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TabButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(TabButtonWidget.class)
public abstract class TabButtonWidgetMixin extends ClickableWidget {
    @Shadow public abstract boolean isCurrentTab();

    public TabButtonWidgetMixin(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Redirect(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    private void changeButtonTexture(DrawContext instance, Identifier texture, int x, int y, int width, int height) {
        if(MinecraftClient.getInstance().getWindow().isFullscreen()) {
            instance.drawGuiTexture(texture, x, y, width, height);
            return;
        }
        RenderSystem.enableBlend();
        instance.drawGuiTexture(BlurredWindow.NEW_TAB_BUTTON_TEXTURES.get(this.isCurrentTab(), this.isSelected()), x, y, width, height);
        RenderSystem.disableBlend();
    }

    @Inject(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TabButtonWidget;drawCurrentTabLine(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;I)V", shift = At.Shift.BEFORE))
    private void renderBackgroundTexture(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(!MinecraftClient.getInstance().getWindow().isFullscreen()) renderBackgroundTexture(context, this.getX() + 2, this.getY() + 2, this.getRight() - 2, this.getBottom());
    }

    @Unique
    void renderBackgroundTexture(DrawContext context, int left, int top, int right, int bottom) {
        renderBackgroundTexture(context, BlurredWindow.MENU_BACKGROUND_TEXTURE, left, top, 0.0f, 0.0f, right - left, bottom - top);
    }

    @Unique
    public void renderBackgroundTexture(DrawContext context, Identifier texture, int x, int y, float u, float v, int width, int height) {
        RenderSystem.enableBlend();
        context.drawTexture(texture, x, y, 0, u, v, width, height, 32, 32);
        RenderSystem.disableBlend();
    }
}

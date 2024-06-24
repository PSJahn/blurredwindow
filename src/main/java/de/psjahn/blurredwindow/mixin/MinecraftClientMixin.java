package de.psjahn.blurredwindow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@SuppressWarnings("unused")
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void injectRender(CallbackInfo info) {
        RenderSystem.clearColor(0, 0, 0, 0);
        MinecraftClient.getInstance().getFramebuffer().clear(MinecraftClient.IS_SYSTEM_MAC);
    }

    @Unique
    private static void framebufferDrawInternalWithAlpha(Framebuffer framebuffer, int width, int height) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._disableDepthTest();
        GlStateManager._depthMask(false);
        GlStateManager._viewport(0, 0, width, height);
        GlStateManager._disableBlend();
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ShaderProgram shaderProgram = Objects.requireNonNull(minecraftClient.gameRenderer.blitScreenProgram, "Blit shader not loaded");
        shaderProgram.addSampler("DiffuseSampler", framebuffer.getColorAttachment());
        shaderProgram.bind();
        BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.BLIT_SCREEN);
        bufferBuilder.vertex(0.0f, 0.0f, 0.0f);
        bufferBuilder.vertex(1.0f, 0.0f, 0.0f);
        bufferBuilder.vertex(1.0f, 1.0f, 0.0f);
        bufferBuilder.vertex(0.0f, 1.0f, 0.0f);
        BufferRenderer.draw(bufferBuilder.end());
        shaderProgram.unbind();
        GlStateManager._depthMask(true);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;draw(II)V"))
    private void redirectRenderFramebuffer(Framebuffer framebuffer, int width, int height, Operation<Void> original, boolean tick) {
        RenderSystem.assertOnRenderThreadOrInit();
        MinecraftClient client = MinecraftClient.getInstance();
        framebufferDrawInternalWithAlpha(framebuffer, width, height);
    }
}

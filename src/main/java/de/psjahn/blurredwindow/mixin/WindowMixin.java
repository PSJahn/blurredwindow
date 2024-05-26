package de.psjahn.blurredwindow.mixin;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import de.psjahn.blurredwindow.Dwmapi;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeWayland;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.glfw.GLFWNativeX11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(Window.class)
public abstract class WindowMixin {
    @Shadow public abstract long getHandle();

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwCreateWindow(IILjava/lang/CharSequence;JJ)J", remap = false, shift = At.Shift.BEFORE))
    private void enableTransparency(CallbackInfo info) {
        GLFW.glfwWindowHint(GLFW.GLFW_TRANSPARENT_FRAMEBUFFER, GLFW.GLFW_TRUE);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/glfw/GLFW;glfwCreateWindow(IILjava/lang/CharSequence;JJ)J", remap = false, shift = At.Shift.AFTER))
    private void enableAcrylic(CallbackInfo info) {
        if(GLFW.glfwGetPlatform() == GLFW.GLFW_PLATFORM_WIN32) {
            WinDef.HWND window = new WinDef.HWND(new Pointer(GLFWNativeWin32.glfwGetWin32Window(getHandle())));

            Dwmapi.setAcrylicBackground(window);
            Dwmapi.setUseImmersiveDarkMode(window, true);
            Dwmapi.removeBorder(window);
        }else if(GLFW.glfwGetPlatform() == GLFW.GLFW_PLATFORM_X11) {
            // Linux (Xorg) - Unless mods like WayGL are used, Minecraft
            // on Linux will use xorg even on Wayland through xwayland.

            long window = GLFWNativeX11.glfwGetX11Window(getHandle());
            try {
                // TODO: Use natively instead of via command
                Runtime.getRuntime().exec(new String[]{
                        "xprop",
                        "-id", "" + window,
                        "-f", "_KDE_NET_WM_BLUR_BEHIND_REGION",
                        "32c",
                        "-set", "_KDE_NET_WM_BLUR_BEHIND_REGION", "1",
                });
            }catch (Exception ex) {
                LoggerFactory.getLogger("blurredwindow").error("Failed to run xprop to enable blur (might be KDE specific)", ex);
            }
        }
    }
}
package de.psjahn.blurredwindow.mixin;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import de.psjahn.blurredwindow.Dwmapi;
import de.psjahn.blurredwindow.XLib;
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

import java.nio.ByteBuffer;

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
            // This is the equivalent of this command, using XLib:
            // $ xprop -id <window> -f _KDE_NET_WM_BLUR_BEHIND_REGION 32c -set _KDE_NET_WM_BLUR_BEHIND_REGION 1

            long display = GLFWNativeX11.glfwGetX11Display();
            long window = GLFWNativeX11.glfwGetX11Window(getHandle());
            long property = XLib.INSTANCE.XInternAtom(display, "_KDE_NET_WM_BLUR_BEHIND_REGION", false);
            byte[] data = new byte[]{ 0x00, 0x00, 0x00, 0x01 }; // Basically 1, but in 32 bit and passed as pointer to buffer "data"
            long res = XLib.INSTANCE.XChangeProperty(display, window, property, XLib.XA_CARDINAL_ATOM, 32, 0, ByteBuffer.wrap(data), 1);
        }
    }
}
package de.psjahn.blurredwindow;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;

public interface Dwmapi extends Library {
    Dwmapi INSTANCE = Native.load("dwmapi", Dwmapi.class);

    int DWMWA_USE_IMMERSIVE_DARK_MODE = 20;
    int DWMWA_SYSTEMBACKDROP_TYPE = 38;
    int DWMWA_BORDER_COLOR = 34;

    @SuppressWarnings("UnusedReturnValue")
    int DwmSetWindowAttribute(HWND hwnd, WinDef.UINT dwAttribute, PointerType pvAttribute, int cbAttribute);

    static void setUseImmersiveDarkMode(HWND hwnd, boolean enabled) {
        INSTANCE.DwmSetWindowAttribute(hwnd, new WinDef.UINT(DWMWA_USE_IMMERSIVE_DARK_MODE), new WinDef.BOOLByReference(new WinDef.BOOL(enabled)), 4);
    }

    static void setAcrylicBackground(HWND hwnd) {
        INSTANCE.DwmSetWindowAttribute(hwnd, new WinDef.UINT(DWMWA_SYSTEMBACKDROP_TYPE), new IntByReference(3), 4);
    }

    static void removeBorder(HWND hwnd) {
        setBorderColor(hwnd, 4294967294L);
    }

    static void setBorderColor(HWND hwnd, long color) {
        INSTANCE.DwmSetWindowAttribute(hwnd, new WinDef.UINT(DWMWA_BORDER_COLOR), new WinDef.UINTByReference(new WinDef.UINT(color)), 4);
    }
}
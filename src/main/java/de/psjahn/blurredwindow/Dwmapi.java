package de.psjahn.blurredwindow;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;

public interface Dwmapi extends Library {
    Dwmapi INSTANCE = Native.loadLibrary("dwmapi", Dwmapi.class);

    int DwmSetWindowAttribute(HWND hwnd, WinDef.UINT dwAttribute, PointerType pvAttribute, int cbAttribute);

    static void setUseImmersiveDarkMode(HWND hwnd, boolean enabled) {
        System.out.println(INSTANCE.DwmSetWindowAttribute(hwnd, new WinDef.UINT(20), new WinDef.BOOLByReference(new WinDef.BOOL(enabled)), 4));
    }

    static void setAcrylicBackground(HWND hwnd) {
        INSTANCE.DwmSetWindowAttribute(hwnd, new WinDef.UINT(38), new IntByReference(3), 4);
    }

    static void removeBorder(HWND hwnd) {
        setBorderColor(hwnd, 4294967294L);
    }

    static void setBorderColor(HWND hwnd, long color) {
        INSTANCE.DwmSetWindowAttribute(hwnd, new WinDef.UINT(34), new WinDef.UINTByReference(new WinDef.UINT(color)), 4);
    }
}
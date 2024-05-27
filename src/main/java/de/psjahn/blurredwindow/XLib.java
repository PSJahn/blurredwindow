package de.psjahn.blurredwindow;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.nio.ByteBuffer;

public interface XLib extends Library {
    XLib INSTANCE = Native.load("libX11", XLib.class);

    long XA_CARDINAL_ATOM = 6;

    long XInternAtom(long displayPtr, String atomName, boolean onlyIfExists);

    @SuppressWarnings("UnusedReturnValue")
    int XChangeProperty(long displayPtr, long window, long/*Atom*/ property, long/*Atom*/ type, int format, int mode, ByteBuffer data, int nElements);

    static void setBlurBehind(long display, long window, boolean enabled) {
        long property = XLib.INSTANCE.XInternAtom(display, "_KDE_NET_WM_BLUR_BEHIND_REGION", false);
        byte[] data = enabled ? new byte[]{ 0x00, 0x00, 0x00, 0x01 } : new byte[]{ 0x00, 0x00, 0x00, 0x00 }; // A boolean but in 32 bit and passed as pointer to buffer "data"
        XLib.INSTANCE.XChangeProperty(display, window, property, XLib.XA_CARDINAL_ATOM, 32, 0, ByteBuffer.wrap(data), 1);
    }
}
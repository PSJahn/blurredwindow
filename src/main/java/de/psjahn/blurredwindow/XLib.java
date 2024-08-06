package de.psjahn.blurredwindow;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.nio.ByteBuffer;

public interface XLib extends Library {
    XLib INSTANCE = Native.load("X11", XLib.class);

    long XA_CARDINAL_ATOM = 6;

    long XInternAtom(long displayPtr, String atomName, boolean onlyIfExists);

    @SuppressWarnings("UnusedReturnValue")
    int XChangeProperty(long displayPtr, long window, long/*Atom*/ property, long/*Atom*/ type, int format, int mode, ByteBuffer data, int nElements);

    @SuppressWarnings("UnusedReturnValue")
    int XDeleteProperty(long displayPtr, long window, long/*Atom*/ property);

    static void setBlurBehind(long display, long window, boolean enabled) {
        long propertyAtom = XLib.INSTANCE.XInternAtom(display, "_KDE_NET_WM_BLUR_BEHIND_REGION", false);

        if(enabled) {
            // 32 bit value and passed as pointer to buffer "data". Value doesn't seem to matter, but let's just use 0x01
            byte[] data = new byte[]{0x00, 0x00, 0x00, 0x01};
            XLib.INSTANCE.XChangeProperty(display, window, propertyAtom, XLib.XA_CARDINAL_ATOM, 32, 0, ByteBuffer.wrap(data), 1);
        }else {
            XLib.INSTANCE.XDeleteProperty(display, window, propertyAtom);
        }
    }
}
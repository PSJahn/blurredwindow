package de.psjahn.blurredwindow;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.nio.ByteBuffer;

public interface XLib extends Library {

    long XA_CARDINAL_ATOM = 6;

    XLib INSTANCE = Native.loadLibrary("libX11", XLib.class);

    long XInternAtom(long displayPtr, String atomName, boolean onlyIfExists);

    int XChangeProperty(long displayPtr, long window, long/*Atom*/ property, long/*Atom*/ type, int format, int mode, ByteBuffer data, int nElements);

}
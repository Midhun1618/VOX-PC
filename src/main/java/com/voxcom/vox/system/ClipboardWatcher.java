package com.voxcom.vox.system;

import com.voxcom.vox.sync.ClipboardSyncService;

import java.awt.*;
import java.awt.datatransfer.*;

public class ClipboardWatcher implements ClipboardOwner {

    private Clipboard clipboard;

    public ClipboardWatcher() {
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        regainOwnership();
    }

    private void regainOwnership() {
        Transferable t = clipboard.getContents(this);
        clipboard.setContents(t, this);
    }

    @Override
    public void lostOwnership(Clipboard c, Transferable t) {

        try { Thread.sleep(150); } catch (Exception ignored) {}

        try {
            Transferable contents = clipboard.getContents(this);

            if(contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String) contents.getTransferData(DataFlavor.stringFlavor);
                ClipboardSyncService.sendFromPC(text);
            }

        } catch (Exception ignored) {}

        regainOwnership();
    }
}

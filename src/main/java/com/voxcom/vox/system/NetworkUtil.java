package com.voxcom.vox.system;

import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkUtil {

    public static boolean hasInternet() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 1200);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
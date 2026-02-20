package com.voxcom.vox.net;

import java.io.PrintWriter;
import java.net.Socket;

public class VoxClient {

    public static void send(String command) {

        try (Socket socket = new Socket("localhost", 5050)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("VOX:" + command);
        } catch (Exception e) {
            System.out.println("VOX background not running");
        }
    }
}
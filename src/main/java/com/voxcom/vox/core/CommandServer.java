package com.voxcom.vox.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class CommandServer {

    public static void start() {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(5050)) {

                System.out.println("VOX command server running on 5050");

                while (true) {
                    Socket client = server.accept();

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(client.getInputStream()));

                    String line = in.readLine();

                    if (line != null && line.startsWith("VOX:")) {
                        String command = line.substring(4).trim();
                        System.out.println("VOX Command: " + command);
                        CommandExecutor.execute(command);
                    }

                    client.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
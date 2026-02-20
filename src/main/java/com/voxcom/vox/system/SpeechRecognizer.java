package com.voxcom.vox.system;

import java.util.Scanner;

public class SpeechRecognizer {

    public static String listen() {

        System.out.println("Listening... type command (mock mic): ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
}
package com.voxcom.vox.ui.layouts;

import com.voxcom.vox.ui.theme.*;

import javax.swing.*;
import java.awt.*;

public class TopTabs extends PixelPanel {

    public TopTabs(Runnable tasks, Runnable history, Runnable reminders) {

        setLayout(new FlowLayout(FlowLayout.LEFT));

        PixelButton t1 = new PixelButton("TASKS");
        PixelButton t2 = new PixelButton("HISTORY");
        PixelButton t3 = new PixelButton("REMINDERS");

        t1.addActionListener(e -> tasks.run());
        t2.addActionListener(e -> history.run());
        t3.addActionListener(e -> reminders.run());

        add(t1);
        add(t2);
        add(t3);
    }
}

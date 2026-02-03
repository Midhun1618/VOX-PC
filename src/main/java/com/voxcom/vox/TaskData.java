package com.voxcom.vox;

import com.google.cloud.Timestamp;

public class TaskData {

    public String title;
    public boolean completed;
    public Timestamp createdAt;
    public Timestamp expiresAt;

    // 🔥 REQUIRED by Firestore
    public TaskData() {
    }

    // Optional helper constructor
    public TaskData(String title) {
        this.title = title;
    }
}

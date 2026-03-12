# VOX Desktop (vox-pc)

VOX Desktop is the **Windows companion application** of the VOX Cross-Platform Productivity Ecosystem.  
It connects with **VOX Android** to create a synchronized productivity environment where users can monitor tasks, execute voice commands, and track productivity metrics in real time.

The desktop application acts as a **central productivity dashboard**, integrating voice interaction, cross-device synchronization, and system utilities to enable a seamless workflow.

VOX Desktop emphasizes **measurable productivity, hands-free interaction, and real-time synchronization**.

---

# Overview

VOX Desktop extends productivity from mobile devices to desktop systems.

While **VOX Android** focuses on task creation and mobile interaction, the desktop application provides:

- A centralized productivity dashboard
- Voice command execution
- Clipboard synchronization
- Task monitoring and reminders
- Focus Index productivity analytics

The system integrates **wake word detection, offline speech recognition, and command pipelines**, allowing users to interact with the system hands-free.

---

# Core Features

## Cross Device Pairing

VOX Desktop securely connects with VOX Android using a **pairing code system**.

Features include:

- Secure device authentication
- Session management
- Persistent device linking
- Real time communication

---

## Real Time Task Synchronization

Tasks created on VOX Android are synchronized with the desktop dashboard.

Capabilities include:

- Live task updates
- Task state synchronization
- Activity monitoring
- Cross device consistency

---

## Wake Word Detection

VOX Desktop supports hands-free activation through a wake word detection service.

Example interaction:

```
User: "VOX"
System: Wake word detected
```

Once activated, the system begins listening for commands.

---

## Speech Recognition (Offline)

Voice commands are processed using **Vosk**, an offline speech recognition engine.

Advantages:

- Fully offline processing
- Low latency
- Lightweight models
- Real time transcription

Voice pipeline:

1. Wake word detection
2. Microphone audio capture
3. Speech recognition (Vosk)
4. Command parsing
5. Command execution

Example command:

```
User: "Show my tasks"
System: Opening tasks dashboard
```

---

## Voice Reactive Desktop Widget

VOX Desktop includes a floating widget that visually responds to voice activity.

Widget states include:

- Idle
- Wake detected
- Listening
- Processing
- Command executed

This provides **visual feedback during voice interactions**.

---

## Focus Index Productivity Metric

VOX measures productivity using a metric called **Focus Index**.

Task lifecycle:

- Tasks expire after 24 hours
- Completed tasks → Done
- Incomplete tasks → Missed

Productivity score:

```
Focus Index = Done / (Done + Missed)
```

This allows productivity to be **quantified rather than simply recorded**.

---

## Cloud Clipboard Synchronization

VOX Desktop synchronizes clipboard content across devices.

Capabilities:

- Copy text on desktop → paste on mobile
- Copy text on mobile → paste on desktop

This improves cross device workflow efficiency.

---

# System Architecture

VOX Desktop follows a **modular layered architecture** separating system services, UI components, networking, and voice processing.

Main layers include:

### UI Layer
Handles dashboard screens, widgets, and visual components.

### Voice Processing Layer
Handles wake word detection, audio capture, and speech recognition.

### Command Layer
Processes recognized speech and executes commands.

### Sync Layer
Handles cross device synchronization and cloud communication.

### System Services Layer
Handles microphone input, audio playback, clipboard monitoring, and network utilities.

---

# Technology Stack

### Language

Java

### Desktop UI

Java Swing

### Voice Processing

Wake word detection engine  
Offline speech recognition using **Vosk**

### Synchronization

Firebase Realtime Database

### Architecture

Modular layered architecture

---

# Project Structure

```
vox-pc
│
├── src
│   └── main
│       └── java
│           └── com
│               └── voxcom
│                   └── vox
│
│                       ├── config
│                       │   └── VoxSettings.java
│                       │       Application configuration and system constants
│
│                       ├── core
│                       │   ├── CommandExecutor.java
│                       │   │   Executes parsed voice commands
│                       │   │
│                       │   ├── CommandServer.java
│                       │   │   Handles command communication pipeline
│                       │   │
│                       │   └── VoxBackground.java
│                       │       Manages background services
│
│                       ├── net
│                       │   └── VoxClient.java
│                       │       Handles networking with VOX Android
│
│                       ├── sync
│                       │   └── ClipboardSyncService.java
│                       │       Synchronizes clipboard across devices
│
│                       ├── system
│                       │   ├── AudioRecorder.java
│                       │   ├── MicRecorder.java
│                       │   ├── WakewordService.java
│                       │   ├── WhisperRecognizer.java
│                       │   ├── GoogleSpeechRecognizer.java
│                       │   ├── ClipboardWatcher.java
│                       │   ├── NetworkUtil.java
│                       │   └── SoundPlayer.java
│                       │
│                       │   Handles microphone input, wake word detection,
│                       │   speech recognition, clipboard monitoring, and system utilities
│
│                       ├── ui
│                       │
│                       │   ├── frame
│                       │   │   ├── DashboardFrame.java
│                       │   │   └── LoginFrame.java
│                       │   │
│                       │   ├── layouts
│                       │   │   ├── LeftSidebar.java
│                       │   │   ├── ProfileSidebar.java
│                       │   │   └── TopTabs.java
│                       │   │
│                       │   ├── reminders
│                       │   │   └── ReminderList.java
│                       │   │
│                       │   ├── screens
│                       │   │   ├── HomeScreen.java
│                       │   │   ├── TasksScreen.java
│                       │   │   ├── HistoryScreen.java
│                       │   │   ├── RemindersScreen.java
│                       │   │   └── SettingsScreen.java
│                       │   │
│                       │   ├── tasks
│                       │   │   ├── TaskListPanel.java
│                       │   │   ├── TaskRow.java
│                       │   │   └── TaskTabs.java
│                       │   │
│                       │   ├── theme
│                       │   │   ├── VoxTheme.java
│                       │   │   ├── PixelPanel.java
│                       │   │   ├── PixelButton.java
│                       │   │   ├── PixelTextField.java
│                       │   │   └── FontUtil.java
│                       │   │
│                       │   └── widget
│                       │       └── VoxWidget.java
│                       │           Floating voice interaction widget
│
│                       └── util
│                           Utility helper classes
│
├── resources
│   ├── avatars
│   ├── font
│   ├── icons
│   ├── loader
│   ├── sounds
│   ├── wakeword
│   ├── widget_states
│   └── serviceAccountKey.json
│
└── README.md
```

---

# Installation

Clone the repository

```
git clone https://github.com/your-username/vox-pc.git
```

Navigate to the project

```
cd vox-pc
```

Compile and run

```
javac App.java
java App
```

---

# VOX Ecosystem

VOX consists of multiple components working together.

| Component | Platform | Purpose |
|--------|--------|--------|
| VOX Android | Kotlin | Task creation and mobile interaction |
| VOX Desktop | Java | Dashboard and voice control |
| Cloud Sync | Firebase | Data synchronization |

---

# Future Improvements

Planned improvements include:

- Natural language command parsing
- Multi device synchronization
- AI based productivity insights
- Calendar integrations
- Cross platform desktop builds
- Smart notifications

---

# Author

**Midhun**

B.Tech Computer Science and Engineering  
Specialization: Machine Learning

---

# License

MIT License
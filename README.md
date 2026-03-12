# VOX Desktop (vox-pc)

VOX Desktop is the desktop companion application of the **VOX Cross-Platform Productivity Ecosystem**, designed to synchronize productivity workflows between Android and Windows.

The desktop client acts as a **central productivity dashboard**, allowing users to monitor tasks, track productivity metrics, and interact with the system using voice commands through **wake word activation and speech recognition**.

VOX Desktop focuses on **measurable productivity**, real-time synchronization, and seamless cross-device interaction.

---

# Overview

VOX Desktop extends the productivity environment from mobile to desktop.

While **VOX Android** handles task creation and mobile interactions, the desktop application provides a **larger control surface for monitoring tasks and executing voice commands**.

The system integrates:

- Wake word detection
- Speech recognition
- Command execution pipelines
- Real-time synchronization
- Productivity analytics

This allows users to control tasks and interact with the system **hands-free**, improving workflow efficiency.

---

# Architecture

VOX Desktop follows a **modular architecture** separating voice processing, synchronization, and UI components.

### UI Layer
- Desktop dashboard
- Floating voice widget
- Task visualization panels

### Voice Interaction Layer
- Wake word detection engine
- Speech recognition engine
- Command parsing system

### Productivity Engine
- Focus Index calculation
- Task state management
- Activity tracking

### Sync Layer
- Android pairing service
- Real-time task synchronization
- Cloud clipboard service

### Data Layer
- Task storage
- Session state management
- Productivity analytics

---

# Key Features

## Cross-Device Pairing

VOX Desktop securely connects with VOX Android using a **unique pairing code system**.

Features include:

- Secure authentication handshake
- Device linking
- Session validation

Once paired, both devices remain synchronized.

---

## Real-Time Task Synchronization

Tasks created on mobile are automatically synchronized to the desktop dashboard.

Capabilities include:

- Live task updates
- Status tracking
- Cross-device consistency
- Activity monitoring

---

## Wake Word Detection

VOX Desktop supports **hands-free system activation** using a wake word detection engine.

Example interaction:

```
User: "VOX"
System: Wake word detected
```

This activates the speech pipeline without requiring manual input.

---

## Speech Recognition Pipeline

Once activated, the system enters listening mode and processes commands using a speech recognition model.

Pipeline stages:

1. Wake word detection  
2. Voice capture  
3. Speech-to-text conversion  
4. Command parsing  
5. Command execution  

Example commands:

- "Show my tasks"
- "Mark task complete"
- "Open dashboard"

---

## Voice-Reactive Desktop Widget

VOX Desktop includes a floating widget that provides **visual feedback for voice interactions**.

Widget states include:

- Wake detected
- Listening
- Processing
- Command executed

This improves user awareness during voice interactions.

---

## Focus Index Dashboard

VOX measures productivity using a metric called **Focus Index**.

Tasks have a **24-hour life cycle**.

Task states:

Completed → Done  
Incomplete → Missed  

The productivity score is calculated as:

```
Focus Index = Done / (Done + Missed)
```

This provides a **quantifiable measure of productivity**.

---

## Cloud Clipboard Synchronization

VOX Desktop allows users to share clipboard content between devices.

Supported actions:

- Copy on desktop → paste on Android
- Copy on Android → paste on desktop

This removes friction when working across devices.

---

# System Workflow

Typical VOX workflow:

1. User creates tasks on VOX Android
2. Tasks synchronize with VOX Desktop
3. Desktop dashboard visualizes tasks and productivity metrics
4. User interacts using voice commands
5. VOX processes commands and updates task states
6. Focus Index updates dynamically

---

# Technology Stack

**Language**

- Java

**Desktop UI**

- Java Swing / JavaFX

**Voice Processing**

- Wake word detection engine
- Speech recognition engine

**Speech Recognition Model**

- Whisper

**Synchronization**

- Cloud-based task synchronization

**Architecture**

- Modular layered architecture

---

# Project Structure

```
vox-pc
│
├── src
│   ├── ui
│   │   ├── dashboard
│   │   ├── widget
│   │   └── components
│   │
│   ├── voice
│   │   ├── wakeword
│   │   ├── recognition
│   │   └── command
│   │
│   ├── sync
│   │   ├── pairing
│   │   └── tasksync
│   │
│   ├── productivity
│   │   ├── focusindex
│   │   └── taskmanager
│   │
│   └── utils
│
└── README.md
```

---

# Installation

### Clone the repository

```bash
git clone https://github.com/your-username/vox-pc.git
```

### Navigate to the project

```bash
cd vox-pc
```

### Compile and run

```bash
javac Main.java
java Main
```

---

# VOX Ecosystem

VOX consists of multiple components working together.

| Component | Platform | Purpose |
|--------|--------|--------|
| VOX Android | Kotlin | Task creation and mobile interaction |
| VOX Desktop | Java | Dashboard and voice control |
| Cloud Sync | Backend | Data synchronization |

---

# Future Improvements

Planned improvements include:

- Advanced natural language command parsing
- Multi-device synchronization
- AI productivity insights
- Calendar integrations
- Cross-platform desktop builds
- Smart notification system

---

# Use Cases

VOX Desktop can be useful for:

- Students managing study tasks
- Developers tracking daily goals
- Professionals monitoring productivity
- Users who prefer voice-driven productivity tools

---

# Contributing

Contributions are welcome.

Steps:

1. Fork the repository  
2. Create a new branch  
3. Commit changes  
4. Submit a pull request  

---

# License

This project is released under the MIT License.

---

# Author

**Midhun**  
B.Tech Computer Science and Engineering (Machine Learning)

---

# Links

GitHub Repository :https://github.com/Midhun1618/vox-pc

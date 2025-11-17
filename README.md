# SoundPlayer

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-007396.svg?logo=java)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-0078D7.svg?logo=javafx)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36.svg?logo=apache-maven)](https://maven.apache.org/)
[![Release](https://img.shields.io/badge/Latest_Release-V2.0-blue.svg)](https://github.com/AhmedQw1/SoundPlayer/releases/latest)
[![Download](https://img.shields.io/badge/Download-EXE-brightgreen.svg)](https://github.com/AhmedQw1/SoundPlayer/releases/latest)

## Overview
A multimedia player built with JavaFX 21 that supports both audio and video playback! This application combines elegant Material Design with good media functionality to deliver a nice user experience.

![SoundPlayer Screenshot](screenshots/main-screen.png)

---

## Installation

Download [SoundPlayerV2-2.0.exe](https://github.com/AhmedQw1/SoundPlayer/releases/tag/v2.0.0) from releases.

**Requirements:** Windows 10+, 4GB RAM, ~35MB disk space. No Java required—runtime bundled.

**Install:** Run the `.exe`, follow the wizard, choose directory. Launch from desktop or Start Menu.

**Uninstall:** Settings > Apps > SoundPlayerV2 > Uninstall

---

## Key Features

### Dual Media Support
- **Audio:** MP3, WAV and other common formats
- **Video:** MP4, AVI, MOV, MKV, WMV, FLV, WebM
- Smart format detection with adaptive UI

### System Design
- Dual themes (dark/light) with Material Design
- FontAwesome icons and smooth animations
- Responsive layouts for all window sizes

### Playback Controls
- Variable speed: 0.5x, 1x, 1.5x, 2x
- Enhanced seeking with custom tooltips
- Volume control and intelligent looping

### Playlist Management
- Auto-save and restore playlists
- Visual indicators (videos,  audio)
- Quick delete and auto-selection

### Audio Visualization
- Animated spinning vinyl disk
- Speed-synced rotation
- Theme-adaptive glowing effects

### User Experience
- Theme persistence and file size display
- Comprehensive error handling
- Full keyboard navigation support
- MVC architecture with modular CSS

---

## Building From Source

### Prerequisites
- Java JDK 21+
- JavaFX 21 (included)
- Maven 3.8+

### Dependencies
- JavaFX 21 (Controls, FXML, Media)
- JFoenix 9.0.10 (Material Design)
- Ikonli 12.3.1 (FontAwesome Icons)
- CSSFX 11.5.0 (Live CSS Reload)

### Build & Run
```bash
git clone https://github.com/AhmedQw1/SoundPlayer.git
cd SoundPlayer
mvn clean package
mvn javafx:run
```

---

## Usage

### Quick Start
1. Launch application
2. Click "Open Media" to add files
3. Control playback with bottom controls
4. Toggle themes (🌙/☀) in top-right
5. Adjust speed via dropdown
6. Navigate with Previous/Next or click playlist items

---

## Project Structure
```
SoundPlayerV2/
├── src/main/java/
│   ├── com/example/soundplayerv1/
│   │   └── SoundPlayerApplication.java
│   ├── Controller/
│   │   ├── MediaController.java
│   │   ├── PlaylistController.java
│   │   ├── SoundPlayerController.java
│   │   ├── ThemeController.java
│   │   └── UIController.java
│   ├── model/
│   │   └── SoundFile.java
│   └── util/
│       └── TimeFormatter.java
├── src/main/resources/
│   └── com/example/soundplayerv1/
│       ├── css/
│       │   ├── dark/
│       │   └── light/
│       ├── disk.png
│       └── SoundPlayer-view.fxml
└── pom.xml
```

---

## License
MIT Licensed

---

## Acknowledgments
- JavaFX Team for the excellent framework
- JFoenix for Material Design components
- Ikonli for the icon system
- FontAwesome for comprehensive icon library

---

## Contact
**Developer:** [@AhmedQw1](https://github.com/AhmedQw1) 
**Instagram:** [@_klqc](https://instagram.com/_klqc)

---

Built using JavaFX 21 and modern Java development practices

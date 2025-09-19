# 🎵 Sound Player V2

[![GitHub last commit](https://img.shields.io/github/last-commit/AhmedQw1/SoundPlayer)](https://github.com/AhmedQw1/SoundPlayer/commits)
[![GitHub top language](https://img.shields.io/github/languages/top/AhmedQw1/SoundPlayer)](https://github.com/AhmedQw1/SoundPlayer)

## Overview
A modern multimedia player built with JavaFX 21 that supports both audio and video playback! 🎬🎵 This professional-grade application combines elegant Material Design with powerful media functionality to deliver an exceptional user experience.

![SoundPlayer Screenshot](screenshots/main-screen.png)

---

## 📥 Installation

- Download the installer from the Releases page:
  - Latest V2: [SoundPlayer V2.0](https://github.com/AhmedQw1/SoundPlayer/releases/tag/v2.0.0)
  - Previous V1: [SoundPlayer V1.0](https://github.com/AhmedQw1/SoundPlayer/releases/tag/v1.0.0)
- File to download for V2: `SoundPlayerV2-2.0.exe`

### Install Steps
1. Run `SoundPlayerV2-2.0.exe`
2. Follow the installer wizard
3. Choose your installation directory (you can customize it)
4. Finish and launch from:
   - Desktop shortcut (if created)
   - Start Menu: Start > SoundPlayerV2

### Default Install Locations
- Per-user (default, no admin required):
  - C:\Users\<YourUser>\AppData\Local\Programs\SoundPlayerV2
- All users (when run as admin or selected in wizard):
  - C:\Program Files\SoundPlayerV2

Note: The installer lets you pick a custom folder thanks to the installation wizard.

### Uninstall
- Windows Settings > Apps > Installed apps > SoundPlayerV2 > Uninstall
- Or via Control Panel > Programs and Features

### Requirements
- Windows 10 or higher
- 4GB RAM recommended
- ~35MB free disk space
- No Java required — a runtime is bundled with the installer

### Troubleshooting
- Windows SmartScreen: Click “More info” > “Run anyway” (unsigned app warning)
- Video playback: Uses Windows Media Foundation. Most modern formats work out of the box on Windows 10+. If a specific codec is missing on your system, install the relevant video codec pack from Microsoft/Windows features.

---

## ✨ Key Features

### 🎬 Dual Media Support
- Audio Playback: MP3, WAV, and other common audio formats
- Video Playback: MP4, AVI, MOV, MKV, WMV, FLV, WebM
- Smart Detection: Automatically switches between audio and video modes
- Adaptive UI: Dynamic container sizing for optimal viewing experience

### 🎨 Modern Design System
- Dual Theme Support: Professional dark and light themes
- Material Design: JFoenix components with smooth animations
- FontAwesome Icons: Modern vector icons throughout the interface
- Responsive Layout: Adapts to different window sizes and content types
- Visual Feedback: Hover effects, transitions, and loading animations

### 🎛️ Advanced Playback Controls
- Variable Speed: 0.5x, 1x, 1.5x, 2x playback speeds
- Enhanced Seeking: Lag-free progress slider with custom tooltips
- Volume Control: Smooth volume adjustment with visual feedback
- Navigation: Previous/Next with intelligent looping
- Reset Function: Quick restart to beginning

### 📋 Smart Playlist Management
- Persistent Playlists: Automatically saves and restores your playlist
- Visual Indicators: 📹 for videos, 🎵 for audio files
- Quick Actions: Delete items with animated buttons
- Auto-Selection: Currently playing track highlighted
- Duplicate Prevention: Smart file management

### 🎭 Audio Visualization
- Animated Disk: Spinning vinyl disk during audio playback (disk.png)
- Speed-Synced Animation: Disk rotation matches playback speed
- Theme-Adaptive Effects: Glowing effects that match your theme

### ⚙️ Enhanced User Experience
- Theme Persistence: Remembers your preferred theme
- File Size Display: Shows formatted file sizes
- Error Handling: Comprehensive error management with user feedback
- Keyboard Support: Full keyboard navigation support
- Custom Tooltips: Professional tooltip system

---

## 🖼️ Screenshots
- Dark Theme — Professional dark theme with blue accents and glowing effects
- Light Theme — Clean light theme with modern Material Design elements

---

## 🛠️ Building From Source

### Prerequisites
- Java JDK 21 or higher
- JavaFX 21 (included in dependencies)
- Maven 3.8+ for building

### Modern Dependencies
- JavaFX 21 (Controls, FXML, Media)
- JFoenix 9.0.10 (Material Design)
- Ikonli 12.3.1 (FontAwesome Icons)
- CSSFX 11.5.0 (Live CSS Reload)

### Build Instructions
```bash
# Clone the repository
git clone https://github.com/AhmedQw1/SoundPlayer.git
cd SoundPlayer

# Build the project
mvn clean package

# Run the application (development)
mvn javafx:run
```

### Development Features
- Live CSS Reload: CSSFX enables real-time CSS updates
- Module Support: Full JPMS compliance
- Modern Architecture: Clean MVC-like controller separation

---

## 🚀 Usage

### Quick Start Guide
1. Launch the application
2. Click “Open Media” to add audio or video files
3. Control playback using the enhanced controls at the bottom
4. Switch Themes using the toggle (🌙/☀) in the top-right
5. Adjust Speed via the playback rate dropdown
6. Navigate with Previous/Next buttons or click playlist items

### Advanced Features
- Theme Switching: Toggle between dark and light themes instantly
- Speed Control: Adjust playback speed from 0.5x to 2x
- Custom Tooltips: Hover over sliders for precise value information
- Playlist Management: Right-click functionality for advanced operations
- File Management: Persistent playlist that remembers your media

---

## 🏗️ Project Structure
```
SoundPlayerV2/
├── src/main/java/
│   ├── com/example/soundplayerv1/
│   │   └── SoundPlayerApplication.java    # Main application entry point
│   ├── Controller/
│   │   ├── MediaController.java           # Media playback logic
│   │   ├── MediaInfoController.java       # Media file information
│   │   ├── PlaylistController.java        # Playlist management
│   │   ├── SoundPlayerController.java     # Main UI controller
│   │   ├── ThemeController.java           # Theme switching
│   │   └── UIController.java              # UI component management
│   ├── model/
│   │   └── SoundFile.java                 # File model
│   ├── util/
│   │   └── TimeFormatter.java             # Time formatting utilities
│   └── module-info.java                   # JPMS module configuration
├── src/main/resources/
│   ├── com/example/soundplayerv1/
│   │   ├── css/
│   │   │   ├── dark/                      # Dark theme styles
│   │   │   │   ├── dark-theme.css
│   │   │   │   ├── dark-buttons.css
│   │   │   │   ├── dark-lists.css
│   │   │   │   ├── dark-sliders.css
│   │   │   │   └── video-style.css
│   │   │   ├── light/                     # Light theme styles
│   │   │   │   ├── light-theme.css
│   │   │   │   ├── light-buttons.css
│   │   │   │   ├── light-lists.css
│   │   │   │   ├── light-sliders.css
│   │   │   │   └── video-style.css
│   │   │   ├── animations.css             # Shared animations
│   │   │   ├── base.css                   # Base styles
│   │   │   ├── enhanced-buttons.css       # Enhanced button styling
│   │   │   ├── enhanced-progress.css      # Advanced slider styling
│   │   │   └── video-style.css            # Video component styling
│   │   ├── disk.png                       # Spinning disk image
│   │   └── SoundPlayer-view.fxml          # Main UI layout
│   └── META-INF/
│       └── MANIFEST.MF                    # JAR manifest
└── pom.xml                                # Maven configuration
```

---

## 🎯 Architecture

### Controller Architecture
- SoundPlayerController: Main UI controller and coordinator
- MediaController: Handles all media playback logic
- PlaylistController: Manages playlist operations and navigation
- ThemeController: Controls theme switching and persistence
- UIController: Manages UI components and interactions
- MediaInfoController: Handles media file information display

### CSS Theme System
The application features a sophisticated dual-theme system with modular CSS:

Dark Theme Structure:
```
css/dark/
├── dark-theme.css
├── dark-buttons.css
├── dark-lists.css
├── dark-sliders.css
└── video-style.css
```

Light Theme Structure:
```
css/light/
├── light-theme.css
├── light-buttons.css
├── light-lists.css
├── light-sliders.css
└── video-style.css
```

Shared Components:
```
css/
├── base.css
├── animations.css
├── enhanced-buttons.css
├── enhanced-progress.css
└── video-style.css
```

---

## 🎯 Roadmap
- Windows Installer: Create a packaged installer for easy distribution
- Equalizer: Audio frequency adjustment
- Visualizations: Additional audio visualization modes
- Playlist Export: Save playlists to files
- Subtitles: Video subtitle support
- Streaming: Online media streaming capabilities
- Plugins: Extensible plugin architecture
- Cross-Platform: macOS and Linux support

---

## 🤝 Contributing
Contributions are welcome! Here's how you can help:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

Development Guidelines:
- Follow JavaFX best practices
- Maintain the existing architecture patterns
- Add appropriate CSS styling for both themes
- Include proper error handling
- Test on Windows and other platforms when possible

---

## 📄 License
MIT Licensed 
---

## 🙏 Acknowledgments
- JavaFX Team for the excellent framework
- JFoenix for Material Design components
- Ikonli for the beautiful icon system
- FontAwesome for the comprehensive icon library
- CSSFX for development-time CSS reloading

---

## 📞 Contact
Developer: [@AhmedQw1](https://github.com/AhmedQw1)  
Instagram: [@_klqc](https://instagram.com/_klqc)

### 🔗 Other Projects
- [IBM Capstone Project](https://github.com/AhmedQw1/java-database-capstone.git)
- [University Chat App](https://github.com/AhmedQw1/uni-chat-app.git)
- [CV Web Portfolio](https://github.com/AhmedQw1/CV-Web-Portfolio.git)

Last updated: 2025-09-11 by AhmedQw1

Built with ❤️ using JavaFX 21 and modern Java development practices

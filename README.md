#  Sound Player

![GitHub last commit](https://img.shields.io/github/last-commit/AhmedQw1/SoundPlayer)
![GitHub top language](https://img.shields.io/github/languages/top/AhmedQw1/SoundPlayer)
![License](https://img.shields.io/github/license/AhmedQw1/SoundPlayer)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/AhmedQw1/SoundPlayer)

## Overview

A modern audio player built with JavaFX :) It combines elegant design with powerful playback features. This application provides a complete solution for managing and enjoying your music collection with an intuitive interface.

![SoundPlayer Screenshot](screenshots/main-screen.png)

## Features

- **Sleek Modern UI**: Clean interface with intuitive controls and visual feedback
- **Comprehensive Playlist Management**: Create, save, and load playlists
- **Advanced Playback Controls**: Play, pause, skip, and adjust volume with ease
- **Audio Visualization**: Visual representation of audio playback
- **Format Support**: Compatible with MP3, WAV, and other common audio formats
- **Drag & Drop Support**: Easily add songs to your playlist
- **Customizable Theme**: Modern dark theme that's easy on the eyes
- **Persistent Settings**: Your preferences and last played song are remembered

### Dark Theme
![Dark Theme](screenshots/dark-theme.png)

### Light Theme
![Light Theme](screenshots/light-theme.png)

## Installation

### Download Installer

Download the latest installer from the [Releases page](https://github.com/AhmedQw1/SoundPlayer/releases).

### Installation Steps

1. Run the downloaded `.exe` installer
2. Follow the installation prompts
3. Launch SoundPlayer from the desktop shortcut or Start menu

### System Requirements

- Windows 10 or higher
- 4GB RAM recommended
- 100MB free disk space
- Java Runtime Environment (bundled with installer)

## Usage

### Quick Start Guide

1. Launch SoundPlayer
2. Click "Open File" or "Open Folder" to add music
3. Use the playback controls at the bottom to control your music
4. Right-click on songs in the playlist for additional options

## Building From Source

### Prerequisites

- Java JDK 21 or higher
- JavaFX 21
- WiX Toolset v3.14+ (for building installer)

### Build Instructions

```bash
# Clone the repository
git clone https://github.com/AhmedQw1/SoundPlayer.git
cd SoundPlayer

# Build the project
mvn clean package

# Create installer (Windows)
./build_installer.bat
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

unspecified

## Acknowledgments

- JavaFX for providing the framework

---

Last updated: 2025-07-19 by AhmedQw1

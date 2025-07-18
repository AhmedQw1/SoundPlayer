package model;

import java.io.File;

public class SoundFile {
    private String name;
    private File file;

    public SoundFile(File file) {
        this.file = file;
        this.name = file.getName();
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return name;
    }
}
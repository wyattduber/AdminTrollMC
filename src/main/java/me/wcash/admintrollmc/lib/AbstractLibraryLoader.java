package me.wcash.admintrollmc.lib;

import java.util.List;

public interface AbstractLibraryLoader<Library> {
    List<Library> initLibraries();
    void loadLibraries();

}
package com.kun.util;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class Icons {

    private static @NotNull Icon load(@NotNull String path) {
        return IconLoader.getIcon(path, Icons.class);
    }

    public static final @NotNull Icon logoIcon = load("/icons/pluginIcon.svg");

}

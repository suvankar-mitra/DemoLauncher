package com.suvankarmitra.demolauncher;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.Comparator;

public class Item implements Comparable<Item>{
    CharSequence label; //package name
    CharSequence name; //app name
    Drawable icon; //app icon

    @Override
    public int compareTo(@NonNull Item o) {
        return name.toString().compareTo(o.name.toString());
    }
}

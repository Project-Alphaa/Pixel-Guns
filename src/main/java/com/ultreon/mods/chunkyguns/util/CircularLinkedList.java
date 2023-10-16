package com.ultreon.mods.chunkyguns.util;

import java.util.ArrayList;
import java.util.List;

public class CircularLinkedList<T> {
    private final List<T> elements = new ArrayList<>();
    private int currentIndex = 0;

    public CircularLinkedList() {
    }

    public void add(T element) {
        elements.add(element);
    }

    public T currentElement() {
        return elements.get(currentIndex);
    }

    public void resetIndex() {
        currentIndex = 0;
    }

    public void prev() {
        currentIndex = currentIndex == 0 ? elements.size() - 1 : currentIndex - 1;
		if (currentIndex < 0) {
			currentIndex = elements.size() - 1;
		}
    }

    public void next() {
        currentIndex = currentIndex == elements.size() - 1 ? 0 : currentIndex + 1;
    }
}
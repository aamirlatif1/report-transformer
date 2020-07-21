package com.astraia.components;

import java.util.List;

/**
 * Component is base class to build composite tree to maintain im memory structure of report
 * {@link ParentComponent} is composite class and {@link TextComponent} is leaf class
 */
public abstract class Component {

    private final String name;

    public Component(String name) {
        this.name = name;
    }

    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    public List<Component> getComponents() {
        throw new UnsupportedOperationException();
    }

    public boolean hasChild() {
        return false;
    }

    public List<Component> children() {
        throw new UnsupportedOperationException();
    }

    public String name() {
        return name;
    }

}

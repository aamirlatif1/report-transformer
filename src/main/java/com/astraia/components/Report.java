package com.astraia.components;

import java.util.List;

/**
 * It is wrapper class which manage composite tree.
 */
public class Report {

    private List<Component> components;

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<Component> getComponents() {
        return components;
    }
}

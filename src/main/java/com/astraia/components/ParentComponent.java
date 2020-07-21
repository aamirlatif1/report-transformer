package com.astraia.components;

import java.util.ArrayList;
import java.util.List;

/**
 * ParentComponent is composite class for report
 */
public class ParentComponent extends Component {

    private final List<Component> components = new ArrayList<>();

    public ParentComponent(String name) {
        super(name);
    }

    @Override
    public void add(Component component) {
        components.add(component);
    }

    @Override
    public List<Component> getComponents() {
        return components;
    }

    @Override
    public boolean hasChild() {
        return components.size() > 0;
    }

    @Override
    public List<Component> children() {
        return components;
    }
}

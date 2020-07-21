package com.astraia.renderer;

import com.astraia.components.Component;
import com.astraia.components.Report;
import com.astraia.components.TextComponent;

import java.util.List;

import static java.util.Objects.isNull;

/**
 * Render {@link Report} object according to selected implementation class
 */
public abstract class Renderer {

    /**
     * Simple factory for concrete classes of Render
     * @param type name of renderr
     * @return concreate class of Render like {@link WikiRenderer}
     */
    public static Renderer ofType(String type) {
        Renderer renderer = null;
        if (type.equals("wiki"))
            renderer = new WikiRenderer();
        return renderer;
    }

    /**
     * template method to render report composite.
     * This will render report tree according to concrete class implementation
     * of {@link #renderComponent(Component, String, int)}
     * @param report composite report to render
     * @return convert report according to concrete class
     */
    public final String render(Report report) {
        List<Component> components = report.getComponents();
        if(isNull(components) || components.isEmpty())
            return "";
        return convert(components, 0);
    }

    /**
     * utility method to recursively render the whole composite tree.
     * @param components to render
     * @param level some component are render according level nested
     * @return output of rendered tree
     */
    private String convert(List<Component> components, int level) {
        StringBuilder output = new StringBuilder();
        for (Component component : components) {
            if (component.hasChild())
                output.append(renderComponent(component, convert(component.children(), level + 1), level));
             else
                output.append(renderComponent(component, ((TextComponent) component).getContent(), level));
        }
        return output.toString().trim().replaceAll("\n\n", "\n")
                .replaceAll("\n\n", "\n");
    }

    /**
     * it actually render all components according to its formatting style.
     * @param component to render according to its formatting style
     * @param content to wrap in the formatting element
     * @param level used to render it level is needed
     * @return rendered element
     */
    public abstract String renderComponent(Component component, String content, int level);


}

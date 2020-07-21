package com.astraia.renderer;

import com.astraia.components.Component;

public class WikiRenderer extends Renderer {

    /**
     * {@inheritDoc}
     */
    public String renderComponent(Component component, String content, int level) {
        return WikiFormatting.toFormat(component.name()).apply(content, level);
    }

}

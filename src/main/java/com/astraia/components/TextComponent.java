package com.astraia.components;

/**
 * TextComponent is leaf class for report composite
 */
public class TextComponent extends Component {
    private final String content;

    public TextComponent(String content) {
        super("text");
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}

package com.astraia.renderer;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.stream.Stream.generate;

/**
 * Handle formatting of individual elements rendering for Wiki style.
 * If you want to add support for any other element for wiki style give implementation here. e.g.,
 * <pre>{@code
 *  ORDERED_ITEM("orderitem"){
 *       public String apply(String text, int level) {
 *           String li = generate(() -> "#").limit(level).collect(Collectors.joining());
 *             return String.format("%s%s%s\n", li, text, li);
 *         }
 *  }
 * }</pre>
 */
public enum WikiFormatting {

    TEXT("text") {
        public String apply(String text, int level) { return text; }
    },
    SECTION("section") {
        public String apply(String text, int level) {  return String.format("\n%s\n", text);}
    },
    HEADING("heading") {
        public String apply(String text, int level) {
          String heading = generate(() -> "=").limit(level).collect(Collectors.joining());
            return String.format("%s%s%s\n", heading, text, heading);
        }
    },
    BOLD("bold") {
        public String apply(String text, int level) { return String.format("'''%s'''", text); }
    },
    ITALIC("italic") {
        public String apply(String text, int level) { return String.format("''%s''", text); }
    };

    WikiFormatting(String name) {
        this.name = name;
    }

    public static WikiFormatting toFormat(String name) {
        return Arrays.stream(values())
                .filter(v -> v.name.equals(name)).findFirst().get();
    }

    private final String name;
    public abstract String apply(String text, int level);
}

package com.astraia.renderer;

import com.astraia.components.Component;
import com.astraia.components.ParentComponent;
import com.astraia.components.Report;
import com.astraia.components.TextComponent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class WikiRendererTest {

    private WikiRenderer wikiRenderer;

    @Before
    public void setUp() throws Exception {
        wikiRenderer = new WikiRenderer();
    }

    @Test
    public void whenReportTreeIsNull() {
        //Given
        Report report = new Report();
        //When
        String output = wikiRenderer.render(report);
        //Then
        assertThat(output, equalTo(""));
    }

    @Test
    public void whenReportTreeIsEmpty() {
        //Given
        Report report = new Report();
        report.setComponents(Collections.EMPTY_LIST);
        //When
        String output = wikiRenderer.render(report);
        //Then
        assertThat(output, equalTo(""));
    }

    @Test
    public void whenReportContainSectionAndHeading() {
        //Given
        Report report = new Report();
        List<Component> components = new ArrayList<>();
        Component pc = new ParentComponent("section");
        pc.add(createComponentWithText("heading", "This is heading text"));
        components.add(pc);
        report.setComponents(components);
        //When
        String output = wikiRenderer.render(report);
        //Then
        assertThat(output, equalTo("=This is heading text="));
    }

    @Test
    public void whenReportOnlyBoldAndItalic() {
        //Given
        Report report = new Report();
        List<Component> components = new ArrayList<>();
        components.add(createComponentWithText("bold", "This is bold text"));
        components.add(createComponentWithText("italic", "This is italic text"));
        report.setComponents(components);
        //When
        String output = wikiRenderer.render(report);
        //Then
        assertThat(output, equalTo("'''This is bold text'''''This is italic text''"));
    }

    public Component createComponentWithText(String name, String text) {
        Component pc = new ParentComponent(name);
        pc.add(new TextComponent(text));
        return pc;
    }

}
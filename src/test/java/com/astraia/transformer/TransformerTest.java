package com.astraia.transformer;

import com.astraia.renderer.Renderer;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TransformerTest {

    private Transformer transformer;

    @Before
    public void setUp() throws Exception {
        URL res = getClass().getClassLoader().getResource("sample/example1.xml");
        File file = Paths.get(res.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();
        transformer = new Transformer(Paths.get(absolutePath), Renderer.ofType("wiki"));
    }

    @Test
    public void whenReportContainSectionAndHeading() throws Exception{
        //When
        TransformedResult result = transformer.call();

        //Then
        String content = getOutputFileContent();
        assertThat(result.getContent(), equalTo(content));
    }

    private String getOutputFileContent() throws Exception {
        URL res = getClass().getClassLoader().getResource("sample/example1.wiki");
        File file = Paths.get(res.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();
        return new String(Files.readAllBytes(Paths.get(absolutePath)));
    }

}
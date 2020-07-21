package com.astraia.transformer;

import com.astraia.components.Report;
import com.astraia.renderer.Renderer;
import com.astraia.xml.XMLParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * a thread to transform given file {@link #inputFilePath} to output according to {@link #renderer}
 */
@RequiredArgsConstructor
public class Transformer implements Callable<TransformedResult> {

    private static final Logger logger = LoggerFactory.getLogger(Transformer.class);

    private final Path inputFilePath;
    private final Renderer renderer;

    @Override
    public TransformedResult call() {
        logger.info("transforming file : {}", inputFilePath.getFileName());
        XMLParser xmlParser = XMLParser.parser(inputFilePath);
        Report report = xmlParser.parse();
       return new TransformedResult(inputFilePath.getFileName().toString(), renderer.render(report));
    }


}

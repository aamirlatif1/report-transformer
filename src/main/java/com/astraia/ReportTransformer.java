package com.astraia;

import com.astraia.renderer.Renderer;
import com.astraia.transformer.TransformedResult;
import com.astraia.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static com.astraia.utils.FileUtil.*;

/**
 * Transform all xml files from given directory to output directory according to given output format
 */
public class ReportTransformer {
    private static final Logger logger = LoggerFactory.getLogger(ReportTransformer.class);
    private static final String EXTENSION = "xml";

    private final String inputDirectory;
    private final String outputDirectory;
    private final String outputFormat;
    private final int threads;

    public ReportTransformer(String inputDirectory, String outputDirectory, String outputFormat, int threads) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
        this.outputFormat = outputFormat;
        this.threads = threads;
    }

    /**
     * transform given all xml files from {@link #inputDirectory}
     * to file format given {@link #outputFormat} and place it at {@link #outputDirectory}
     * this transform files in multiple threads to improve performance.
     *
     * @throws IOException if file not found
     */
    public void transform() throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        transformExistingFiles(executorService);
        transformNewFiles(executorService);

        // clean up on service exit
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    executorService.shutdown();
                    logger.info("Application Stopped");
                }
        ));
    }

    private void transformExistingFiles(ExecutorService executorService) throws IOException {
        Set<String> processedFiles = getProcessedFiles(inputDirectory);
        List<Path> filePaths = getFilesList(inputDirectory, EXTENSION);
        List<Callable<TransformedResult>> transformers = new ArrayList<>();
        for (Path filePath : filePaths) {
            if (!processedFiles.contains(filePath.getFileName().toString()))
                transformers.add(new Transformer(filePath, Renderer.ofType(outputFormat)));
        }
        try {
            saveResults(executorService.invokeAll(transformers));
            logger.info("existing {} files processed", executorService.invokeAll(transformers).size());
        } catch (InterruptedException | ExecutionException e) {
            logger.error("An error occurred", e);
        }
    }

    /**
     * watch {@link #inputDirectory} for any new file to add and transform it
     * @param executorService uses for execution of threads
     * @throws IOException if file not found
     */
    private void transformNewFiles(ExecutorService executorService) throws IOException {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(inputDirectory);
            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                List<Callable<TransformedResult>> transformers = new ArrayList<>();
                for (WatchEvent<?> event : key.pollEvents()) {
                    logger.info("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
                    transformers.add(new Transformer(Paths.get(inputDirectory, event.context().toString()), Renderer.ofType(outputFormat)));
                }
                saveResults(executorService.invokeAll(transformers));
                key.reset();
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("An error occurred", e);
        }
    }

    private void saveResults(List<Future<TransformedResult>> futureResults) throws InterruptedException, ExecutionException, IOException {
        List<String> processed = new ArrayList<>();
        for (Future<TransformedResult> result : futureResults) {
            String fileName = result.get().getFileName();
            saveFile(outputFilePath(fileName), result.get().getContent());
            processed.add(fileName);
        }
        markFileProcessed(inputDirectory, processed);
    }

    private String outputFilePath(String fileName) {
        return outputDirectory + "/" + fileName.substring(0, fileName.indexOf(".") + 1) + outputFormat;
    }

}

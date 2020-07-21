package com.astraia;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Option(name = "-i", aliases = {"--input"}, usage = "input directory by default current directory")
    private String inputDirectory = "./";

    @Option(name = "-o", aliases = {"--output"}, usage = "output directory by default current directory")
    private String outputDirectory = "./";

    @Option(name = "--outputFormat", usage = "output format by default wiki (will be support others in future)")
    private String outputFormat = "wiki";

    @Option(name = "-t", aliases = {"--threads"}, usage = "number of threads (default: 2)")
    private int threads = 2;

    private final List<String> commandLineArguments = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        new Main(args).run();
    }

    public Main(String... args) {
        commandLineArguments.addAll(Arrays.asList(args));
    }

    private void run() throws IOException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(commandLineArguments);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar reporttransformer.jar [options...]");
            parser.printUsage(System.err);
            System.err.println();
            return;
        }
        logger.info("transforming xml files from {}", inputDirectory);
       new ReportTransformer(inputDirectory,
                outputDirectory,
                outputFormat,
                threads).transform();

        logger.info("All files are transformed to {} ana placed at {}", outputFormat, outputDirectory);
    }
}

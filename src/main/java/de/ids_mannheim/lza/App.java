package de.ids_mannheim.lza;

import edu.wisc.library.ocfl.core.db.ObjectDetailsDatabaseBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import edu.wisc.library.ocfl.api.OcflRepository;
import edu.wisc.library.ocfl.api.model.DigestAlgorithm;
import edu.wisc.library.ocfl.core.OcflRepositoryBuilder;
import edu.wisc.library.ocfl.core.extension.storage.layout.config.HashedNTupleLayoutConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.cli.*;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.postgresql.ds.PGSimpleDataSource;
/**
 * Hello world!
 *
 */
public class App 
{
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    static Path repoDir = Paths.get("ocfl-repo"); // This directory contains the OCFL storage root.

    static Path workDir = Paths.get("ocfl-work"); // This directory is used to assemble OCFL versions.
                                                        // It cannot be within the storage root

    private final OcflRepository repo;

    public App() {
        this(repoDir.toString(),workDir.toString());
    }
    
    public App(String repoDir, String workDir) {
        App.repoDir = Paths.get(repoDir);
        App.workDir = Paths.get(workDir);
        // the OCFL storage root.
        if (!App.repoDir.toFile().exists())
            App.repoDir.toFile().mkdir();
        if (!App.workDir.toFile().exists())
            App.workDir.toFile().mkdir();

        //SQLiteDataSource ds = new SQLiteDataSource();
        //ds.setDatabaseName("ocfl.db");
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUser("postgres");
        repo = new OcflRepositoryBuilder()
                .defaultLayoutConfig(new HashedNTupleLayoutConfig()
                        .setTupleSize(2).
                        setDigestAlgorithm(DigestAlgorithm.sha512))
                .storage(storage -> storage.fileSystem(App.repoDir))
                .workDir(App.workDir)
                // Optional: add database for caching metadata
                //.objectDetailsDb(new ObjectDetailsDatabaseBuilder().dataSource(ds).build())
                .build();
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in de.ids_mannheim.lza package
        final ResourceConfig rc = new ResourceConfig().packages("de.ids_mannheim.lza");
        HashMap<String,Object> props = new HashMap<>();
        props.put("ocfl_repo",repo);
        rc.addProperties(props);
        //rc.register(new LoggingFeature((java.util.logging.Logger) null));
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }
    /**
     * Main method.
     * @param args No command line arguments expected
     */
    public static void main( String[] args )
    {
        Options options = new Options();
        options.addOption(newOpt("s","store", true, "The OCFL store", true));
        options.addOption(newOpt("w", "work-dir", true, "The work dir", true));
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);  //it will parse according to the options and parse option value
        } catch (ParseException e) {
            formatter.printHelp("App", options);
            System.exit(1);
        }
        App app = new App(cmd.getOptionValue("s"), cmd.getOptionValue("w"));

        app.logger.info("Starting server");
        final HttpServer server = app.startServer();
        System.out.printf("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...%n", BASE_URI);
        // Add proper shutdown when server is exited
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.logger.info("Shutting down server");
            server.shutdown();
        }));
        app.mainWait();
    }

    /**
     * Suspend main thread
     */
    synchronized void mainWait() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create new option and set its required feature
     * @param opt the short name
     * @param longOpt the long name
     * @param hasArg requires an argument
     * @param description option description
     * @param required flag if the option is required
     * @return the new option
     */
    private static Option newOpt(String opt, String longOpt, boolean hasArg, String description, boolean required)
    {
        Option o = new Option(opt, longOpt, hasArg, description);
        o.setRequired(required);
        return o;
    }
}

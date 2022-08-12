package de.ids_mannheim.lza;

import edu.wisc.library.ocfl.api.OcflRepository;
import edu.wisc.library.ocfl.api.model.DigestAlgorithm;
import edu.wisc.library.ocfl.core.OcflRepositoryBuilder;
import edu.wisc.library.ocfl.core.extension.storage.layout.config.HashedNTupleLayoutConfig;
import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@SpringBootApplication
public class OcflJavaRestApplication {

  static Path repoDir = Paths.get("ocfl-repo"); // This directory contains the OCFL storage root.

  static Path workDir = Paths.get("ocfl-work"); // This directory is used to assemble OCFL versions.
  // It cannot be within the storage root

  public static void main(String[] args) {
    Options options = new Options();
    options.addOption(Option.builder("s")
            .longOpt("store")
            .desc("The OCFL store")
            .hasArg()
            .required(false)
            .build());
        options.addOption(Option.builder("w")
            .longOpt("work-dir")
            .desc("The work dir")
            .hasArg()
            .required(false)
            .build());
    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    try {
        CommandLine cmd = parser.parse(options, args);  //it will parse according to the options and parse option value
        if (cmd.hasOption("s"))
            OcflJavaRestApplication.repoDir = Paths.get(cmd.getOptionValue("s"));
        if (cmd.hasOption("w"))
            OcflJavaRestApplication.workDir = Paths.get(cmd.getOptionValue("w"));
    } catch (ParseException e) {
      formatter.printHelp("OcflJavaRestApplication", options);
      System.exit(1);
    }
    ConfigurableApplicationContext ctx = SpringApplication.run(OcflJavaRestApplication.class, args);
    HashMap<String,Object> map = new HashMap<>();

    // TODO handle command line parameters
    // the OCFL storage root.
    if (!OcflJavaRestApplication.repoDir.toFile().exists())
      OcflJavaRestApplication.repoDir.toFile().mkdir();
    if (!OcflJavaRestApplication.workDir.toFile().exists())
      OcflJavaRestApplication.workDir.toFile().mkdir();
//    PGSimpleDataSource ds = new PGSimpleDataSource();
//    ds.setUser("postgres");
    OcflRepository repo = new OcflRepositoryBuilder()
            .defaultLayoutConfig(new HashedNTupleLayoutConfig()
                    .setTupleSize(2).
                    setDigestAlgorithm(DigestAlgorithm.sha512))
            .storage(storage -> storage.fileSystem(OcflJavaRestApplication.repoDir))
            .workDir(OcflJavaRestApplication.workDir)
            // Optional: add database for caching metadata
            //.objectDetailsDb(new ObjectDetailsDatabaseBuilder().dataSource(ds).build())
            .build();
    map.put("ocfl_repo",repo);
    ctx.getEnvironment().getPropertySources().addLast(new MapPropertySource("MY_MAP", map));
  }
}

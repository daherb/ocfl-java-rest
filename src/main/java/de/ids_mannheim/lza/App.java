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
public class App {

  static Path repoDir = Paths.get("ocfl-repo"); // This directory contains the OCFL storage root.

  static Path workDir = Paths.get("ocfl-work"); // This directory is used to assemble OCFL versions.
  // It cannot be within the storage root

  public static void main(String[] args) {
    Options options = new Options();
    options.addOption(newOpt("s","store", true, "The OCFL store", false));
    options.addOption(newOpt("w", "work-dir", true, "The work dir", false));
    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);  //it will parse according to the options and parse option value
    } catch (ParseException e) {
      formatter.printHelp("App", options);
      System.exit(1);
    }
    ConfigurableApplicationContext ctx = SpringApplication.run(App.class, args);
    HashMap<String,Object> map = new HashMap<>();

    // TODO handle command line parameters
    // the OCFL storage root.
    if (!App.repoDir.toFile().exists())
      App.repoDir.toFile().mkdir();
    if (!App.workDir.toFile().exists())
      App.workDir.toFile().mkdir();
//    PGSimpleDataSource ds = new PGSimpleDataSource();
//    ds.setUser("postgres");
    OcflRepository repo = new OcflRepositoryBuilder()
            .defaultLayoutConfig(new HashedNTupleLayoutConfig()
                    .setTupleSize(2).
                    setDigestAlgorithm(DigestAlgorithm.sha512))
            .storage(storage -> storage.fileSystem(App.repoDir))
            .workDir(App.workDir)
            // Optional: add database for caching metadata
            //.objectDetailsDb(new ObjectDetailsDatabaseBuilder().dataSource(ds).build())
            .build();
    map.put("ocfl_repo",repo);
    ctx.getEnvironment().getPropertySources().addLast(new MapPropertySource("MY_MAP", map));
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

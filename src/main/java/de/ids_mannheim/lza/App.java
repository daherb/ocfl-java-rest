package de.ids_mannheim.lza;

import edu.wisc.library.ocfl.api.OcflRepository;
import edu.wisc.library.ocfl.api.model.DigestAlgorithm;
import edu.wisc.library.ocfl.api.model.ObjectVersionId;
import edu.wisc.library.ocfl.api.model.VersionInfo;
import edu.wisc.library.ocfl.core.OcflRepositoryBuilder;
import edu.wisc.library.ocfl.core.extension.storage.layout.config.HashedNTupleLayoutConfig;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Path repoDir = Paths.get("ocfl-repo"); // This directory contains the OCFL storage root.
        Path workDir = Paths.get("ocfl-work"); // This directory is used to assemble OCFL versions. It cannot be within
        // the OCFL storage root.
        if (!repoDir.toFile().exists())
            repoDir.toFile().mkdir();
        if (!workDir.toFile().exists())
            workDir.toFile().mkdir();
        OcflRepository repo = new OcflRepositoryBuilder()
                .defaultLayoutConfig(new HashedNTupleLayoutConfig()
                        .setTupleSize(2).
                        setDigestAlgorithm(DigestAlgorithm.sha512))
                .storage(storage -> storage.fileSystem(repoDir))
                .workDir(workDir)
                .build();

        repo.putObject(ObjectVersionId.head("ids:o1"),
                Paths.get("object-data-dir"),
                new VersionInfo()
                        .setUser("Herbert Lange","mailto:lange@ids-mannheim.de")
                        .setMessage("initial commit")
        );
        System.out.println(repo.validateObject("ids:o1",true));
    }
}

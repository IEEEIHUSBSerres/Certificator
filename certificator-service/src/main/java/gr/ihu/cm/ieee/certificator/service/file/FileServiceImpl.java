package gr.ihu.cm.ieee.certificator.service.file;

import io.vavr.control.Try;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileServiceImpl implements FileService {

    @Override
    public Try<String> readFile(final File file) {
        return Try.of(file::getAbsolutePath)
                .flatMap(absoluteFilePath -> Try.of(() -> Paths.get(absoluteFilePath))
                        .flatMap(filePath -> Try.of(() -> Files.readAllBytes(filePath))
                                .flatMap(fileBytes -> Try.of(() -> new String(fileBytes)))));
    }
}

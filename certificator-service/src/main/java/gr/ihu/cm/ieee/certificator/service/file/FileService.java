package gr.ihu.cm.ieee.certificator.service.file;

import io.vavr.control.Try;

import java.io.File;
import java.util.List;

public interface FileService {

    Try<String> readFile(File file);
}

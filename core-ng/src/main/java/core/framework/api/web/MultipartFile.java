package core.framework.api.web;

import java.nio.file.Path;

/**
 * @author neo
 */
public class MultipartFile {
    public final Path path;
    public final String fileName;
    public final String contentType;

    public MultipartFile(Path path, String fileName, String contentType) {
        this.path = path;
        this.fileName = fileName;
        this.contentType = contentType;
    }
}

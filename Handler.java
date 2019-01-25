package handler;

import java.io.File;
import java.io.IOException;

public interface Handler {
    void handler(File html) throws IOException;
}

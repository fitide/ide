package org.ide;

import java.nio.file.Path;

public record GoToResult(Path file, int row, int col) {
}

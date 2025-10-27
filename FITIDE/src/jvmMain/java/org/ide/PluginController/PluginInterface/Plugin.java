package org.ide.PluginController.PluginInterface;

import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;

public interface Plugin {


    ParseTree getFileParseTree(File file);
}

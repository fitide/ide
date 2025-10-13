package org.ide.File_explorer.FELogger;

public class ConsoleLogger implements FELogger{

    @Override
    public void print(String str) {
        System.out.println(str);
    }
}

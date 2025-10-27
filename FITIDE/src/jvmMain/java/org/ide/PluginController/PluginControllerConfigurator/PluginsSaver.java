package org.ide.PluginController.PluginControllerConfigurator;

import org.ide.PluginController.LangsInfo.Lang;
import org.ide.PluginController.LangsInfo.LangConf;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class PluginsSaver {

    public static void saveToConfig(Path pathToConfDir, String confName, Map<String, Lang> langList) throws IOException {
        JSONObject jsonObject = new JSONObject();

        JSONObject langs = new JSONObject();
        for (var lang : langList.values()) {
            JSONObject langPar = new JSONObject();

            setExtension(lang, langPar);
            setConfigs(lang, langPar);

            langs.put(lang.language, langPar);
        }
        jsonObject.put("languages", langs);


        saveToFile(jsonObject, pathToConfDir, confName);
    }

    private static void setExtension(Lang lang, JSONObject langPar) {
        JSONArray extensions = new JSONArray();
        extensions.addAll(Arrays.asList(lang.filesExtensions));
        langPar.put("extension", extensions);
    }

    private static void setConfigs(Lang lang, JSONObject langPar) {
        JSONArray configsJson = new JSONArray();
        Set<String> names = lang.confs.keySet();
        for (var name : names) {
            LangConf conf = lang.confs.get(name);
            JSONObject config = new JSONObject();

            config.put("name", name);
            config.put("path_to_compiler", conf.pathToCompile.toString());

            JSONArray argsJson = new JSONArray();
            argsJson.addAll(Arrays.asList(conf.compileArgs));
            config.put("args", argsJson);

            configsJson.add(config);
        }
        langPar.put("configs", configsJson);
    }

    private static void saveToFile(JSONObject json, Path pathToConfDir, String confName) throws IOException {
        File confDir = new File(pathToConfDir.toString());
        if (!confDir.exists()) {
            if (!confDir.mkdir()) {
                throw new RuntimeException("Unable to create conf dir");
            }
        }

        File conf = new File(pathToConfDir + File.separator + confName + ".json");
        if (!conf.exists()) {
            if (!conf.createNewFile()) throw new RuntimeException("Unable to create conf");
        }

        String pathToTempConf = pathToConfDir + File.separator + confName + "Temp" + ".json";
        File confTemp = new File(pathToTempConf);
        if (!conf.exists()) {
            if (!confTemp.createNewFile()) throw new RuntimeException("Unable to create conf");
        }

        PrintWriter pw = new PrintWriter(new FileOutputStream(confTemp, false));

        pw.write(json.toJSONString());
        pw.flush();

        Files.move(confTemp.toPath(), conf.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}

package io;


import com.google.gson.Gson;
import web.observation.ObservablePage;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FSHelper {
    private static String CONFIG_FILE_NAME = "observable_config.config";

    public List<ObservablePage> getConfigInfo() throws IOException //файл конфигурации содержит информацию о добавленных страницах
    {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(CONFIG_FILE_NAME));
        }
        catch (FileNotFoundException e)
        {
            File configFile = new File(CONFIG_FILE_NAME);
            configFile.createNewFile();
            reader = new BufferedReader(new FileReader(CONFIG_FILE_NAME));
        }

        List<String> strings = new LinkedList<>();
        while(reader.ready())
        {
            strings.add(reader.readLine());
        }
        List<ObservablePage> pages = new ArrayList<>(strings.size());
        Gson gson = new Gson();
        for (String str: strings)
        {
            pages.add( gson.fromJson(str,ObservablePage.class));
        }

        reader.close();
        return pages;
    }

    public void saveConfigInfo(List<ObservablePage> pages) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE_NAME));
        Gson gson = new Gson();
        for (ObservablePage page : pages)
            writer.write(gson.toJson(page) + "\n");
        writer.close();
    }

    public void save(String info, String fileName) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(makeWritable(fileName)));
        writer.write(info);
        writer.close();
    }

    public String load(String fileName) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(makeWritable(fileName)));
        StringBuilder info = new StringBuilder("");
        while (reader.ready())
            info.append(reader.readLine() + "\n");
        info.deleteCharAt(info.length() - 1);
        reader.close();
        return info.toString();
    }

    public void delete(String fileName)
    {
        new File(makeWritable(fileName)).delete();
    }

    public String makeWritable(String s)
    {
        String[] strings = s.split("[/\\\\:]");
        StringBuilder result = new StringBuilder("");
        for (String str : strings)
        {
            result.append(str);
        }
        return result.toString();
    }


}

package hit.go.forward.common.config;

import java.io.File;
import java.io.FileInputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONConfigReader {
    private static Gson JSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        JSON = builder.create();
    }

    public static <T> T readConfig(String configFileName, Class<? extends T> configEntityClass) {
        File configFile = new File(JSONConfigReader.class.getClassLoader().getResource(configFileName).getFile());
        FileInputStream reader = null;
        try {
            byte[] data = new byte[1048576];
            reader = new FileInputStream(configFile);
            int length = reader.read(data);
            String jsonStr = new String(data, 0, length);
            return JSON.fromJson(jsonStr, configEntityClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
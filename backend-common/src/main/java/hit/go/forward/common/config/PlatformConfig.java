package hit.go.forward.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PlatformConfig {
    

    public static String testRead() {
        File config = new File(PlatformConfig.class.getClassLoader().getResource("config.json").getFile());
        try {
            FileInputStream reader = new FileInputStream(config);
            byte[] data = new byte[1048576];
            int length = reader.read(data);
            String jsonStr = new String(data, 0, length);
            
            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
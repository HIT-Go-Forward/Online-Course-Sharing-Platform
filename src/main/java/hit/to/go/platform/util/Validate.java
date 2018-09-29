package hit.to.go.platform.util;

import java.util.Random;

/**
 * Created by 班耀强 on 2018/9/29
 */
public class Validate {
    private static final Random random = new Random();
    private static final String codes = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String genValidateCode() {
        int c;
        StringBuilder buffer = new StringBuilder();
        for (int i = 0;i < 6;i ++) {
            c = random.nextInt(codes.length());
            buffer.append(codes.charAt(c));
        }
        return buffer.toString();
    }
}

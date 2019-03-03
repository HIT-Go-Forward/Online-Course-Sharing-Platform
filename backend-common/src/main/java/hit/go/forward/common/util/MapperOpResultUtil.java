package hit.go.forward.common.util;

public class MapperOpResultUtil {
    public static boolean isSucceded(Integer rows) {
        return rows != null && rows.equals(1);
    }

    public static boolean isSucceded(Integer rows, int rowsExpected) {
        return rows != null && rows.equals(rowsExpected);
    }
}
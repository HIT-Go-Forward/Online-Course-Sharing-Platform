package hit.to.go.platform.protocol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/2
 */
public class RequestWrapper extends HttpServletRequestWrapper {
    private Map<String, String[]> paras = new HashMap<>();
    private String uri;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        Map<String, String[]> map = request.getParameterMap();
        for (String key : map.keySet()) {
            String[] values = map.get(key);
            if (values == null || values.length == 0 || values[0].equals("")) continue;
            paras.put(key, values);
        }

        uri = request.getRequestURI();
    }

    public void setRequestURI(String uri) {
        this.uri = uri;
    }

    @Override
    public String getRequestURI() {
        return uri;
    }

    public void addParameter(String key, Object value) {
        if (value != null) {
            if (value instanceof String[]) paras.put(key, (String[]) value);
            else if (value instanceof String) paras.put(key, new String[] {(String) value});
            else paras.put(key, new String[]{String.valueOf(value)});
        }
    }

    @Override
    public String getParameter(String name) {
        String[] values = paras.get(name);
        return values == null || values.length == 0 ? null : values[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return paras;
    }

    @Override
    public String[] getParameterValues(String name) {
        return paras.get(name);
    }
}

package hit.go.forward.test;

import org.junit.Test;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class TestAction {
    private static final OkHttpClient client = new OkHttpClient();

    @Test
    public void testActions() {

        System.out.println();
    }

    private void testLogin() {

    }
}

class Client {

    static void get(String url, ResponseLister lister) {

    }

    static void post(String url, ResponseLister lister) {
        
    }
}

interface ResponseLister {
    void success(Response response);

    void error(Response response);
}
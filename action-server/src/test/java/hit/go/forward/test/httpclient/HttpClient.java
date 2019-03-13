package hit.go.forward.test.httpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import hit.go.forward.common.protocol.RequestResult;
import hit.go.forward.common.protocol.RequestResults;

public final class HttpClient {
    private static CloseableHttpClient client = HttpClients.createDefault();

    public static void get(String url, ResponseListener listener) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        BufferedReader reader = null;
        try {
            response = client.execute(httpGet);
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            RequestResult result = RequestResults.fromJSON(reader.readLine());
            if (listener != null) listener.onSuccess(result);
        } catch (Exception e) {
            if (listener != null) listener.onError(e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void post(String url, PostBody body, ResponseListener listener) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        BufferedReader reader = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(body.pairs));
            response = client.execute(httpPost);
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            RequestResult result = RequestResults.fromJSON(reader.readLine());
            if (listener != null) listener.onSuccess(result);
        } catch (Exception e) {
            if (listener != null) listener.onError(e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static abstract class ResponseListener {
        public abstract void onSuccess(RequestResult result);

        public abstract void onError(Exception e);
    }

    public static class PostBody {
        List<NameValuePair> pairs = new ArrayList<>();

        public PostBody add(String name, String value) {
            pairs.add(new BasicNameValuePair(name, value));
            return this;
        }
    }
}
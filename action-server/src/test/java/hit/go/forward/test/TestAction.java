package hit.go.forward.test;

import org.junit.Test;

import hit.go.forward.common.protocol.RequestResult;
import hit.go.forward.test.httpclient.HttpClient;
import hit.go.forward.test.httpclient.HttpClient.ResponseListener;

public class TestAction {
    private static final boolean isDebug = false;
    private static final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJoaXQuZ28uZm9yd2FyZCIsInVzZXJUeXBlIjoiMiIsInVzZXJJZCI6IjMiLCJ2ZXJzaW9uIjoiMCJ9.yRcWHZunRs6KZBvI_3yhTXbk6QvFkCIFtfvMS_6IhM0uo4g4BjQU8izeaxX3Ok_GVuHKlObW6ARfjnGUlgXzlw";
    private static final ResponseListener listener = new ResponseListener(){
    
        @Override
        public void onSuccess(RequestResult result) {
            if (result.getStatus() >= 20000 && result.getStatus() < 30000) System.out.println(result.getData());
            throw new RequestFailedException(result);
        }
    
        @Override
        public void onError(Exception e) {
            throw new RuntimeException(e);
        }
    };
    private static final String HOST = "http://localhost:8080";

    @Test
    public void testActions() {
        if (isDebug) {
            testLogin();
            testSendValidateCode();
            testSendValidateCodeToCurrentUser();
            testGetUserInfo();
            testModifyInfo();
            System.out.println();
        }
    }

    private void testSendValidateCode() {
        String url = HOST + "/authority/sendValidateCode.action?email=1103255088@qq.com";
        HttpClient.get(url, listener);
    }

    private void testSendValidateCodeToCurrentUser() {
        String url = HOST + "/authority/sendValidateCodeToCurrentUser.action";
        HttpClient.get(url, listener);
    }

    private void testGetUserInfo() {
        String url = HOST + "/authority/getUserInfo.action?userId=3";
        HttpClient.get(url, listener);
    }

    private void testModifyInfo() {
        String url = HOST + "/authority/modifyInfo.action?sex=1&birthday=2017-01-10&education=1" +
            "&school=534&intro=imadmin&note=1&phone=15812345678&token=" + token;
        HttpClient.get(url, listener);
    }

    private void testLogin() {
        String url = HOST + "/authority/login.action?account=3&&password=b7b5a5fe444395b635f17604a7c67dbf";
        HttpClient.get(url, listener);
    }
}

class RequestFailedException extends RuntimeException {

    public RequestFailedException(RequestResult result) {
        super(result.toString());
    }
}
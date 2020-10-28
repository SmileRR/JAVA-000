import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuickStartTest {
    @Test
    public void getTest() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8088/api/hello");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            Assert.assertEquals("HTTP/1.1 200 OK", response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            // 确保响应头完成响应
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
    }
}
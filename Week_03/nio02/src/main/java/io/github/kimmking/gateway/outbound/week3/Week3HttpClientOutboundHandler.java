package io.github.kimmking.gateway.outbound.week3;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class Week3HttpClientOutboundHandler {

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        FullHttpResponse fullResponse = null;
        CloseableHttpResponse httpResponse = null;

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8088/api/hello");

            for (Map.Entry<String, String> entry: fullRequest.headers().entries()) {
                System.out.println("header key:" + entry.getKey() + " value: "+entry.getValue());
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }

            httpResponse = httpclient.execute(httpGet);
            HttpEntity responseEntity = httpResponse.getEntity();
            System.out.println("响应状态为:" + httpResponse.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                byte[] body = EntityUtils.toByteArray(responseEntity);
                System.out.println("响应内容为:" +  new String(body));
                httpResponse.close();

                fullResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
                fullResponse.headers().set("Content-Type", "application/json");
                fullResponse.headers().setInt("Content-Length", Integer.parseInt(httpResponse.getFirstHeader("Content-Length").getValue()));
                ctx.write(fullResponse);
                ctx.flush();
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

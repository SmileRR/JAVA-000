package io.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Arrays;
import java.util.concurrent.*;

public class LoadRunner {
    private static int concurrentNum = 10;
    private static int totalNum = 100;
    private static String url = "http://www.baidu.com";
    public static void main(String[] args) throws Exception{
        //init member variable;
        init(args);

        System.out.printf("Testing Url: %s\n", url);
        System.out.printf("Concurrent: %3d, Total Num: % d\n", concurrentNum, totalNum);

        long startTime = System.currentTimeMillis();
        int failureNum = 0;
        long totalTime = 0;
        long sumOfTimeSpent = 0;
        double totalAvg = 0;
        double percAvg = 0;
        long[] durations = new long[totalNum];
        ExecutorService executor = Executors.newFixedThreadPool(concurrentNum);

        try{
            for (int i = 0; i < totalNum; i++){
                FutureTask<Long> requestTask = getLongFutureTask(i);
                executor.submit(requestTask);
                Long timeSpent = requestTask.get(30, TimeUnit.SECONDS);
                if (timeSpent != -1) {
                    durations[i] = timeSpent;
                    sumOfTimeSpent += timeSpent;
                } else {
                    durations[i] = Long.MAX_VALUE;
                    failureNum++;
                }
            }
            Arrays.sort(durations);
            totalAvg = Arrays.stream(durations).average().orElse(Double.NaN);
            int percIndex = Math.round((totalNum-failureNum) * 95/100);
            percAvg = Arrays.stream( Arrays.copyOfRange(durations, 0, percIndex)).average().orElse(Double.NaN);
            totalTime = System.currentTimeMillis() - startTime;
            System.out.println("================= Load Runner Result / Million Seconds ==================");
            System.out.printf("Executed Num: %d, Failure Num: %d\n", totalNum, failureNum);
            System.out.printf("Avg Time: %.2f, 95%% Avg Time: %.2f, 95%% Index: %d\n", totalAvg, percAvg, percIndex);

            System.out.printf("Total Duration: %d, Sum of TimeSpent:%d\n", totalTime, sumOfTimeSpent);
        } finally {
            executor.shutdown();
        }
    }

    private static FutureTask<Long> getLongFutureTask(int i) {
        FutureTask<Long> requestTask = new FutureTask<Long>(new Callable<Long>() {
            public Long call() throws Exception {
                Long start = System.currentTimeMillis();
                Long duration = -1L;

                //HTTP client call
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(url);
                CloseableHttpResponse response = httpclient.execute(httpGet);
                try {
                    HttpEntity responseEntity = response.getEntity();
                    if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                        duration = System.currentTimeMillis() -  start;
                    } else {
                        // other case consider as failed.
                        duration = -1L;
                    }
//                    System.out.printf("Thread: %s, Duration: %s\n", Thread.currentThread().getName(), duration);
                } finally {
                    response.close();
                }
                return duration;
            }
        }) ;
        return requestTask;
    }

    static void init(String[] args) {
        if(args == null) return;

        for (String arg : args) {
            if (arg == null || arg.length() <= 1) continue;

            //input url
            if (arg.startsWith("http://")) {
                url = arg;
                continue;
            }

            String paramName = arg.substring(0,1);
            String paramValue = arg.substring(1);
            //input concurrent number
            if (paramName.equalsIgnoreCase("c") && paramValue.matches("[0-9]+")){
                concurrentNum = Integer.parseInt(arg.substring(1));
                continue;
            }
            //input duration seconds
            if (paramName.equalsIgnoreCase("t") && paramValue.matches("[0-9]+")){
                totalNum = Integer.parseInt(arg.substring(1));
                continue;
            }
        }
    }
}

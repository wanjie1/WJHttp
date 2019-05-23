package com.example.wj.wjhttp.http;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Dispatcher {

    //最大请求数
    private int maxRequests;

    //同一个host最大请求数目
    private int maxRequestsPreHost;

    public Dispatcher()
    {
        this(64,5);
    }
    public Dispatcher(int maxRequests,int maxRequestsPreHost)
    {
        this.maxRequests = maxRequests;
        this.maxRequestsPreHost = maxRequestsPreHost;
    }

    private ExecutorService executorService = null;//声明一个线程池

    //等待双端队列
    private final Deque<Call.AsyncCall> readyAsyncCalls = new ArrayDeque<>();
    //正在运行的双端队列
    private final Deque<Call.AsyncCall> runningAsyncCalls = new ArrayDeque<>();


    //实现线程池的初始化
    public synchronized ExecutorService initExecutorService()
    {
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r,"http Client");
                return thread;
            }
        };
        if(executorService == null)
        {
            executorService = new ThreadPoolExecutor(0,Integer.MAX_VALUE,60,TimeUnit.SECONDS,new SynchronousQueue<Runnable>(),threadFactory);
        }
        return executorService;
    }

    public void enqueue(Call.AsyncCall asyncCall)
    {
        //判断正在运行的主机数目是否小于maxRequests,同时同一个host请求小于小于maxRequestPreHost
        if(runningAsyncCalls.size() < maxRequests && getRunningPreHostCount(asyncCall) < maxRequestsPreHost)
        {
            runningAsyncCalls.add(asyncCall);
            initExecutorService().execute(asyncCall);
        }
        else
        {
            readyAsyncCalls.add(asyncCall);
        }
    }

    private int getRunningPreHostCount(Call.AsyncCall asyncCall)
    {
        int count = 0;
        for(Call.AsyncCall runningAsyncCall: runningAsyncCalls)
        {
            if(runningAsyncCall.getHost().equals(asyncCall.getHost()))
                count++;
        }
        return count;
    }

    public void finished(Call.AsyncCall asyncCall)
    {
        synchronized (this)
        {
            runningAsyncCalls.remove(asyncCall);
            checkReadyCalls();
        }
    }

    private void checkReadyCalls()
    {
        if(runningAsyncCalls.size() > maxRequests)
            return;
        if(readyAsyncCalls.isEmpty())
            return;

        Iterator<Call.AsyncCall> iterator = readyAsyncCalls.iterator();
        while(iterator.hasNext())
        {
            Call.AsyncCall asyncCall = iterator.next();
            if(getRunningPreHostCount(asyncCall) < maxRequestsPreHost)
            {
                iterator.remove();
                runningAsyncCalls.add(asyncCall);
                executorService.execute(asyncCall);
            }

            if(runningAsyncCalls.size() < maxRequests)
                return;
        }
    }
}

package com.chenhe.client.nettydemo2.channel;

import java.util.concurrent.*;

/**
 * @author chenhe
 * @date 2019-05-11 16:43
 * @desc
 */
public class SyncFuture<T> implements Future<T> {

    /**
     * 请求-响应同步
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 响应结果
     */
    private T response;

    /**
     * 请求开始时间
     */
    private long beginTime = System.currentTimeMillis();
    
    public SyncFuture(){
        
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        if(response!=null){
            return true;
        }
        return false;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return this.response;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        countDownLatch.await(timeout,unit);
        return this.response;
    }

    public void setResponse(T response) {
        this.response = response;
        countDownLatch.countDown();
    }

    public long getBeginTime() {
        return beginTime;
    }
}

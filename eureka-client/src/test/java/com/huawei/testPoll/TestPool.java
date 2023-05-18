package com.huawei.testPoll;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestPool implements Runnable{
    private Integer index;

    public TestPool(Integer index) {
        this.index = index;
    }

    @Override
    public void run() {
        System.out.println(this.index);
    }

    public static void main(String[] args) {
        try {
            createCacheThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void createCacheThread() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            executorService.execute(new TestPool(temp));
            // 如果加上sleep，可以更清晰地看到线程池将线程复用了
//            Thread.sleep(1);
        }
    }


}

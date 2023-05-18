package com.huawei.testPoll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestPool1  implements Callable<TestPool1>  {
    private Integer index;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public TestPool1(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "TestPool1{" +
                "index=" + index +
                '}';
    }

    @Override
    public TestPool1 call() throws Exception {
        this.setIndex(this.index);
        return this;
    }

    public static void main(String[] args) {
        try {
            List<TestPool1> cacheThread = createCacheThread();
            for (TestPool1 a:cacheThread) {
                System.out.println(a);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static List<TestPool1> createCacheThread() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<TestPool1> testPool1List = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            Future<TestPool1> submit = executorService.submit(new TestPool1(temp));
            TestPool1 testPool1 = submit.get();
            testPool1List.add(testPool1);
            // 如果加上sleep，可以更清晰地看到线程池将线程复用了
//            Thread.sleep(1);
        }
        return testPool1List;
    }


}

package com.personal.mall.search.thread;

import java.util.concurrent.*;

/**
 * @ClassName ThreadDemo
 * @Author liupanpan
 * @Date 2025/9/11
 * @Description 三种创建线程的方式
 */
public class ThreadDemo {
    // 创建线程池
    private static ExecutorService es = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main方法执行了...");
        Demo01 demo01 = new Demo01();
        demo01.start();

        Demo02 demo02 = new Demo02();
        new Thread(demo02).start();

        new Thread(() -> {
            System.out.println("Runnable2 ------> "+Thread.currentThread().getName());
        }).start();

        FutureTask<String> futureTask = new FutureTask<>(new Demo03());
        FutureTask<?> futureTask1 = new FutureTask<>(new Demo02(),null);

        Thread thread = new Thread(futureTask);
        Thread thread1 = new Thread(futureTask1);
        thread.start();
        thread1.start();
        String s = futureTask.get();
        System.out.println("Callable返回结果："+s);

        // 线程池1

        es.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("pool1 ------> "+Thread.currentThread().getName());
            }
        });
        es.shutdown();
        System.out.println("main方法结束了...");

        // 线程池2
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5,
                100,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture.runAsync(() -> {
            System.out.println("CompletableFuture--void--start");
            int i = 200/20;
            System.out.println("CompletableFuture--void--end");
        },poolExecutor);

        // 返回结果
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("CompletableFuture--return--start");
            int i = 200 / 20;
            System.out.println("CompletableFuture--return--end");
            return i;
        }, poolExecutor).handle((res,exec) -> {
            System.out.println("res----->"+res+";exec------>"+exec);
            return res*10;
        });
        System.out.println("返回结果："+future.get());

        poolExecutor.allowCoreThreadTimeOut(true);
        poolExecutor.execute(() -> {
            System.out.println("pool2 ------> "+Thread.currentThread().getName());
        });



    }
}

class Demo01 extends Thread {
    @Override
    public void run() {
        System.out.println("thread ------> "+Thread.currentThread().getName());
    }
}

class Demo02 implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable1 ------> "+Thread.currentThread().getName());
    }
}

class Demo03 implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("Callable ------> "+Thread.currentThread().getName());
        return "ok";
    }
}

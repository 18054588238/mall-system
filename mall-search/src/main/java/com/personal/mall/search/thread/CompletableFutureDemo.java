package com.personal.mall.search.thread;

import java.util.concurrent.*;

/**
 * @ClassName CompletableFutureDemo
 * @Author liupanpan
 * @Date 2025/9/15
 * @Description
 */
public class CompletableFutureDemo {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
            50,
            5,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());


    /*public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("111111");
            int i = 100/0;
            System.out.println("222222");
            return i;
        }, executor).whenCompleteAsync((res, exec) -> {
            System.out.println("res-----" + res + "exec-----" + exec);
        }).exceptionally((res) -> { // 异步任务抛异常时触发
            System.out.println("res---" + res);
            return 10;
        });
        System.out.println(future.get());
    }*/

    /*public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("111");
            int i = 100;
            System.out.println("222");
            return i;
        }, executor)*//*.thenRunAsync(() -> {
            System.out.println("222");
        })*//**//*.thenAcceptAsync((res) -> {
            System.out.println("res----->" + res);
        })*//*.thenApplyAsync((res) -> {
            System.out.println("res --- >" + res);
            return res*2;
        });
        System.out.println(future.get());
    }*/

    /*// 所有线程执行完成后 再执行
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future1 -- start");
            int i = 100;
            System.out.println("future1 -- end");
            return i;
        },executor);

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future2 -- start");
            int i = 50;
            System.out.println("future2 -- end");
            return i;
        },executor);

        future1.runAfterBothAsync(future2, () -> {
            System.out.println("run--");
        });

        future1.thenAcceptBothAsync(future2,(f1,f2) -> {
            System.out.println("future1 -- >"+f1);
            System.out.println("future2 -- >"+f2);
        });

        // thenCombineAsync: 既可以获取前面两个线程的返回结果，同时也会返回结果给阻塞的线程
        CompletableFuture<Integer> future = future1.thenCombineAsync(future2, (f1, f2) -> {
            System.out.println("future1 -- >" + f1);
            System.out.println("future2 -- >" + f2);
            return f1 + f2;
        });
        System.out.println(future.get());

    }*/

    // 任一线程执行完成后 就执行
    /*public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future1 -- start");
            int i = 100;
            System.out.println("future1 -- end");
            return i;
        },executor);

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future2 -- start");
            int i = 50;
            System.out.println("future2 -- end");
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                System.out.println("-");
            }
            return i;
        },executor);

        future1.runAfterEitherAsync(future2,() -> {
            System.out.println("不接受返回结果，本身也不返回结果");
        });

        future1.acceptEitherAsync(future2,(f) -> {
            System.out.println("接收返回结果："+f);
        });

        CompletableFuture<Integer> future = future1.applyToEitherAsync(future2, (res) -> {
            System.out.println("接收返回结果：" + res);
            return res * 2;
        });
        System.out.println(future.get());

    }*/
    // 两个以上线程
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future1 -- start");
            int i = 100;
            System.out.println("future1 -- end");
            return i;
        },executor);

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future2 -- start");
            int i = 50;
            System.out.println("future2 -- end");
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                System.out.println("-");
            }
            return i;
        },executor);

        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future3 -- start");
            int i = 300;
            System.out.println("future3 -- end");
            return i;
        },executor);

        // 所有线程全部执行完毕
        CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2, future3);
        allOf.get() ;// 阻塞，等待所有任务执行完毕
        System.out.println("allOf:"+allOf.get()+"1--"+future1.get()+"2--"+future2.get()+"3--"+future3.get());

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future1, future2, future3);
        System.out.println(anyOf.get());

    }
}

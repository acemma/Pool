package ac.thread.pool;

/**
 * @author acemma
 * @date 2018/12/24 22:15
 * @Description
 */
public class Task implements Runnable {

    private static volatile Integer i = 1;

    @Override
    public void run() {
        // 执行任务
        synchronized (i) {
            System.out.println("当前处理的线程是：" + Thread.currentThread().getName() + ",执行任务：" + (i++) + "完成");
        }
    }
}

package ac.thread.pool;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author acemma
 * @date 2018/12/24 10:52
 * @Description 线程池实现类
 */
public class ThreadPoolManager implements ThreadPool{

    //线程池中默认线程个数
    private static int workNum = 5;

    // 工作线程数组
    private WorkThread[] workThreads;

    //正在执行的线程任务数量
    private static volatile int excuteTaskNumber = 0;

    //任务队列，作为一个缓冲
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();

    private static ThreadPoolManager threadPoolManager;

    private AtomicLong threadNum = new AtomicLong();

    //单例模式
    private ThreadPoolManager(){
        this(ThreadPoolManager.workNum);
    }

    private ThreadPoolManager(int workNum){
        if (workNum > 0){
            ThreadPoolManager.workNum = workNum;
        }
        workThreads = new WorkThread[ThreadPoolManager.workNum];
        for (int i=0;i<ThreadPoolManager.workNum;i++){
            workThreads[i] = new WorkThread();
            Thread thread = new Thread(workThreads[i],"ThreadPool-worker-" + threadNum.incrementAndGet());
            thread.start();
            System.out.println("初始化线程总数：" + (i+1) + ",当前线程名称是：ThreadPool-worker-" + threadNum);
        }
    }

    public static ThreadPool getThreadPool(){
        return getThreadPool(workNum);
    }

    public static ThreadPool getThreadPool(int workNum){
        if (workNum > 0){
            ThreadPoolManager.workNum = workNum;
        }
        if (threadPoolManager == null){
            threadPoolManager = new ThreadPoolManager(ThreadPoolManager.workNum);
        }
        return threadPoolManager;
    }


    @Override
    public void execute(Runnable task) {
        synchronized (taskQueue){
            taskQueue.add(task);
            taskQueue.notifyAll();
        }
    }

    @Override
    public void execute(List<Runnable> tasks) {
        synchronized (taskQueue){
            taskQueue.addAll(tasks);
            taskQueue.notifyAll();
        }
    }

    @Override
    public int getExecuteTaskNumber() {
        return excuteTaskNumber;
    }

    @Override
    public int getWaitTaskNumber() {
        return taskQueue.size();
    }

    @Override
    public int getWorkThreadNumber() {
        return workNum;
    }

    @Override
    public void destroy() {
        while (!taskQueue.isEmpty()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i=0; i < workThreads.length; i++){
            workThreads[i].shutDown();
            workThreads[i] = null;
        }
        threadPoolManager = null;
        taskQueue.clear();
    }


    private class WorkThread implements Runnable{
        //线程是否可用标识
        private boolean isRunning = true;

        @Override
        public void run() {
            Runnable runner = null;
            while (isRunning){
                synchronized (taskQueue){
                    while (isRunning && taskQueue.isEmpty()){
                        try {
                            taskQueue.wait(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!taskQueue.isEmpty()){
                        runner = taskQueue.poll();
                    }
                }
                if (runner != null){
                    runner.run();
                }
                excuteTaskNumber++;
                runner = null;
            }
        }

        public void shutDown(){
            isRunning = false;
        }
    }
}

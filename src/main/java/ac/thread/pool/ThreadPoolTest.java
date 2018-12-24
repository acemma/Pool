package ac.thread.pool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author acemma
 * @date 2018/12/24 22:16
 * @Description
 */
public class ThreadPoolTest {

    public static void main(String[] args) {
        ThreadPool t = ThreadPoolManager.getThreadPool(5);
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tasks.add(new Task());
        }
        System.out.println(t);
        t.execute(tasks);
        // 所有的线程执行完成才destroy
        t.destroy();
        System.out.println(t);
    }
}

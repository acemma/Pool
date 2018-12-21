package ac.db.conn.pool;

/**
 * @author acemma
 * @date 2018/12/21 11:43
 * @Description 数据库连接池工厂 单例模式
 */
public class MyPoolFactory {

    private static class CreatePool{
        private static IMyPool myPool = new MyDefaultPool();
    }

    public static IMyPool getInstance(){
        return CreatePool.myPool;
    }

}

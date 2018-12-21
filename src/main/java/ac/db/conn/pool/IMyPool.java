package ac.db.conn.pool;

/**
 * @author acemma
 * @date 2018/12/21 10:47
 * @Description IMyPool，对外提供数据库连接池的基本服务，比如得到一个数据库操作管道，或创建一个数据库管道
 */
public interface IMyPool {

    /**
     * 获取连接
     * @return
     */
    MyPoolConnection getMyPoolConnection();

    /**
     * 创建连接
     * @param count
     */
    void createMyPoolConnection(int count);

}

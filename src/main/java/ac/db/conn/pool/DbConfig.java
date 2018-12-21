package ac.db.conn.pool;

/**
 * @author acemma
 * @date 2018/12/21 10:53
 * @Description 数据库连接池配置文件 这里简化直接提供常量
 */
public class DbConfig {

    //jdbc驱动
    public static final String jdbcDriver = "com.mysql.jdbc.Driver";
    //jdbc连接地址
    public static final String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/demo";
    //jdbc连接用户名
    public static final String jdbcUsername = "root";
    //jdbc连接密码
    public static final String jdbcPassword = "root";

    //数据库连接池初始化大小
    public static final int initCount = 10;
    //连接池不足是增长的步进值
    public static final int step = 2;
    //连接池的最大连接数
    public static final int maxCount = 50;

}

package ac.db.conn.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Vector;

/**
 * @author acemma
 * @date 2018/12/21 11:17
 * @Description
 * 连接池：
 * 1、加载外部初始化配置
 * 2、加载数据库驱动
 * 3、要考虑要用什么集合对连接管道存储 考虑线程安全 这里用vector
 */
public class MyDefaultPool implements IMyPool{

    private Vector<MyPoolConnection> myPoolConnectionVector = new Vector<>();
    private static String jdbcUrl;
    private static String jdbcUsername;
    private static String jdbcPassword;
    private static int initCount;
    private static int step;
    private static int maxCount;


    public MyDefaultPool(){
        //加载外部初始化配置
        init();

        //加载驱动
        try {
            Class.forName(DbConfig.jdbcDriver);
        }catch (Exception e){
            e.printStackTrace();
        }

        //初始化数据库连接池管道
        createMyPoolConnection(initCount);
    }


    /**
     * 加载外部初始化配置
     */
    private void init(){
        jdbcUrl = DbConfig.jdbcUrl;
        jdbcUsername = DbConfig.jdbcUsername;
        jdbcPassword = DbConfig.jdbcPassword;
        initCount = DbConfig.initCount;
        step = DbConfig.step;
        maxCount = DbConfig.maxCount;
    }


    @Override
    public MyPoolConnection getMyPoolConnection() {
        if (myPoolConnectionVector.size() < 1){
            throw new RuntimeException("连接池初始化错误");
        }
        MyPoolConnection myPoolConnection = null;
        try {
            //从连接管道集合中取得连接
            myPoolConnection = getRealConnectionFromPool();
            while (myPoolConnection == null){
                //如果没有取得，则创建新的连接添加到管道集合中
                createMyPoolConnection(step);
                //再次连接管道集合中取得连接
                myPoolConnection = getRealConnectionFromPool();
                return myPoolConnection;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return myPoolConnection;
    }

    @Override
    public void createMyPoolConnection(int count) {
        if (myPoolConnectionVector.size() > maxCount || myPoolConnectionVector.size() + count > maxCount){
            throw new RuntimeException("连接池已满");
        }

        for (int i=0; i < count; i++){
            try {
                Connection connection = DriverManager.getConnection(jdbcUrl,jdbcUsername,jdbcPassword);
                MyPoolConnection myPoolConnection = new MyPoolConnection(connection,false);
                myPoolConnectionVector.add(myPoolConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private synchronized MyPoolConnection getRealConnectionFromPool() throws Exception{
        for (MyPoolConnection myPoolConnection : myPoolConnectionVector){
            if (!myPoolConnection.isBusy()){
                //假设超时时间为3000s
                if (myPoolConnection.getConnection().isValid(3000)){
                    myPoolConnection.setBusy(true);
                    return myPoolConnection;
                }else {
                    //超时则重新建立连接
                    Connection connection = DriverManager.getConnection(jdbcUrl,jdbcUsername,jdbcPassword);
                    myPoolConnection.setBusy(true);
                    myPoolConnection.setConnection(connection);
                    return myPoolConnection;
                }
            }
        }
        return null;
    }

}

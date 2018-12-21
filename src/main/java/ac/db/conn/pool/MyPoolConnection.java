package ac.db.conn.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author acemma
 * @date 2018/12/21 11:11
 * @Description 封装connection
 */
public class MyPoolConnection {

    private Connection connection;
    private boolean isBusy = false;

    public MyPoolConnection(Connection connection,boolean isBusy){
        this.connection =  connection;
        this.isBusy = isBusy;
    }

    //关闭连接
    public void close(){
        this.isBusy = false;
    }

    //查询
    public ResultSet query(String sql){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }
}

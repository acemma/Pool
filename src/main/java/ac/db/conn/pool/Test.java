package ac.db.conn.pool;

import java.sql.ResultSet;

/**
 * @author acemma
 * @date 2018/12/21 11:47
 * @Description 测试类
 */
public class Test {

    public static IMyPool myPool = MyPoolFactory.getInstance();

    public static void main(String[] args) {

        for (int i=0; i < 100; i++){
            MyPoolConnection myPoolConnection = myPool.getMyPoolConnection();
            ResultSet query = myPoolConnection.query("select * from t_user");
            try {
                while (query.next()){
                    System.out.println(query.getString("username") + "," + query.getString("phone")
                            + ",使用管道：" + myPoolConnection.getConnection());
                }
                myPoolConnection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}

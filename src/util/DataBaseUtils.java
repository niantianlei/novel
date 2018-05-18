package util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import bean.User;

public class DataBaseUtils {
	
	private static String username; //用户名
	private static String password; //密码
	private static String dataBaseName; //数据库名
	
	static {
		config("jdbc.properties");
	}

	

	/**
	 * 配置数据库的基本信息
	 * @return
	 */
	public static void config(String path){
		InputStream inputStream = DataBaseUtils.class.getClassLoader().getResourceAsStream(path);
		Properties p = new Properties();
		try {
			p.load(inputStream);
			username = p.getProperty("db.username");
			password = p.getProperty("db.password");
			dataBaseName = p.getProperty("db.dataBaseName");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库链接
	 * @return
	 */
	public static Connection getConnection(){
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection  = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dataBaseName+"?useUnicode=true&characterEncoding=utf8",username,password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * 关闭链接
	 * @param connection
	 * @param statement
	 * @param rs
	 */
	public static void closeConnection(Connection connection,PreparedStatement statement,ResultSet rs){
		try {
			if(rs!=null)rs.close();
			if(statement!=null)statement.close();
			if(connection!=null)connection.close();
		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}
	
	
	/**
	 * 查询出数据，并且list返回
	 * @param sql
	 * @param objects
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String,Object>> queryForList(String sql,Object...objects){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.prepareStatement(sql);
			for (int i = 0; i < objects.length; i++) {
				statement.setObject(i+1, objects[i]);
			}
			
			rs = statement.executeQuery();
			while(rs.next()){
				ResultSetMetaData resultSetMetaData = rs.getMetaData();
				int count = resultSetMetaData.getColumnCount(); //获取列数
				Map<String,Object> map = new HashMap<String, Object>();
				for (int i = 0; i < count; i++) {
					map.put(resultSetMetaData.getColumnName(i+1), rs.getObject(resultSetMetaData.getColumnName(i+1)));
				}
				result.add(map);
			};
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeConnection(connection, statement, rs);
		}
		
		return result;
	}
	
	/**
	 * 查询出数据，并且map返回
	 * @param sql
	 * @param objects
	 * @return
	 * @throws SQLException
	 */
	public static Map<String,Object> queryForMap(String sql,Object...objects) throws SQLException{
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String,Object>> list = queryForList(sql, objects);
		if(list.size() != 1){
			return null;
		}
		result = list.get(0);
		return result;
	}
	
	/**
	 * 查询出数据，并且返回一个JavaBean
	 * @param sql
	 * @param clazz
	 * @param objects
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static <T>T queryForBean(String sql,Class clazz,Object...objects){
		T obj = null;
		Map<String,Object> map = null;
		Field field = null;
		try {
			obj = (T) clazz.newInstance();   //创建一个新的Bean实例
			map = queryForMap(sql, objects); //先将结果集放在一个Map中
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(map == null){
			return null;
		}
		//遍历Map
		for (String columnName : map.keySet()) {
			Method method = null;
			String propertyName = StringUtils.columnToProperty(columnName); //属性名称
			
			try {
				field = clazz.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} 
			//获取JavaBean中的字段
			String fieldType = field.toString().split(" ")[1]; //获取该字段的类型
			//System.out.println(fieldType);
			Object value = map.get(columnName);
			if(value == null){
				continue;
			}
			/**获取set方法的名字* */
			String setMethodName = "set" + StringUtils.upperCaseFirstCharacter(propertyName);
			//System.out.println(setMethodName);
			try {
				/**获取值的类型* */
				String valueType = value.getClass().getName();
				
				/**查看类型是否匹配* */
				if(!fieldType.equalsIgnoreCase(valueType)){
					//System.out.println("类型不匹配");
					if(fieldType.equalsIgnoreCase("java.lang.Integer")){
						value = Integer.parseInt(String.valueOf(value));
					}else if(fieldType.equalsIgnoreCase("java.lang.String")){
						value = String.valueOf(value);
					}else if(fieldType.equalsIgnoreCase("java.util.Date")){
						valueType = "java.util.Date";
						//将value转换成java.util.Date
						String dateStr = String.valueOf(value);
						Timestamp ts = Timestamp.valueOf(dateStr);
						Date date = new Date(ts.getTime());
						value = date;
					}
				}
				
				/**获取set方法* */
				//System.out.println(valueType);
				method = clazz.getDeclaredMethod(setMethodName,Class.forName(fieldType));
				/**执行set方法* */
				method.invoke(obj, value);
			}catch(Exception e){
				/**如果报错，基本上是因为类型不匹配* */
				e.printStackTrace();
			}
		}
		//System.out.println(obj);
		return obj;
	}
	
	
	/**
	 * DML操作
	 * @param sql
	 * @param objects
	 */
	public static void update(String sql,Object...objects){
		Connection connection = getConnection();
	    PreparedStatement statement = null;
	    try {
	    	statement = (PreparedStatement) connection.prepareStatement(sql);
	    	for (int i = 0; i < objects.length; i++) {
				statement.setObject(i+1, objects[i]);
			}
	    	statement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }finally{
	    	closeConnection(connection, statement, null);
	    }
	}
	
	
	
	public static void main(String[] args) throws SQLException, NoSuchFieldException, SecurityException {
		User user = DataBaseUtils.queryForBean("select * from t_user  limit 1", User.class);
		DataBaseUtils.config("jdbc.properties");
		Map map = DataBaseUtils.queryForMap("select * from t_user where username = ?","李四");
		System.out.println(map);
	}
}

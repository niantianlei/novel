package test;

import java.sql.Connection;
import java.util.Map;

import util.DataBaseUtils;

public class TestDataBaseUtils {

	public static void main(String[] args) {
		DataBaseUtils.config("jdbc.properties");
		Connection conn = DataBaseUtils.getConnection();
		System.out.println(conn);
		
	}
}

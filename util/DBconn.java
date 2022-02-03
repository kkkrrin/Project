package com.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconn {

	private static Connection conn =null;

	public static Connection getConnection() { // db연결 

		String url="jdbc:oracle:thin:@127.0.0.1:1521:xe";
		String user="suzi";
		String pwd="a123";

		if(conn==null) {

			try {

				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn=DriverManager.getConnection(url,user,pwd);


			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

		return conn; 

	}
	

	public static void close() { // 연결 끊기 

		if(conn==null) {
			return;
		}
		try {
			if(!conn.isClosed()) {
				conn.close();
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		conn=null; // 연결 끊고나서 초기화해주는 작업.
	}

}


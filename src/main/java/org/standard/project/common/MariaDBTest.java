package org.standard.project.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBTest {

		public static void main(String[] args) {
			String url = "jdbc:mysql://localhost:3306/standard";
			String user = "root";
			String password = "12345";
			try {
				Class.forName("org.mariadb.jdbc.Driver");
				System.out.println("�뱶�씪�씠踰� 寃��깋 �꽦怨�");
				Connection conn = DriverManager.getConnection(url, user, password);
				System.out.println(conn);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("�뱶�씪�씠踰� 寃��깋 �떎�뙣");
			} catch (SQLException e) {
				System.out.println("Connection Error!");
				e.printStackTrace();
			}
			System.out.println("JdbcUtil Connection >> " + JdbcUtil.getConnection());
		}
}
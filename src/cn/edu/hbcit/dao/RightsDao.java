package cn.edu.hbcit.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;


import cn.edu.hbcit.pojo.Books;
import cn.edu.hbcit.pojo.Majors;
import cn.edu.hbcit.pojo.Users;
import cn.edu.hbcit.utils.MD5;

/**
 * 公共权限类
 * 简要说明:
 * @author 刘杰
 * @version 1.00  2014-6-16下午08:57:57	新建
 */
public class RightsDao {
	protected final Logger log = Logger.getLogger(RightsDao.class.getName());
	/**
	 * 判断权限是否为负责人
	 * @return
	 */
	public boolean isMajorsManager(String username){
		
		ArrayList<Users> list = null;
		boolean flag=false;
		try {
			Connection conn = Base.Connect();
			Users users = new Users();
			QueryRunner qr = new QueryRunner();
			String sql = "SELECT level FROM tb_users WHERE PK_users=? AND level=1";
			
			list = (ArrayList<Users>)qr.query(conn, sql, new BeanListHandler(Users.class),username);
			if (list.size()>0){
				flag=true;
			}
			DbUtils.closeQuietly(conn);//关闭连接
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

	}
	/**
	 * 判断权限是否为系领导
	 * @return
	 */
	public boolean isDepartmentManager(String username){
		
		ArrayList<Users> list = null;
		boolean flag=false;
		try {
			Connection conn = Base.Connect();
			Users users = new Users();
			QueryRunner qr = new QueryRunner();
			String sql = "SELECT level FROM tb_users WHERE PK_users=? AND level=0";
			
			list = (ArrayList<Users>)qr.query(conn, sql, new BeanListHandler(Users.class),username);
			if (list.size()>0){
				flag=true;
			}
			DbUtils.closeQuietly(conn);//关闭连接
					} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

	}
	/**
	 * 根据用户名，返回用户真实姓名
	 * @return
	 */
	public String getRealName(String username){
		ArrayList<Users> list = null;
		
		String realname=null;
		try{
		Connection conn = Base.Connect();
		Users users = new Users();
		QueryRunner qr = new QueryRunner();
		
		String sql = "SELECT true_name FROM tb_users WHERE PK_users=?  ";
		list = (ArrayList<Users>)qr.query(conn, sql, new BeanListHandler(Users.class),username);
		
		
		for(Users a : list){
			realname=a.getTrue_name();
		}
		DbUtils.closeQuietly(conn);//关闭连接
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return realname;
		
		
	}
	
	/**
	 * 查询所有教师姓名和ID
	 * @return
	 */
	public ArrayList selectLeaders(){
		ArrayList<Users> list = null;
		try {
			Connection conn = Base.Connect();
			Users users = new Users();
			QueryRunner qr = new QueryRunner();
			String sql = "SELECT PK_users, true_name FROM tb_users ORDER BY PK_users";
		
			list = (ArrayList<Users>)qr.query(conn, sql, new BeanListHandler(Users.class));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
		
	}

	/***
	 * 根据用户名查询用户信息
	 * @param name
	 * @return
	 */
		public ArrayList selectUsersByName(String name){
			ArrayList<Users> list = null;
			try {
				Connection conn = Base.Connect();
				Users users = new Users();
				QueryRunner qr = new QueryRunner();
				String sql = "SELECT PK_users, true_name, level FROM tb_users WHERE PK_users=?";
			
				list = (ArrayList<Users>)qr.query(conn, sql, new BeanListHandler(Users.class),name);
			
				DbUtils.closeQuietly(conn);//关闭连接
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return list;
		}
		/***
		 * 查询用户所有信息
		 * @return
		 */
		public ArrayList selectUsersAll(){
			ArrayList<Users> list = null;
			try {
				Connection conn = Base.Connect();
				Users users = new Users();
				QueryRunner qr = new QueryRunner();
				String sql = "SELECT PK_users, password, true_name, level FROM tb_users ORDER BY PK_users";
			
				list = (ArrayList<Users>)qr.query(conn, sql, new BeanListHandler(Users.class));
			
				DbUtils.closeQuietly(conn);//关闭连接
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return list;
		}
		/***
		 * 查询用户名是否存在
		 * @param username
		 * @return
		 */
		public boolean isUserExist(String username){
			ArrayList<Users> list = null;
			boolean flag=false;
			try {
				Connection conn = Base.Connect();
				Users users = new Users();
				QueryRunner qr = new QueryRunner();
				String sql = "SELECT * FROM tb_users WHERE PK_users=?";
				
				list = (ArrayList<Users>)qr.query(conn, sql, new BeanListHandler(Users.class),username);
				if (list.size()>0){
					flag=true;
				}
				DbUtils.closeQuietly(conn);//关闭连接
						} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return flag;
		}
	/***
	 * 新增用户
	 * @param pk_user
	 * @param password
	 * @param true_name
	 * @param level
	 * @return
	 */
		public boolean addUsers(String pk_user, String password, String true_name, int level){
			int count = 0;
			boolean flag = false;
			try {
				Connection conn = Base.Connect();
				QueryRunner qr = new QueryRunner();
				String pwd=MD5.MD5Encode(password);
				String sql = "INSERT INTO tb_users (pk_users, password, true_name, level) values (?, ?, ?, ?)";
			
				count = qr.update(conn, sql, pk_user, pwd, true_name, level);
				
				DbUtils.closeQuietly(conn);//关闭连接
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(count>0){
				flag = true;
			}
			return flag;
	}
	/***
	 * 根据用户名修改信息
	 * @param pk_user
	 * @param password
	 * @param true_name
	 * @param level
	 * @return
	 */
		public boolean updateUsers(String pk_user,  String true_name, int level){
			int count = 0;
			boolean flag = false;
			try {
				Connection conn = Base.Connect();
				QueryRunner qr = new QueryRunner();
				String sql = "UPDATE tb_users SET  true_name=?, level=? WHERE pk_users=? ";
			
				count = qr.update(conn, sql, true_name, level, pk_user);
				
				DbUtils.closeQuietly(conn);//关闭连接
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(count>0){
				flag = true;
			}
			return flag;
		}
		/***
		 * 重置用户密码/修改密码
		 * @param password
		 * @param pk_user
		 * @return
		 */
		public boolean reSetPassword(String password,String pk_user){
			int count = 0;
			boolean flag = false;
			try {
				Connection conn = Base.Connect();
				QueryRunner qr = new QueryRunner();
				String sql = "UPDATE tb_users SET  password=? WHERE pk_users=? ";
			
				count = qr.update(conn, sql, password, pk_user);
				
				DbUtils.closeQuietly(conn);//关闭连接
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(count>0){
				flag = true;
			}
			return flag;
		}
		/***
		 * 删除用户
		 * @param pk_user
		 * @return
		 */
		public boolean deleteUsers(String pk_user){
			int count = 0;
			boolean flag = false;
			try {
				Connection conn = Base.Connect();
				QueryRunner qr = new QueryRunner();
				String sql = "DELETE FROM tb_users WHERE pk_users=? ";
			
				count = qr.update(conn, sql, pk_user);
				
				DbUtils.closeQuietly(conn);//关闭连接
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(count>0){
				flag = true;
			}
			return flag;
		}

}

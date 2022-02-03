package com.kmlogin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.join.MemberDTO;


public class KmmemberDAO {
	private Connection conn = null;
	
	public KmmemberDAO(Connection conn) {
		
		this.conn = conn;
		
	}
	public int insertData(KmmemberDTO dto) {

		int result=0;

		PreparedStatement pstmt=null;
		String sql;

		try {


			sql="insert into kmmember(userId,userPwd,userName,userTel,userJob,sellUser,userFor) values(?,?,?,?,?,?,?)";

			pstmt=conn.prepareStatement(sql);

			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserPwd());
			pstmt.setString(3, dto.getUserName());
			pstmt.setString(4, dto.getUserTel());
			pstmt.setString(5, dto.getUserJob());
			pstmt.setString(6, dto.getSellUser());
			pstmt.setString(7, dto.getUserFor());

			result=pstmt.executeUpdate();

			pstmt.close();


		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return result; 

	}
	
	public KmmemberDTO getReadData(String userId) {
		
		KmmemberDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
					
		try {
			
			sql = "select userId, userPwd, userName,userTel, userJob, sellUser, userFor, profileImg from kmmember where userId = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				dto = new KmmemberDTO();
				
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserTel(rs.getString("userTel"));
				dto.setUserJob(rs.getString("userJob"));
				dto.setSellUser(rs.getString("sellUser"));
				dto.setUserFor(rs.getString("userFor"));
				dto.setProfileImg(rs.getString("profileImg"));
				
			}
			
			rs.close();
			pstmt.close();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			
		}
					
		return dto;
		
	}
	
		public KmmemberDTO getFindId(String userName, String userTel) {
		
		KmmemberDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
					
		try {
			
			sql = "select userId, userPwd, userName,userTel, userJob, sellUser, userFor from kmmember where userName = ? and userTel = ?";			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			pstmt.setString(2, userTel);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				dto = new KmmemberDTO();
				
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserTel(rs.getString("userTel"));
				dto.setUserJob(rs.getString("userJob"));
				dto.setSellUser(rs.getString("sellUser"));
				dto.setUserFor(rs.getString("userFor"));
				
			}
			
			rs.close();
			pstmt.close();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			
		}
					
		return dto;
		
	}
		public int updatedJob(String userJob, String userId) {
			
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				
				sql = "update kmmember set userJob =? where userId=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, userJob);
				pstmt.setString(2, userId);
				
				result = pstmt.executeUpdate();
				
				pstmt.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
				
			}

			return result;
		}
		
			public int updatedPwd(String userPwd, String userId) {
			
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				
				sql = "update kmmember set userPwd =? where userId=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, userPwd);
				pstmt.setString(2, userId);
				
				result = pstmt.executeUpdate();
				
				pstmt.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
				
			}

			return result;
		}
			
			public int updatedproFileImg(String fileName, String userId) {
				
				int result = 0;
				PreparedStatement pstmt = null;
				String sql;
				
				try {
					
					sql = "update kmmember set profileImg =? where userId=?";
					
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(1, fileName);
					pstmt.setString(2, userId);
					
					result = pstmt.executeUpdate();
					
					pstmt.close();
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.toString());
					
				}

				return result;
			}
		
		
			public int deleteId(String userId) {
			
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				
				sql = "delete from kmmember where userId=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, userId);
				
				result = pstmt.executeUpdate();
				
				pstmt.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
				
			}

			return result;
		}
		
		
		
	
	
}

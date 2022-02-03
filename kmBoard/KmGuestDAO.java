package com.kmBoard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KmGuestDAO {
	
	private static KmGuestDAO dao = new KmGuestDAO();
	private KmGuestDAO() {}
	
	public static KmGuestDAO getInstance() {
		return dao;
	}
	
	private Connection conn = null;
	
	
	public KmGuestDAO(Connection conn) {
		this.conn = conn;
	}
	
	
	//guNum의 max값
	public int getMaxNum() {
		
		int maxNum = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "select nvl(max(guNum),0) from kmGuest ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				maxNum = rs.getInt(1);
			
			rs.close();
			pstmt.close();
				
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return maxNum;
	}
	
	//입력
	public int insertData(KmGuestDTO dto) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "insert into kmGuest (num, guContent, guCreated, gdsNum ) ";
			sql+= " values (?,?,sysdate, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getGuNum());
			pstmt.setString(2, dto.getGuContent());
			pstmt.setInt(3, dto.getGdsNum());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
		
	}
	
	//전체데이터 가져오기
	public List<KmGuestDTO> getListData(int start, int end){
		
		List<KmGuestDTO> lists = new ArrayList<KmGuestDTO>();
		
		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "select * from ( ";
			sql+= "select rownum rnum, data.* from( ";
			sql+= "select guNum, userId, guContent, guCreated ";
			sql+= "from kmGuest where gdsNum=? order by guNum desc) data) ";
			sql+= "where rnum>=? and rnum<=? ";
			
			pstmt = conn.prepareStatement(sql);
			
			KmGuestDTO dto = new KmGuestDTO();
			
			pstmt.setInt(1, dto.getGdsNum());
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {

				dto = new  KmGuestDTO();
				
				dto.setGuNum(rs.getInt("guNum"));
				dto.setGdsNum(rs.getInt("gdsNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setGuContent(rs.getString("guContent"));
				dto.setGuCreated(rs.getString("guCreated"));
				
				lists.add(dto);
			}
			
			rs.close();
			pstmt.close();
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return lists;
		
	}
	
	//전체 데이터 갯수
	public int getDataCount() {
		
		int totalCount = 0;
		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "select nvl(count(*),0) from kmGuest where gdsNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			KmGuestDTO dto = new KmGuestDTO();
			
			pstmt.setInt(1, dto.getGdsNum());
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				totalCount = rs.getInt(1);
			
			rs.close();
			pstmt.close();
			
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return totalCount;
	}
	
	public int deleteData(int guNum) {
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "delete kmGuest where guNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, guNum);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
		
	}
	
	public int updateDate() {
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "update kmGuest set guContent=? where guNum=? ";
			
			pstmt = conn.prepareStatement(sql);
			
			KmGuestDTO dto = new KmGuestDTO();
			
			pstmt.setString(1, dto.getGuContent());
			pstmt.setInt(2, dto.getGuNum());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
		
	}
	

}

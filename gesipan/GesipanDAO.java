package com.gesipan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GesipanDAO {
	
	private Connection conn;
	public GesipanDAO(Connection conn) {
		this.conn = conn;
	}
	
	//num의 max값

		public int getMaxNum() {
			int maxNum=0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql="select nvl(max(num),0) from gesiTable";
				pstmt=conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					maxNum = rs.getInt(1);
				}
				rs.close();
				pstmt.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return maxNum;
		}
		
		
		//입력
		public int insertData(GesipanDTO dto) {
			int result=0;
			PreparedStatement pstmt = null;
			String sql;
			try {

				sql = "insert into gesiTable (num,userID,subject,content,";
				sql+="hitCount,created) ";
				sql+="values (?,?,?,?,0,sysdate)";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, dto.getNum());
				pstmt.setString(2, dto.getUserID());
				pstmt.setString(3, dto.getSubject());
				pstmt.setString(4, dto.getContent());
			
				

				result =pstmt.executeUpdate();
				pstmt.close();


			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return result;

		}
		
		
		
		
		
		//전체데이터의 갯수
		public int getDataCount(String searchKey, String searchValue) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			try {
				searchValue = "%" + searchValue + "%";
				
				sql = "select nvl(count(*),0) from gesiTable ";
				sql+= "where " + searchKey + " like ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchValue);
				rs = pstmt.executeQuery();

				if(rs.next()) {
					result = rs.getInt(1);
				}
				rs.close();
				pstmt.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return result;
		}
		
		
		
		//전체리스트
		public List<GesipanDTO> getLists(int start,int end,
				String searchKey, String searchValue){
			List<GesipanDTO> lists = new ArrayList<GesipanDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			try {
				searchValue = "%" + searchValue + "%";
				sql = "select * from (";
				sql+="select rownum rnum, data.* from(";
				sql+= "select num,userID,subject,hitCount,";
				sql+= "to_char(created,'YYYY-MM-DD') created ";
				sql+= "from gesiTable where " + searchKey + " like ? order by num desc) data) ";
				sql+="where rnum>=? and rnum<=?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchValue);
				pstmt.setInt(2, start);
				pstmt.setInt(3, end);

				rs = pstmt.executeQuery();

				while(rs.next()) {
					GesipanDTO dto = new GesipanDTO();

					dto.setNum(rs.getInt("num"));
					dto.setUserID(rs.getString("userID"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitCount"));
					dto.setCreated(rs.getString("created"));

					lists.add(dto);

				}
				rs.close();
				pstmt.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return lists;
		}
		
		
		//조회수 증가
		
		public int updateHitCount(int num) {
			
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			try {
				sql = "update gesiTable set hitCount=hitCount+1 where num=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, num);
				
				result = pstmt.executeUpdate();
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return result;
		}
		
		
		
		//num으로 조회한 한개의 데이터
		public GesipanDTO getReadData(int num) {
			
			GesipanDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			try {
				sql="select num,userID,subject,content,";
				sql+="hitCount,created from gesiTable where num=?";
				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, num);
				

				rs = pstmt.executeQuery();

				if(rs.next()) {
					dto = new GesipanDTO();

					dto.setNum(rs.getInt("num"));
					dto.setUserID(rs.getString("userID"));
					dto.setSubject(rs.getString("subject"));
					dto.setContent(rs.getString("content"));
					dto.setHitCount(rs.getInt("hitCount"));	
					dto.setCreated(rs.getString("created"));
				

				}
				rs.close();
				pstmt.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return dto;
			
		}
		
		//수정
		public int updateData(GesipanDTO dto) {
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			try {
				sql = "update gesiTable set userID=?,subject=?,";
				sql+= "content=? where num=?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getUserID());
				pstmt.setString(2, dto.getSubject());
				pstmt.setString(3, dto.getContent());
				pstmt.setInt(4, dto.getNum());
				result = pstmt.executeUpdate();
				pstmt.close();
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return result;
		}
		
		
		
		public int deleteData(int num) {
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			try {
				sql= "delete gesiTable where num=?";
				
				pstmt = conn.prepareStatement(sql);
				
		
				pstmt.setInt(1, num);
				result = pstmt.executeUpdate();
				pstmt.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		
			return result;
		}
		
		
}

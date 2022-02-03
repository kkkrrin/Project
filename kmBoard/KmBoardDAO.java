package com.kmBoard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.imageFile.ImageFileDTO;
import com.util.DBconn;

public class KmBoardDAO {
	
Connection conn = DBconn.getConnection();
	
	public KmBoardDAO(Connection conn) {
		this.conn = conn;
	}
	
	
	

	public int getMaxNum() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "select nvl(max(gdsNum),0) from kmGoods";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result =rs.getInt(1); 
				
			}
			rs.close();
			pstmt.close();
			
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
		
		
	}
	
	//입력
	public int insertData(KmBoardDTO dto) {
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "insert into kmGoods (gdsNum, gdsName, gdsPrice,";
			sql+= " gdsDes, gdsImg,cateCode,gdsSell,workDate,gdsHitCount,gdsDate,subCate) values ";
			sql+= "(?,?,?,?,?,?,?,?,0,sysdate,?) ";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getGdsNum());
			pstmt.setString(2, dto.getGdsName());
			pstmt.setInt(3, dto.getGdsPrice());
			pstmt.setString(4, dto.getGdsDes());
			pstmt.setString(5, dto.getGdsImg());
			pstmt.setString(6, dto.getCateCode());
			pstmt.setString(7, dto.getGdsSell());
			pstmt.setInt(8, dto.getWorkDate());
			pstmt.setString(9, dto.getSubCate());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;

		
	}
	//num으로 조회한 한개의 데이터 
	public KmBoardDTO getReadData(int gdsNum) {
		
		KmBoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql= "select gdsNum, gdsName, gdsPrice,";
			sql+="gdsDes,gdsImg,gdsHitCount,gdsDate,cateCode,gdsSell,workDate from kmGoods ";
			sql+="where gdsNum=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, gdsNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto= new KmBoardDTO();
				
				
				dto.setGdsNum(rs.getInt("gdsNum"));
				dto.setGdsName(rs.getString("gdsName"));
				dto.setGdsPrice(rs.getInt("gdsPrice"));
				dto.setGdsDes(rs.getString("gdsDes"));
				dto.setGdsImg(rs.getString("gdsImg"));
				dto.setGdsHitCount(rs.getInt("gdsHitCount"));
				dto.setGdsDate(rs.getString("gdsDate"));
				dto.setCateCode(rs.getString("cateCode"));
				dto.setGdsSell(rs.getString("gdsSell"));
				dto.setWorkDate(rs.getInt("workDate"));
		
			}
			
			rs.close();
			pstmt.close();
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return dto;
		
		
		
		
	}
	public CategoryDTO getCategory(String num){
		
		CategoryDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select * from kmCategory where num = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, num);
			
			rs = pstmt.executeQuery();

			while(rs.next()) {
				dto = new CategoryDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setCategory(rs.getString("Category"));				
				
			}
			rs.close();
			pstmt.close();
			
		}catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return dto;
		
	}
	public List<SubCateDTO> getSubCate(String num){
		List<SubCateDTO> lists = new ArrayList<SubCateDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select * from kmSubCate where cateNum = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, num);
			
			rs = pstmt.executeQuery();

			while(rs.next()) {
				SubCateDTO dto = new SubCateDTO();
				
				dto.setSubNum(rs.getInt("cateNum"));
				dto.setSubCate(rs.getString("subcategory"));								
				
				lists.add(dto);
				
			}
			rs.close();
			pstmt.close();
			
		}catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return lists;
		
	}
	
	public List<KmBoardDTO> getList(int start,int end,String searchKey, String searchValue, String category){
		
		List<KmBoardDTO> lists = new ArrayList<KmBoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			searchValue = "%" + searchValue + "%";
			sql = "select * from("
		               + "select rownum rnum, data.* from("
		               + "select gdsNum, gdsName, gdsPrice, gdsDes, gdsImg, gdsDate, cateCode, gdsHitCount"
		               + " from kmGoods where (gdsName like ? or gdsDes like ? or subCate like ?) and cateCode = ? order by gdsNum desc) data ) "
		               + "where rnum>=? and rnum <=?";

			

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, searchValue);
			pstmt.setString(2, searchValue);
			pstmt.setString(3, searchValue);
			pstmt.setString(4, category);
			pstmt.setInt(5, start);
			pstmt.setInt(6, end);
			
			rs = pstmt.executeQuery();

			while(rs.next()) {
				KmBoardDTO dto  = new KmBoardDTO();
				
				dto.setGdsNum(rs.getInt("gdsNum"));
				dto.setGdsName(rs.getString("gdsName"));
				dto.setGdsPrice(rs.getInt("gdsPrice"));
				dto.setGdsDes(rs.getString("gdsDes"));
				dto.setGdsImg(rs.getString("gdsImg"));
				dto.setGdsDate(rs.getString("gdsDate"));
				dto.setCateCode(rs.getString("cateCode"));
				dto.setGdsHitCount(rs.getInt("gdsHitCount"));
				
				lists.add(dto);

			}
			pstmt.close();
			rs.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return lists;
		
	}
	
	
	//결제창
		public KmBoardDTO getPayData(int gdsNum) {
			
			KmBoardDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql= "select gdsNum, gdsName, gdsPrice,";
				sql+="gdsImg,cateCode,gdsSell, workDate from kmGoods ";
				sql+="where gdsNum=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, gdsNum);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					dto= new KmBoardDTO();
					
					
					dto.setGdsNum(rs.getInt("gdsNum"));
					dto.setGdsName(rs.getString("gdsName"));
					dto.setGdsPrice(rs.getInt("gdsPrice"));
					dto.setGdsImg(rs.getString("gdsImg"));				
					dto.setCateCode(rs.getString("cateCode"));
					dto.setGdsSell(rs.getString("gdsSell"));
					dto.setWorkDate(rs.getInt("workDate"));
			
				}
				
				rs.close();
				pstmt.close();
				
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			
			return dto;
			
			
			
			
		}
	
	
	
	//수정
	public int updateData(KmBoardDTO dto) {
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql="update kmGoods set gdsName=?,gdsPrice=?,gdsDes=?,";
			sql+="gdsImg=?,gdsSell=?,cateCode=? where gdsNum=?";
			
				pstmt=conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getGdsName());
				pstmt.setInt(2, dto.getGdsPrice());
				pstmt.setString(3, dto.getGdsDes());
				pstmt.setString(4, dto.getGdsImg());
				pstmt.setString(5, dto.getGdsSell());
				pstmt.setString(6, dto.getCateCode());
				//이건 gdsNum은 못바꿀거로 예상
				pstmt.setInt(6, dto.getGdsNum());
				
				result = pstmt.executeUpdate();
				pstmt.close();
			
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;

	}
	
	
	
	
	//조회수 증가
		public int updateHitCount(int num) {
			
			int result=0;
			PreparedStatement pstmt=null;
			String sql;
			try {
				sql= "update kmGoods set gdsHitCount=gdsHitCount+1 ";
				sql+= "where gdsNum=?";
				
				pstmt =conn.prepareStatement(sql);
				
				pstmt.setInt(1, num);
				
				result = pstmt.executeUpdate();
				
				pstmt.close();
				
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return result;
			
		}
		
	
	
	
	
	
	
	//삭제
	public int deleteData(int gdsNum) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "delete kmGoods where gdsNum=?";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setInt(1, gdsNum);
			
			result = pstmt.executeUpdate();
			pstmt.close();
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
		
	}
	
	//전체 데이터갯수
		public int getDataCount(String searchKey, String searchValue, String category) {
			
			int result=0;
			
			PreparedStatement pstmt= null;
			ResultSet rs = null;
			String sql;
			try {
				
				searchValue = "%" + searchValue + "%";
				
				
				
				sql = "select nvl(count(*),0) from kmGoods ";
				sql += "where (gdsName like ? or gdsDes like ? or subCate like ?) and cateCode = ?";
				

				pstmt=conn.prepareStatement(sql);
				
				pstmt.setString(1, searchValue);
				pstmt.setString(2, searchValue);
				pstmt.setString(3, searchValue);
				pstmt.setString(4, category);
				
				rs=pstmt.executeQuery();
				
				if(rs.next()) {
					result = rs.getInt(1); //컬럼 1개 파생컬럼명 사용 x
				}
				
				rs.close();
				pstmt.close();
				
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return result;
			
		}

		
	
	
	
	
	
	
	
	
	
	
	
	
	
}

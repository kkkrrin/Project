package com.gesipan;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.ant.SessionsTask;

import com.join.CustomInfo;
import com.kmlogin.KmmemberDAO;
import com.kmlogin.KmmemberDTO;
import com.util.DBconn;
import com.util.MyUtil;

public class GesipanServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);		
	}

	protected void forward(HttpServletRequest req,
			HttpServletResponse resp,String url) throws ServletException, IOException {

		RequestDispatcher rd = 
				req.getRequestDispatcher(url);
		rd.forward(req, resp);	


	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String cp = req.getContextPath();	
		Connection conn = DBconn.getConnection();
		GesipanDAO dao = new GesipanDAO(conn);
		MyUtil myUtil = new MyUtil();

		String uri = req.getRequestURI();//study/gesi/createdgesi.do
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter writer = resp.getWriter();
		String url; //forward 주소
		

		if(uri.indexOf("createdgesi.do")!=-1) {

			HttpSession session = req.getSession();
			
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			if(info == null) {
				
				url = "/kmong/kmlogin/login.jsp";	
				forward(req, resp, url);
				return;
				
			}
			
			url="/kmong/kmlogin/createdgesi.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("createdgesi_ok.do")!=-1) {
			GesipanDTO dto = new GesipanDTO();
			
			int maxNum = dao.getMaxNum();
			
			dto.setNum(maxNum+1);
			dto.setUserID(req.getParameter("userID"));
			dto.setSubject(req.getParameter("subject"));		
			dto.setContent(req.getParameter("content"));
			dao.insertData(dto);
			
			/*HttpSession session = req.getSession();
			CustomInfo info = new CustomInfo();
			info.setUserID(dto.getUserID());
			session.setAttribute("customInfo1", info);*/
			
			url = cp + "/gesi/listgesi.do";
			resp.sendRedirect(url);	
		}else if(uri.indexOf("listgesi.do")!=-1) {
			
			HttpSession session = req.getSession();
			
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			if(info == null) {
				
				url = "/kmong/kmlogin/login.jsp";	
				forward(req, resp, url);
				return;
				
			}
			//넘어온 페이지 번호
			String pageNum = req.getParameter("pageNum");
			
			int currentPage = 1;
			
			if(pageNum!=null){
				currentPage = Integer.parseInt(pageNum);
			}
			
			//검색----------------------------
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			if(searchValue!=null){
				
				//넘어온 값이 GET방식이라면 인코딩,디코딩을 한다
				if(req.getMethod().equalsIgnoreCase("GET")){
					searchValue = URLDecoder.decode(searchValue, "UTF-8");
				}
				
			}else{
				searchKey = "subject";
				searchValue = "";
			}
			
			//검색----------------------------
			
			//전체 데이터의 갯수
			int dataCount = dao.getDataCount(searchKey,searchValue);
			
			//한 페이지에 표시할 데이터 갯수
			
			int numPerPage = 5;
			
			//전체 페이지의 갯수
			
			int totalPage = myUtil.getPageCount(numPerPage, dataCount);
			
			//전체페이지의 갯수가 삭제로 인해 현재페이지보다 작아질경우
			
			if(currentPage>totalPage){
				currentPage = totalPage;
			}
			
			//db에서 가져올 데이터의 시작과 끝
			int start  = (currentPage-1)*numPerPage+1;
			int end = currentPage*numPerPage;

			String checkId = info.getUserId();
			
			List<GesipanDTO> lists = dao.getLists(start,end,searchKey,searchValue);
			
			//페이징 처리
			String param = "";
			if(!searchValue.equals("")){
				param = "?searchKey=" + searchKey;
				param+= "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}
			String listUrl = cp + "/gesi/listgesi.do" + param;
			
			
			String pageIndexList = 
					myUtil.pageIndexList(currentPage, totalPage, listUrl);

			//글보기 주소 만들기
			String articleUrl = cp + "/gesi/articlegesi.do";
			
			if(param.equals("")){
				articleUrl += "?pageNum=" + currentPage;
			}else{
				articleUrl += param + "&pageNum=" + currentPage;
			}
			
			//포워딩 페이지에 넘길 데이터
			req.setAttribute("lists", lists);
			req.setAttribute("pageIndexList", pageIndexList);
			req.setAttribute("dataCount", dataCount);

			req.setAttribute("articleUrl", articleUrl);
			
			
			url="/kmong/kmlogin/listgesi.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("articlegesi.do")!=-1) {
			HttpSession session = req.getSession();
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			String str = info.getUserId();
			
			/*
			String userID = req.getParameter("userID");
			if(!str.equals(userID)) {
				System.out.println("아이디가 일치하지않습니다");
				return;
			}*/
			/*KmmemberDAO dao2;
			KmmemberDTO dto2 = dao2.getReadData(userId);*/
			
			
		/*	HttpSession session = req.getSession();
			CustomInfo info = new CustomInfo();
			String Id=info.getUserID();
			String ID=info.getUserId();
			if(!Id.equals(ID)||Id!=ID||ID==null) {
				System.out.println("권한이없습니다");
				return;
			}
			*/
			
			
			int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");
			
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			if(searchValue!=null) {
				searchValue = URLDecoder.decode(searchValue,"UTF-8");
			}
			
			dao.updateHitCount(num);
			GesipanDTO dto = dao.getReadData(num);
			
			if(dto==null) {
				
				url = cp + "/gesi/listgesi.do";
				resp.sendRedirect(url);
				
			}
			
			if(!str.equals(dto.getUserID())) {
				
				writer.println("<script>alert('접근권한이 없습니다.');location.href='javascript:history.back()';</script>");
				writer.close();
				return;
				
			}
			
			int lineSu = dto.getContent().split("\n").length;
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			String param = "pageNum=" + pageNum;
			
			if(searchValue!=null) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("params", param);
			req.setAttribute("lineSu", lineSu);
			req.setAttribute("pageNum", pageNum);
			
			url = "/kmong/kmlogin/articlegesi.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("updatedgesi.do")!=-1) {
			
			int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");

			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			if(searchValue!=null) {
				searchValue = URLDecoder.decode(searchValue,"UTF-8");
			}
			GesipanDTO dto = dao.getReadData(num);

			if(dto==null) {
				url = cp + "/gesi/listgesi.do";
				resp.sendRedirect(url);
			}
			String param = "pageNum=" + pageNum;
			if(searchValue!=null) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}
			req.setAttribute("dto", dto);
			req.setAttribute("pageNum", pageNum);
			req.setAttribute("params", param);
			req.setAttribute("searchKey", searchKey);
			req.setAttribute("searchValue", searchValue);

			url = "/kmong/kmlogin/updatedgesi.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("updatedgesi_ok.do")!=-1) {
			
			//int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");

			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			GesipanDTO dto = new GesipanDTO();
			
			//dto.setNum(num);
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setUserID(req.getParameter("userID"));
			dto.setSubject(req.getParameter("subject"));	
			dto.setContent(req.getParameter("content"));
			
			dao.updateData(dto);
			
			String param = "pageNum=" + pageNum;
			if(!searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}
			url = cp+"/gesi/listgesi.do?" + param;
			resp.sendRedirect(url);
			
		}else if(uri.indexOf("deletedgesi_ok.do")!=-1) {
			

			int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");

			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			dao.deleteData(num);
			
			String param = "pageNum=" + pageNum;
			if(searchValue!=null&&searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}
			url = cp+"/gesi/listgesi.do?" + param;
			resp.sendRedirect(url);

		}

	}

}

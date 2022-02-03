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
		String url; //forward �ּ�
		

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
			//�Ѿ�� ������ ��ȣ
			String pageNum = req.getParameter("pageNum");
			
			int currentPage = 1;
			
			if(pageNum!=null){
				currentPage = Integer.parseInt(pageNum);
			}
			
			//�˻�----------------------------
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			if(searchValue!=null){
				
				//�Ѿ�� ���� GET����̶�� ���ڵ�,���ڵ��� �Ѵ�
				if(req.getMethod().equalsIgnoreCase("GET")){
					searchValue = URLDecoder.decode(searchValue, "UTF-8");
				}
				
			}else{
				searchKey = "subject";
				searchValue = "";
			}
			
			//�˻�----------------------------
			
			//��ü �������� ����
			int dataCount = dao.getDataCount(searchKey,searchValue);
			
			//�� �������� ǥ���� ������ ����
			
			int numPerPage = 5;
			
			//��ü �������� ����
			
			int totalPage = myUtil.getPageCount(numPerPage, dataCount);
			
			//��ü�������� ������ ������ ���� �������������� �۾������
			
			if(currentPage>totalPage){
				currentPage = totalPage;
			}
			
			//db���� ������ �������� ���۰� ��
			int start  = (currentPage-1)*numPerPage+1;
			int end = currentPage*numPerPage;

			String checkId = info.getUserId();
			
			List<GesipanDTO> lists = dao.getLists(start,end,searchKey,searchValue);
			
			//����¡ ó��
			String param = "";
			if(!searchValue.equals("")){
				param = "?searchKey=" + searchKey;
				param+= "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}
			String listUrl = cp + "/gesi/listgesi.do" + param;
			
			
			String pageIndexList = 
					myUtil.pageIndexList(currentPage, totalPage, listUrl);

			//�ۺ��� �ּ� �����
			String articleUrl = cp + "/gesi/articlegesi.do";
			
			if(param.equals("")){
				articleUrl += "?pageNum=" + currentPage;
			}else{
				articleUrl += param + "&pageNum=" + currentPage;
			}
			
			//������ �������� �ѱ� ������
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
				System.out.println("���̵� ��ġ�����ʽ��ϴ�");
				return;
			}*/
			/*KmmemberDAO dao2;
			KmmemberDTO dto2 = dao2.getReadData(userId);*/
			
			
		/*	HttpSession session = req.getSession();
			CustomInfo info = new CustomInfo();
			String Id=info.getUserID();
			String ID=info.getUserId();
			if(!Id.equals(ID)||Id!=ID||ID==null) {
				System.out.println("�����̾����ϴ�");
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
				
				writer.println("<script>alert('���ٱ����� �����ϴ�.');location.href='javascript:history.back()';</script>");
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

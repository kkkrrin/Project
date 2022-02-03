package com.kmlogin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.join.CustomInfo;
import com.join.MemberDTO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.DBconn;
import com.util.MyUtil;

public class KmmemberServlet extends HttpServlet{


	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
	protected void forward(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
	
		RequestDispatcher rd = req.getRequestDispatcher(url);
		
		rd.forward(req, resp);
		
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String cp = req.getContextPath();
		
		resp.setContentType("text/html; charset=UTF-8"); 
		PrintWriter writer = resp.getWriter(); 
		
		Connection conn = DBconn.getConnection();
		
		KmmemberDAO dao = new KmmemberDAO(conn);
		
		MyUtil myUtil = new MyUtil();
		
		String uri = req.getRequestURI();
		
		String url;
		
		String root = getServletContext().getRealPath("/");
		String path = root + "pds" + File.separator + "saveimgFile";
		
		File f = new File(path);
		
		if(!f.exists()) {
			f.mkdirs();
			
		}
		
		
		if(uri.indexOf("created.do")!=-1) {
			
			url="/kmong/kmlogin/created.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("created_ok.do")!=-1) {
			
			KmmemberDTO dto= new KmmemberDTO();
			
			String userId=req.getParameter("userId");
			
			KmmemberDTO dto1=dao.getReadData(userId);
			
			if(dto1!=null) {
				writer.println("<script>alert('이미 존재하는 아이디입니다.'); location.href='"+ cp +"/kmoong/created.do';</script>");
				
				writer.close();
				return;
				
			}
			
			dto.setUserId(req.getParameter("userId"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setUserName(req.getParameter("userName"));
			dto.setUserTel(req.getParameter("userTel"));
			dto.setUserJob(req.getParameter("userJob"));
			dto.setSellUser(req.getParameter("sellUser"));
			dto.setUserFor(req.getParameter("userFor"));
			dao.insertData(dto);
			
			url= cp+"/kmong/kmlogin/login.jsp";
			resp.sendRedirect(url);
	
		}else if(uri.indexOf("login.do") != -1) {
			
			url = "/kmong/kmlogin/login.jsp";	
			forward(req, resp, url);

		}else if(uri.indexOf("login_ok.do") != -1) {
			
			String userId = req.getParameter("userId");
			String userPwd = req.getParameter("userPwd");
			
			KmmemberDTO dto = dao.getReadData(userId);
			
			if(dto == null || (!dto.getUserPwd().equals(userPwd))) {
				
				writer.println("<script>alert('아이디나 비밀번호가 틀렸습니다.'); location.href='"+ cp +"/kmoong/login.do';</script>");
				
				writer.close();
				return;
				
			}
			
			
			
			HttpSession session = req.getSession();
			
			CustomInfo info = new CustomInfo();
			
			info.setUserId(dto.getUserId());
			info.setUserName(dto.getUserName());
			session.setAttribute("customInfo", info);
			
			url = cp + "/kmoong/main.do";
			resp.sendRedirect(url);
			
			
		}else if(uri.indexOf("find.do") != -1) {
			
			url = "/kmong/kmlogin/findId.jsp";	
			forward(req, resp, url);
			
		}else if(uri.indexOf("find_Id.do") != -1) {
			
			String userName = req.getParameter("userName");
			String userTel = req.getParameter("userTel");
			
			KmmemberDTO dto = dao.getFindId(userName, userTel);
			
			if(dto == null) {
				
				writer.println("<script>alert('계정이 없습니다.'); location.href='"+ cp +"/kmoong/find.do';</script>");
				
				writer.close();
				
				return;
			}
			
			String userId = "";
			
			String senderEmail = "ildo5896@naver.com";
			
			String senderName = "이예린";
			String subject = "크몽아이디 찾기 결과";
			
			String receiverEmail = dto.getUserId();
			
			String content = "아이디 찾기 결과는 " + dto.getUserId() + " 입니다.";
			
			String host = "localhost";
			
			Properties props = System.getProperties();
			Session ssn = Session.getInstance(props, null);
			
			try{
				
				MimeMessage message = new MimeMessage(ssn);
				
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
				
				message.setFrom(new InternetAddress(senderEmail, senderName, "UTF-8"));
				
				message.setSubject(subject, "UTF-8");
				message.setContent(content, "text/plain;charset=UTF-8");
				Transport tp = ssn.getTransport("smtp");
				tp.connect(host, "", "");
				tp.sendMessage(message, message.getAllRecipients());
				tp.close();

			}catch(Exception e){
				
				System.out.print(e.toString());		
				
			}
			url = cp + "/kmoong/login.do";
			resp.sendRedirect(url);
			
		}else if(uri.indexOf("find_Pwd.do") != -1) {
			
			String userId = req.getParameter("userId");
			
			KmmemberDTO dto = dao.getReadData(userId);
			
			if(dto == null) {
				
				writer.println("<script>alert('없는 아이디 입니다.'); location.href='"+ cp +"/kmoong/find.do';</script>");
				writer.close();
				return;
			}

			String senderEmail = "ildo5896@naver.com";
			String senderName = "이예린";
			
			String subject = "크몽비밀번호 찾기 결과";
			
			
			String content = "귀하의 비밀번호는 " + dto.getUserPwd() + " 입니다.";
			
			String host = "localhost";
			
			Properties props = System.getProperties();
			Session ssn = Session.getInstance(props, null);
			
			try{
				
				MimeMessage message = new MimeMessage(ssn);
				
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(userId));
				
				message.setFrom(new InternetAddress(senderEmail, senderName, "UTF-8"));
				
				message.setSubject(subject, "UTF-8");
				message.setContent(content, "text/plain;charset=UTF-8");
				Transport tp = ssn.getTransport("smtp");
				tp.connect(host, "", "");
				tp.sendMessage(message, message.getAllRecipients());
				tp.close();

			}catch(Exception e){
				
				System.out.print(e.toString());		
				
			}
			url = cp + "/kmoong/login.do";
			resp.sendRedirect(url);
			
		}else if(uri.indexOf("myInfo.do") != -1) {
			
			HttpSession session = req.getSession();
			
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			if(info == null) {
				
				url = "/kmong/kmlogin/login.jsp";	
				forward(req, resp, url);
				return;
				
			}
			
			String userId = info.getUserId();
			
			KmmemberDTO dto = dao.getReadData(userId);

			String deletePath = cp + "/imgBoard/delete.do";
			String imagePath = cp + "/pds/saveimgFile";
			
			req.setAttribute("deletePath", deletePath);
			req.setAttribute("imagePath", imagePath);
			
			req.setAttribute("deletePath", deletePath);
			
			req.setAttribute("dto", dto);
			
			req.setAttribute("job", dto.getUserJob());
			
			url = "/kmong/kmlogin/myInfo.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("changeJob.do") != -1) {
			
			HttpSession session = req.getSession();
			
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			if(info == null) {
				
				url = "/kmong/kmlogin/login.jsp";	
				forward(req, resp, url);
				return;
				
			}
			String userJob = req.getParameter("selectJob");
			String userId = info.getUserId();
			dao.updatedJob(userJob, userId);
			
			url = cp + "/kmoong/myInfo.do";
			resp.sendRedirect(url);
			
		}else if(uri.indexOf("changePwd.do") != -1) {
			
			HttpSession session = req.getSession();
			
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			if(info == null) {
				
				url = "/kmong/kmlogin/login.jsp";	
				forward(req, resp, url);
				return;
				
			}
			
			url = "/kmong/kmlogin/changePwd.jsp";	
			forward(req, resp, url);
			
		}
		else if(uri.indexOf("changePwd_ok.do") != -1) {
			
			HttpSession session = req.getSession();
			
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			
			
			if(info == null) {
				
				url = "/kmong/kmlogin/login.jsp";	
				forward(req, resp, url);
				return;
				
			}
			
			
			
			String userPwd = req.getParameter("changePwd");
			String nowPwd = req.getParameter("nowPwd");
			String userId = info.getUserId();
			KmmemberDTO dto = dao.getReadData(userId);
			
			if(!nowPwd.equals(dto.getUserPwd())) {
				
				writer.println("<script>alert('현재 비밀번호가 틀렸습니다.'); location.href='javascript:history.back()';</script>");
				writer.close();
				return;
				
			}
			
			dao.updatedPwd(userPwd, userId);
			
			url = cp + "/kmoong/myInfo.do";
			resp.sendRedirect(url);
			
			
		}
		else if(uri.indexOf("deleteId.do") != -1) {
			
			HttpSession session = req.getSession();
			
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			if(info == null) {
				
				url = "/kmong/kmlogin/login.jsp";	
				forward(req, resp, url);
				return;
				
			}
			
			url = "/kmong/kmlogin/deleteId.jsp";	
			forward(req, resp, url);
			
		}else if(uri.indexOf("deleteId_ok.do") != -1) {
			
			HttpSession session = req.getSession();
			
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			if(info == null) {
				
				url = "/kmong/kmlogin/login.jsp";	
				forward(req, resp, url);
				return;
				
			}
			
			String checkId = req.getParameter("userId");
			String userId = info.getUserId();
			
			if(!userId.equals(checkId)) {
				
				writer.println("<script>alert('아이디가 틀렸습니다.'); location.href='javascript:history.back()';</script>");
				writer.close();
				return;
				
			}
			
			dao.deleteId(userId);
			
			url = cp + "/kmoong/login.do";
			resp.sendRedirect(url);
			
			
			
		}else if(uri.indexOf("changeproFile.do") != -1) {
			
			String encType = "utf-8";
			int maxSize = 10*1024*1024;
			
			MultipartRequest mr = new MultipartRequest(req, path, maxSize, encType, new DefaultFileRenamePolicy());
			
			HttpSession session = req.getSession();
			
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			if(info == null) {
				
				url = "/kmong/kmlogin/login.jsp";	
				forward(req, resp, url);
				return;
				
			}
			
			String userId = info.getUserId();
			
			String fileName = mr.getFilesystemName("upload");
			
			dao.updatedproFileImg(fileName , userId);
			
			url = cp + "/kmoong/myInfo.do";
			resp.sendRedirect(url);
			
		}		
		else if(uri.indexOf("logout.do") != -1) {
			
			HttpSession session = req.getSession();
			
			session.removeAttribute("customInfo");
			
			url = cp + "/kmoong/main.do";
			resp.sendRedirect(url);
			
			
		}else if(uri.indexOf("main.do") != -1) {
			
			url = "/kmong/kmmain/kmmain.jsp";	
			forward(req, resp, url);
			
		}
		
		
		
	}
	
	
	

}

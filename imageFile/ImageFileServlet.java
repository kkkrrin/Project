package com.imageFile;

import java.io.File;
import java.io.IOException;
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

import com.join.CustomInfo;
import com.kmlogin.KmmemberDAO;
import com.kmlogin.KmmemberDTO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.DBconn;
import com.util.FileManager;
import com.util.MyUtil;

public class ImageFileServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);		
	}
	
	protected void forward(HttpServletRequest req, HttpServletResponse resp,String url) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(url);
		rd.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = DBconn.getConnection();
		ImageFileDAO dao = new ImageFileDAO(conn);
		MyUtil myUtil = new MyUtil();

		req.setCharacterEncoding("UTF-8");
		String cp = req.getContextPath();
		String uri = req.getRequestURI();
		String url;
		
		String root = getServletContext().getRealPath("/");
		String path = root + "pds" + File.separator + "imageFileTest";

		File f = new File(path);

		if(!f.exists()) {
			f.mkdirs();
		}
		
		if(uri.indexOf("write.do")!=-1) {
			url= "/kmong/kmlogin/write.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("write_ok.do")!=-1) {
			
			String encType = "UTF-8";
			int maxSize = 10*1024*1024;

			MultipartRequest mr =
					new MultipartRequest(req, path, maxSize,encType,
							new DefaultFileRenamePolicy());
			if(mr.getFile("upload")!=null) {

				ImageFileDTO dto = new ImageFileDTO();
				int maxNum = dao.getMaxNum();
				dto.setNum(maxNum + 1);
				dto.setSubject(mr.getParameter("subject"));
				dto.setSaveFileName(mr.getFilesystemName("upload"));

				dao.insertData(dto);
			}

			url = cp + "/images/list2.do";
			resp.sendRedirect(url);
			
		}else if(uri.indexOf("list2.do")!=-1) {
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

			int numPerPage = 9;

			//전체 페이지의 갯수

			int totalPage = myUtil.getPageCount(numPerPage, dataCount);

			//전체페이지의 갯수가 삭제로 인해 현재페이지보다 작아질경우

			if(currentPage>totalPage){
				currentPage = totalPage;
			}

			//db에서 가져올 데이터의 시작과 끝
			int start  = (currentPage-1)*numPerPage+1;
			int end = currentPage*numPerPage;
			
			List<ImageFileDTO> lists = dao.getList(start,end,searchKey,searchValue);
			//페이징 처리
			String param = "";
			if(!searchValue.equals("")){
				param = "?searchKey=" + searchKey;
				param+= "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}
			
			HttpSession session = req.getSession();
			
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			if(info == null) {
				
				url = "/kmong/kmlogin/login.jsp";	
				forward(req, resp, url);
				return;
				
			}
			
			KmmemberDAO dao2 = new KmmemberDAO(conn);
			
			String userId = info.getUserId();
			
			KmmemberDTO dto2 = dao2.getReadData(userId);

			String deletePath2 = cp + "/imgBoard/delete.do";
			String imagePath2 = cp + "/pds/saveimgFile";
			
			req.setAttribute("deletePath2", deletePath2);
			req.setAttribute("imagePath2", imagePath2);
			req.setAttribute("dto2", dto2);
			
			
			String listUrl = cp + "/images/list2.do" + param;
			
			String pageIndexList = 
					myUtil.pageIndexList(currentPage, totalPage, listUrl);

			String deletePath = cp + "/images/delete.do";
			String imagePath = cp + "/pds/imageFileTest";
			
			dataCount = dao.getDataCount(searchKey,searchValue);
			req.setAttribute("imagePath", imagePath);
			req.setAttribute("lists", lists);
			req.setAttribute("pageNum", pageNum);
			req.setAttribute("currentPage", currentPage);
			req.setAttribute("pageIndexList", pageIndexList);
			req.setAttribute("deletePath", deletePath);
			req.setAttribute("pageIndexList", pageIndexList);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("totalPage", totalPage);

			url = "/kmong/kmlogin/list2.jsp";
				forward(req, resp, url);
				
		}else if(uri.indexOf("deleted.do")!=-1) {
			int num = Integer.parseInt(req.getParameter("num"));
			int pageNum = Integer.parseInt(req.getParameter("pageNum"));

			ImageFileDTO dto =dao.getReadData(num);


			//물리적 파일 삭제
			FileManager.doFileDelete(dto.getSaveFileName(), path);

			//테이블정보 삭제
			dao.deleteData(num);

			url = cp + "/images/list2.do?pageNum=" + pageNum;
			resp.sendRedirect(url);

		}
	}
}

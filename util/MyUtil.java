package com.util;

public class MyUtil {
	
	//전체페이지수
	//numPerPage => 한페이지에 보일 수 dataCount => 전체 데이타 수
	public int getPageCount(int numPerPage, int dataCount) {
	
		int pageCount = 0;
		
		pageCount = dataCount / numPerPage;
		
		if(dataCount % numPerPage !=0) {
			
			pageCount++;
			
		}
		
		return pageCount;
		
	}
	
	//페이지처리
	
	public String pageIndexList(int currentPage, int totalPage, String listUrl) {
		
		int numPerBlock = 5; //◀ 이전  6  7  8  9  10   ▶ 다음
		int currentPageSetup; // ◀ 이전(표시한 6 - 1인 5의 값)
		int page; // 6  7  8  9  10 for문의 i같은 역할
		
		StringBuffer sb = new StringBuffer();		
				
		if(currentPage == 0 || totalPage == 0) {
			
			return "";
			
		}
		
		//검색x : list.jsp
		//검색o : list.jsp?searchKey=subject&searchValue=3
		
		if(listUrl.indexOf("?") != -1) {
			
			listUrl = listUrl + "&";
			
		}else {
			
			listUrl = listUrl + "?";
			
		}
		
		//표시할 첫 페이지의 -1 해준값
		currentPageSetup = (currentPage/numPerBlock) * numPerBlock;
		
		if(currentPage % numPerBlock == 0) {
			
			currentPageSetup = currentPageSetup - numPerBlock;
			
		}
		
		// ◀ 이전 버튼
		if(totalPage > numPerBlock && currentPageSetup >0) {
			
			sb.append("<a href=\"" + listUrl + "pageNum=" + currentPageSetup + "\">◀ 이전</a>&nbsp;");
			
		}
		
		//바로가기 페이지
		page = currentPageSetup + 1;
		
		while(page <= totalPage && page <= (currentPageSetup + numPerBlock)) {
			
			if(page==currentPage) {
				
				sb.append("<font color=\"fuchsia\">" + page + "</font>&nbsp;");
				//<font color = "Fuchsia">6</font>&nbsp;				
				
			}else{
				
				sb.append("<a href=\"" + listUrl + "pageNum=" + page + "\">" + page + "</a>&nbsp;");
				
				//<a href="list.jsp?pageNum=7">7</a>&nbsp;
				
			}
			
			page++;
			
			
		
			
		}
		
		//▶다음
		if(totalPage-currentPageSetup>numPerBlock) {
			
			sb.append("<a href=\"" + listUrl + "pageNum=" + page + "\">다음▶</a>&nbsp;");
			
			//<a href="list.jsp?pageNum=11">▶다음</a>&nbsp;
			
		}	
		
		return sb.toString();
		
	}public String pageIndexList(int currentPage, int totalPage, String listUrl, String ctgNum) {
		
		int numPerBlock = 5; //◀ 이전  6  7  8  9  10   ▶ 다음
		int currentPageSetup; // ◀ 이전(표시한 6 - 1인 5의 값)
		int page; // 6  7  8  9  10 for문의 i같은 역할
		
		StringBuffer sb = new StringBuffer();		
				
		if(currentPage == 0 || totalPage == 0) {
			
			return "";
			
		}
		
		//검색x : list.jsp
		//검색o : list.jsp?searchKey=subject&searchValue=3
		
		if(listUrl.indexOf("?") != -1) {
			
			listUrl = listUrl + "&";
			
		}else {
			
			listUrl = listUrl + "?";
			
		}
		
		//표시할 첫 페이지의 -1 해준값
		currentPageSetup = (currentPage/numPerBlock) * numPerBlock;
		
		if(currentPage % numPerBlock == 0) {
			
			currentPageSetup = currentPageSetup - numPerBlock;
			
		}
		
		// ◀ 이전 버튼
		if(totalPage > numPerBlock && currentPageSetup >0) {
			
			sb.append("<a href=\"" + listUrl + "ctgNum=" + ctgNum + "&pageNum=" + currentPageSetup + "\">◀ 이전</a>&nbsp;");
			
		}
		
		//바로가기 페이지
		page = currentPageSetup + 1;
		
		while(page <= totalPage && page <= (currentPageSetup + numPerBlock)) {
			
			if(page==currentPage) {
				
				sb.append("<font color=\"fuchsia\">" + page + "</font>&nbsp;");
				//<font color = "Fuchsia">6</font>&nbsp;				
				
			}else{
				
				sb.append("<a href=\"" + listUrl  +  "ctgNum=" + ctgNum + "&pageNum=" + page + "\">" + page + "</a>&nbsp;");
				
				//<a href="list.jsp?pageNum=7">7</a>&nbsp;
				
			}
			
			page++;
			
		}
		
		//▶다음
		if(totalPage-currentPageSetup>numPerBlock) {
			
			sb.append("<a href=\"" + listUrl +  "ctgNum=" + ctgNum + "&pageNum=" + page + "\">다음▶</a>&nbsp;");
			
			//<a href="list.jsp?pageNum=11">▶다음</a>&nbsp;
			
		}	
		
		return sb.toString();
		
	}
	
		

}

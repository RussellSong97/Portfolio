package action;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class TestProcAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("utf-8");
		

		TestInfo testInfo = new TestInfo();
		int result = 0;
 
		testInfo.setB_writer(request.getParameter("b_writer").trim());
		testInfo.setB_title(request.getParameter("b_title").trim());
		testInfo.setB_content(request.getParameter("b_content").trim());

		TestProcSvc testProcSvc = new TestProcSvc();
		
		if (request.getParameter("wtype").equals("up")) {
			int idx = Integer.parseInt(request.getParameter("b_idx"));
			testInfo.setB_idx(idx);
			result = testProcSvc.testProcUpdate(testInfo);
		}  else {
			result = testProcSvc.testProc(testInfo); 			
		}

		ActionForward forward = new ActionForward();
		forward.setRedirect(true); // dispatch가 아닌 sendRedirect 방식으로 이동
		forward.setPath("test_list.brd");

		return forward;
	}
}

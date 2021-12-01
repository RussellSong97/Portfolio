package action;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class TestFormAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
 

		request.setCharacterEncoding("utf-8");
 
		TestInfo testInfo = new TestInfo(); 
		String wtype ="in";
		TestFormSvc testFormSvc = new TestFormSvc();
		
		if (request.getParameter("b_idx") != null) {
			int idx = Integer.parseInt(request.getParameter("b_idx"));
			testInfo = testFormSvc.getArticle(idx); 	
			wtype ="up";
		}  else {
			testInfo.setB_idx(0);
			testInfo.setB_title("");
			testInfo.setB_writer("");
			testInfo.setB_content("");
		}

		ActionForward forward = new ActionForward(); 
		request.setAttribute("testInfo", testInfo); 
		request.setAttribute("wtype", wtype); 
		forward.setPath("test_form.jsp");

		return forward;
	}
}



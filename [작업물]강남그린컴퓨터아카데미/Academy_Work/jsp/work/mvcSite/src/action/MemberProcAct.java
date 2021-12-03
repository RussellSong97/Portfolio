package action;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class MemberProcAct implements Action {
// 회원관련 작업(가입, 수정, 탈퇴)을 처리하는 클래스
   public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
      request.setCharacterEncoding("utf-8");
      String wtype = request.getParameter("wtype");   // 가입, 수정, 탈퇴 여부를 구분짓는 값
      MemberInfo memberInfo = null;
      HttpSession session = request.getSession();
      // JSp가 아니므로 session 인스턴스를 직접 생성해야 함
      MemberInfo tmpMember = (MemberInfo)session.getAttribute("loginMember");
      
      if (wtype.equals("in") || wtype.equals("up")) {
      // 현재 처리하는 작업이 회원가입이나 정보 수정일 경우
      // (사용자가 입력/수정한 데이터들을 받아와야 함)
         
         memberInfo = new MemberInfo();      
         // 입력 또는 수정할 회원정보들을 담을 인스턴스생성
         // 많은 컬럼을 하나로 만들어서 들고 다니기 위해..         
         // 받아와서 바로 담음..
         if (wtype.equals("in")) {   // 아이디는 가입일때만 회원에게 입력받음 
            memberInfo.setMi_name(request.getParameter("mi_name").trim());         
            memberInfo.setMi_birth(request.getParameter("by") + "-" + request.getParameter("bm") + "-" +  request.getParameter("bd"));         
            memberInfo.setMi_gender(request.getParameter("mi_gender"));   
            // 수정 시에는 받아오지 않는 가입전용 데이터들
            
            
            memberInfo.setMi_id(request.getParameter("mi_id").trim().toLowerCase());
            memberInfo.setMa_zip(request.getParameter("ma_zip"));  
            memberInfo.setMa_addr1(request.getParameter("ma_addr1"));       
            memberInfo.setMa_addr2(request.getParameter("ma_addr2").trim()); 
            // 회원 가입시 기본주소에 대한 정보도 같이 입력받음(수정일때는 안됨)

         } else {
            memberInfo.setMi_id(tmpMember.getMi_id());
            // 정보 수정일 경우 로그인 되어 있는  인스턴스에서 회원 ID정보를 가져옴
            // 정보 수정에서는 주소를 변경하지 않고 주소록 관리에서 주소 수정을 처리함
            
            // 정보 수정일 경우 입력받은 이전 비밀번호를 비교함
            if (!tmpMember.getMi_pwd().equals(request.getParameter("old_pwd"))) {
               // 아닐경우 뒤로 팅겨내기
               response.setContentType("text/html; charset=UTF-8");
               PrintWriter out = response.getWriter();
               out.println("<script>");
               out.println("alert('이전 비밀번호가 틀렸습니다.');");
               out.println("history.back();");
               out.println("</script>");
               out.close();
            } 
         }
         
         memberInfo.setMi_pwd(request.getParameter("mi_pwd").trim());                
         memberInfo.setMi_phone(request.getParameter("p1") + "-" + request.getParameter("p2").trim().trim() + "-" +  request.getParameter("p3").trim());         
         memberInfo.setMi_email(request.getParameter("e1").trim() + "@" + request.getParameter("e3").trim());         
         memberInfo.setMi_issns(request.getParameter("mi_issns"));          
         memberInfo.setMi_ismail(request.getParameter("mi_ismail"));   
         // 사용자가 입력한 데이터들을 MemberInfo형 인스턴스에 저장
      }
      
      
      MemberProcSvc memberProcSvc = new MemberProcSvc();
      int result = 0;
      String lnk = "/index.jsp";
      switch(wtype) {
         case "in" :         // 회원 가입일 경우
            result = memberProcSvc.memberProc(memberInfo, wtype);
            lnk = "../login_form.jsp";
            break;
         case "up" :         // 회원 정보수정일 경우
            result = memberProcSvc.memberProc(memberInfo, wtype);
            lnk = "mypage.mem";
            if (!memberInfo.getMi_pwd().equals("")) {
            	// 비밀번호를 변경할 경우 기존의 로그인 정보에 있는 비밀번호를 새 비밀번호로 변경 
               tmpMember.setMi_pwd(memberInfo.getMi_pwd());               
            }
            tmpMember.setMi_phone(memberInfo.getMi_phone());
            tmpMember.setMi_email(memberInfo.getMi_email());
            tmpMember.setMi_issns(memberInfo.getMi_issns());
            tmpMember.setMi_ismail(memberInfo.getMi_ismail());
            break;
         case "del" :      // 회원 탈퇴일 경우
            result = memberProcSvc.memberDelete(tmpMember.getMi_id());
            lnk = "../logout.jsp";
            break;
      }
      
      ActionForward forward = new ActionForward();

      forward.setRedirect(true);   // dispatch가 아닌 sendRedirect 방식으로 이동
      forward.setPath(lnk);
      
      return forward;
   }

}
package action;

import javax.servlet.http.*;
import vo.*;

public interface Action {
//여러 요청을 처리한 후 이동할 때 동일한 타입으로 이동하게 하기 위한 인터페이스
   ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
   //각 요청을 처리하는 Action클래스들이 공통적으로 구현해야 하는 메소드로서 선언된 메소드
}
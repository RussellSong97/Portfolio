package com.cuv.admin.web.controller.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 커스텀 에러 컨트롤러
 */
@Controller
public class CustomErrorController implements ErrorController {

    @Value("${cuv.service-url}")
    private String serviceUrl;

    /**
     * 커스텀 에러
     *
     * @author SungHa
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("statusCode", statusCode);
            model.addAttribute("serviceUrl", serviceUrl);
        }

        return "error/error";
    }
}

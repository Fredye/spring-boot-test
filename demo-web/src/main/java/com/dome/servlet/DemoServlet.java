package com.dome.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dome.help.ServiceHelp;
import com.dome.help.ServiceHelpImpl;

public class DemoServlet extends HttpServlet{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String addressData="";
    private static Logger logger = LoggerFactory
            .getLogger(DemoServlet.class);
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
        String serviceName=request.getHeader("Service-Name");
        ServiceHelp help=new ServiceHelpImpl("127.0.0.1:2181");
        addressData=help.getData(serviceName);
        } catch (Exception e) {
            logger.error("get address node data error",e);
        }

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(addressData);
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

}
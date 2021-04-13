package com.nivelle.spring.springmvc.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义Servlet
 *
 * @author nivelle
 * @date 2020/01/15
 */
public class MyServlet1 extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse httpServletResponse) throws IOException {
        String desc = getServletConfig().getInitParameter("desc");
        httpServletResponse.getOutputStream().println("desc is " + desc);
    }

}

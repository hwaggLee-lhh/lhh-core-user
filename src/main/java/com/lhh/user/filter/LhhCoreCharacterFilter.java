package com.lhh.user.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.lhh.core.base.LhhFilter;
  
/**
 * 字符串处理过滤
 * @author hwaggLee
 * @createDate 2016年12月19日
 */
public class LhhCoreCharacterFilter extends LhhFilter {
    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        
        req.setCharacterEncoding("UTF-8");
//        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=UTF-8");

//        request.setAttribute("base", request.getContextPath());
        chain.doFilter(req, res);
        
//        logger.info("################################");//////////////////////////////////////
//        logger.info("getContextPath:" + request.getContextPath());
//        logger.info("getPathTranslated:" + request.getPathTranslated());
//        logger.info("getQueryString:" + request.getQueryString());
//        logger.info("getRequestURL:" + request.getRequestURL());
//        logger.info("getServletPath:" + request.getServletPath());
//        logger.info("getRequestURI:" + request.getRequestURI());
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}

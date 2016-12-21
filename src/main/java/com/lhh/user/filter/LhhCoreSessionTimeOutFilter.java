package com.lhh.user.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

/**
 * session超时过滤
 * @author hwaggLee
 * @createDate 2016年12月19日
 */
public class LhhCoreSessionTimeOutFilter implements Filter {
    public static final Logger log = Logger.getLogger(LhhCoreSessionTimeOutFilter.class);
    
    /**
     * 返回的URL路径
     */
    protected String backUrl;
    /**
     * 不过滤的URL路径
     */
    protected String obviateUrl;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse)res;
        HttpSession session = httpRequest.getSession();
        log.debug(""+httpRequest.getRequestURI());
        Assert.notNull(backUrl, "backUrl is null");
        if (session != null) {
        	String username = (String)session.getAttribute("username");
            String contextPath = httpRequest.getContextPath();
            
            boolean isFilter = true;
            if (StringUtils.isNotBlank(obviateUrl)) {
                String[] obviateArray = obviateUrl.split(",");
                for (int i = 0; i < obviateArray.length; i++) {
                	if(obviateArray[i].contains("*")){
                		if(httpRequest.getRequestURI().startsWith(contextPath + obviateArray[i].replace("*", ""))) {
                			//当前访问路径为排序过滤的URL
                            isFilter = false;
                            break;
                		}
                	}else{
                		if(httpRequest.getRequestURI().equals(contextPath + obviateArray[i])) {
                            //当前访问路径为排序过滤的URL
                            isFilter = false;
                            break;
                        }
                	}
                }
            }
            if (isFilter) {
                if (StringUtils.isBlank(username)) {
                    String targetUrl = httpRequest.getContextPath() + backUrl;
                    httpResponse.sendRedirect(httpResponse.encodeRedirectURL(targetUrl));
                    return;
                }
            }
        }
        filterChain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        obviateUrl = filterConfig.getInitParameter("obviateUrl");
        backUrl = filterConfig.getInitParameter("backUrl");
    }

    public void destroy() {
        
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getObviateUrl() {
        return obviateUrl;
    }

    public void setObviateUrl(String obviateUrl) {
        this.obviateUrl = obviateUrl;
    }

}

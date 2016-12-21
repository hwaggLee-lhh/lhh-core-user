package com.lhh.user.core.tag;

import com.lhh.core.base.LhhCoreBaseTag;
import com.lhh.user.security.LhhCoreAuthenticationUtil;

/**
 * 自定义标签
 * url参数必填
 * 判断是否有权限访问某URL
 * @author hwaggLee
 * @createDate 2016年12月19日
 */
public class HasRightToDoTag extends LhhCoreBaseTag {
	private static final long serialVersionUID = 1L;
	private static final String URL_DIVIDER = ",";
    protected String alt = "";
    private String url;
    private LhhCoreAuthenticationUtil authenticationUtil;

    public int doStartTag() {
        if (authenticationUtil == null) {
            authenticationUtil = (LhhCoreAuthenticationUtil) super.getWebApplicationContext().getBean("authenticationUtil");
        }
        if (url != null) {
            String[] dd = url.split(URL_DIVIDER);
            for (int i = 0; i < dd.length; i++) {
                if (authenticationUtil.isAccessableTo(dd[i])) {
                    alt = "";
                    return EVAL_BODY_INCLUDE;
                }
            }
        }
        return SKIP_BODY;
    }

    public int doEndTag() {
        pageContextWrite(pageContext, alt);
        return EVAL_PAGE;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public void setAuthenticationUtil(LhhCoreAuthenticationUtil authenticationUtil) {
		this.authenticationUtil = authenticationUtil;
	}
}

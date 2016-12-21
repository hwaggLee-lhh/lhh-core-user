package com.lhh.user.filter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.lhh.core.exception.LhhCoreException;
import com.lhh.user.LhhUser;
import com.lhh.user.security.LhhCoreSecurityCacheManager;

/**
 * 重载SECURITY3的UsernamePasswordAuthenticationFilter的attemptAuthentication,
 * obtainUsername,obtainPassword方法(完善逻辑) 增加验证码校验模块 添加验证码属性 添加验证码功能开关属性
 * @author hwaggLee
 * @createDate 2016年12月15日
 */
public class LhhCoreUsernamePasswordAuthenticationExtendFilter extends UsernamePasswordAuthenticationFilter {
	@SuppressWarnings("deprecation")
	public LhhCoreUsernamePasswordAuthenticationExtendFilter() {
		super.setRememberMeServices(new TokenBasedRememberMeServices());
	}
	// 验证码字段
	private String validateCodeParameter = LhhUser.systemName+"ValidateCode";
	// 是否开启验证码功能
	private boolean openValidateCode = true;
	@Resource
	private LhhCoreSecurityCacheManager securityCacheManager;
	
	public void setFilterProcessesUrl(String filterProcessesUrl) {
		super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(filterProcessesUrl));
	}
	public void setTargetUrlParameter(String targetUrlParameter) {
		AuthenticationSuccessHandler successHandler = super.getSuccessHandler();
		if(successHandler instanceof AbstractAuthenticationTargetUrlRequestHandler) {
			AbstractAuthenticationTargetUrlRequestHandler targetUrlHandler = (AbstractAuthenticationTargetUrlRequestHandler)successHandler;
			targetUrlHandler.setTargetUrlParameter(targetUrlParameter);
		}
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		// 开启验证码功能的情况
		if (isOpenValidateCode()) checkValidateCode(request);

//		return super.attemptAuthentication(request, response);
		
		try {
			return super.attemptAuthentication(request, response);
		} catch(BadCredentialsException e) {
			throw new LhhCoreException(LhhCoreException.MSG_ERROR_USERNAME_OR_PWD);
		} catch(DisabledException e) { //未激活用户
			throw new LhhCoreException(LhhCoreException.MSG_FREEZE_USER);
		} catch (UsernameNotFoundException e) {
			throw new LhhCoreException(e.getMessage());
		} 
	}

	// 匹对验证码的正确性
	private void checkValidateCode(HttpServletRequest request) {
		String jcaptchaCode = obtainValidateCodeParameter(request);
		String validateCodeInSession = (String) request.getSession().getAttribute(validateCodeParameter);
		if (null == validateCodeInSession || "" == validateCodeInSession)
			throw new LhhCoreException(LhhCoreException.MSG_TIMEOUT);
		if (null == jcaptchaCode || ""==jcaptchaCode)
			throw new LhhCoreException(LhhCoreException.MSG_NOTNULL);
		boolean b = jcaptchaCode.toLowerCase().equals(validateCodeInSession.toLowerCase());
		if (!b) {
			throw new LhhCoreException(LhhCoreException.MSG_ERROR);
		}
	}

	private String obtainValidateCodeParameter(HttpServletRequest request) {
		Object obj = request.getParameter(getValidateCodeParameter());
		return null == obj ? null : obj.toString().trim();
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		Object obj = request.getParameter(getUsernameParameter());
		return null == obj ? "" : obj.toString().trim();
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		Object obj = request.getParameter(getPasswordParameter());
		return null == obj ? "" : obj.toString().trim();
	}

	public String getValidateCodeParameter() {
		return validateCodeParameter;
	}

	/**
	 * 不建议set，否则当被其他项目引用时无法区分为那个项目设定
	 * @param validateCodeParameter
	 */
	@Deprecated
	public void setValidateCodeParameter(String validateCodeParameter) {
		this.validateCodeParameter = validateCodeParameter;
	}

	public boolean isOpenValidateCode() {
		return openValidateCode;
	}

	public void setOpenValidateCode(boolean openValidateCode) {
		this.openValidateCode = openValidateCode;
	}

}
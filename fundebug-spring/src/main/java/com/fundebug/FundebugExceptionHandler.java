package com.fundebug;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 将错误上报到Fundebug
 *
 * 使用Order保证FundebugExceptionHandler先与其他HandlerExceptionResolver执行
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class FundebugExceptionHandler implements HandlerExceptionResolver {

	private final Fundebug fundebug;

	FundebugExceptionHandler(final Fundebug fundebug) {
		this.fundebug = fundebug;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			java.lang.Exception ex) {

		System.out.println("Hello, Fundebug!");
		ex.printStackTrace();

		fundebug.notifyError(ex);

		// 返回null，Exception交给下一个HandlerExceptionResolver处理
		return null;
	}
}

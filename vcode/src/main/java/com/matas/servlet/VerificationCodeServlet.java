package com.matas.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.matas.code.VerificationCode;

/**
 * @filename VerificationCodeServlet.java
 *
 * @desc 系统验证码servlet
 *
 * @author liuliang
 *
 * @date 2015年1月5日 上午10:51:03
 *
 */

public class VerificationCodeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public VerificationCodeServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		VerificationCode verificationCode = new VerificationCode();

		// 将验证码保存到session
		HttpSession session = request.getSession();
		// SessionUtil.putMapValue(session, "verificationCode",
		// verificationCode.getCode());
		session.setAttribute("verificationCode", verificationCode.getCode());

		// Captcha captcha = new SpecCaptcha(140,40,4);

		verificationCode.write(response.getOutputStream());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}

package com.matas.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

public class CalcKaptchaServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = -3521311694146568675L;
	private Properties props;
	private Producer kaptchaProducer;
	private String sessionKeyValue;

	public CalcKaptchaServlet() {
		this.props = new Properties();

		this.kaptchaProducer = null;

		this.sessionKeyValue = null;
	}

	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);

		ImageIO.setUseCache(false);

		Enumeration<String> initParams = conf.getInitParameterNames();
		while (initParams.hasMoreElements()) {
			String key = initParams.nextElement();
			String value = conf.getInitParameter(key);
			this.props.put(key, value);
		}

		Config config = new Config(this.props);
		this.kaptchaProducer = config.getProducerImpl();
		this.sessionKeyValue = config.getSessionKey();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setDateHeader("Expires", 0L);

		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

		resp.addHeader("Cache-Control", "post-check=0, pre-check=0");

		resp.setHeader("Pragma", "no-cache");

		resp.setContentType("image/jpeg");

		Random random = new Random();
		String s1 = random.nextInt(100) + "";
		String s2 = random.nextInt(100) + "";
		int r = Integer.valueOf(s1).intValue() + Integer.valueOf(s2).intValue();

		req.getSession().setAttribute(this.sessionKeyValue, String.valueOf(r));

		BufferedImage bi = this.kaptchaProducer.createImage(s1 + "+" + s2 + "=?");

		ServletOutputStream out = resp.getOutputStream();

		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
	}
}
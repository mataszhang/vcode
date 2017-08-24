package com.matas.code;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomUtils;

/**
 * 混淆线，字体旋转
 * 
 * @author Matas
 *
 */
public class VerificationCode {
	/**
	 * 图片的宽度
	 */
	private int width = 120;

	/**
	 * 图片的高度
	 */
	private int height = 40;

	/**
	 * 验证码字符个数
	 */
	private int codeCount = 4;

	/**
	 * 验证码干扰线数
	 */
	private int lineCount = 1;
	/**
	 * 噪点数量
	 */
	private int dotCount = 40;

	/**
	 * 验证码
	 */
	private String code = null;

	/**
	 * 验证码图片Buffer
	 */
	private BufferedImage buffImg = null;

	private char[] codeSequence = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };

	public VerificationCode() {
		this.createCode();
	}

	/**
	 * 
	 * @param width
	 *            图片宽
	 * @param height
	 *            图片高
	 */
	public VerificationCode(int width, int height) {
		this.width = width;
		this.height = height;
		this.createCode();
	}

	/**
	 * 
	 * @param width
	 *            图片宽
	 * @param height
	 *            图片高
	 * @param codeCount
	 *            字符个数
	 * @param lineCount
	 *            干扰线条数
	 */
	public VerificationCode(int width, int height, int codeCount, int lineCount) {
		this.width = width;
		this.height = height;
		this.codeCount = codeCount;
		this.lineCount = lineCount;
		this.createCode();
	}

	public void createCode() {
		int x = 0, codeY = 0;
		int red = 0, green = 0, blue = 0;
		x = width / (codeCount + 1);// 每个字符的宽度
		codeY = height - 4;

		// 图像buffer
		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();
		// 生成随机数
		Random random = new Random();
		// 将图像填充为白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		// 创建字体
		Font font = new Font("TimesRoman", Font.BOLD + Font.ITALIC, 42);
		g.setFont(font);
		// 抗锯齿 高清显示
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);

		for (int i = 0; i < lineCount; i++) {
			int lineStroke = 3 + random.nextInt(2); // 线的粗细
			int x1 = RandomUtils.nextInt(width / 10, width / 5);
			int y1 = RandomUtils.nextInt(height / 4, height);
			int x2 = width - RandomUtils.nextInt(width / 15, width / 4);
			int y2 = height - RandomUtils.nextInt(height / 20, height / 2);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			g.setStroke(new BasicStroke(lineStroke));
			g.drawLine(x1, y1, x2, y2);
		}

		// 画噪点
		for (int i = 0; i < dotCount; i++) {
			float dotStroke = 1 + RandomUtils.nextFloat(0.5f, 2f);
			int x1 = RandomUtils.nextInt(0, width - 1);
			int y1 = RandomUtils.nextInt(0, height - 1);
			int x2 = x1 + RandomUtils.nextInt(1, 8);
			int y2 = y1 + RandomUtils.nextInt(1, 7);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			g.setStroke(new BasicStroke(dotStroke));
			g.drawLine(x1, y1, x2, y2);
		}

		// randomCode记录随机产生的验证码
		StringBuffer randomCode = new StringBuffer();
		// 随机产生codeCount个字符的验证码。
		for (int i = 0; i < codeCount; i++) {
			String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			// 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
			red = random.nextInt(200);
			green = random.nextInt(200);
			blue = random.nextInt(200);
			g.setColor(new Color(red, green, blue));
			// 旋转
			double rotate = RandomUtils.nextDouble(0.5, 8);
			if (rotate > 5) {
				rotate = -rotate;
			}
			g.rotate(rotate * Math.PI / 180); // 转对应度
			g.drawString(strRand, i * x + 5, codeY);
			g.rotate(-rotate * Math.PI / 180);// 转回来
			// 将产生的四个随机数组合在一起。
			randomCode.append(strRand);
		}
		// 将四位数字的验证码保存到Session中。
		code = randomCode.toString();
	}

	public void write(String path) throws IOException {
		OutputStream sos = new FileOutputStream(path);
		this.write(sos);
	}

	public void write(OutputStream sos) throws IOException {
		ImageIO.write(buffImg, "png", sos);
		sos.close();
	}

	public BufferedImage getBuffImg() {
		return buffImg;
	}

	public String getCode() {
		return code;
	}
}
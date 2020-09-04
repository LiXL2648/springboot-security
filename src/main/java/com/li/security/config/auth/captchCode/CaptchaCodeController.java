package com.li.security.config.auth.captchCode;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.li.security.config.auth.captchCode.CaptchaCodeVo;
import com.li.security.config.auth.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class CaptchaCodeController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    @GetMapping("/kaptcha")
    public void kaptcha(HttpSession session, HttpServletResponse response) throws IOException {

        // 设置浏览器不要缓存等
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        // 将验证码保存到session中，并设置过期时间为两分钟
        String captchaText = captchaProducer.createText();
        session.setAttribute(MyConstants.CAPTCHA_SESSION_KEY, new CaptchaCodeVo(captchaText, 2 * 60));

        // 将验证码图片返回到前端
        try(ServletOutputStream out = response.getOutputStream();) {
            BufferedImage image = captchaProducer.createImage(captchaText);
            ImageIO.write(image, "jpg", out);
            out.flush();
        } //使用try-with-resources不用手动关闭流
    }
}

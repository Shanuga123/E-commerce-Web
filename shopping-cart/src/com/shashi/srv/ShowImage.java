package com.shashi.srv;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.shashi.service.impl.ProductServiceImpl;

@WebServlet("/ShowImage")
public class ShowImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ShowImage() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String prodId = request.getParameter("pid");
        System.out.println("Product ID: " + prodId);

        ProductServiceImpl dao = new ProductServiceImpl();
        byte[] image = dao.getImage(prodId);
        System.out.println("Image data length: " + (image != null ? image.length : "null"));

        if (image == null) {
            File fnew = new File(request.getServletContext().getRealPath("/images/laptop.png"));
            System.out.println("Default image path: " + fnew.getAbsolutePath());
            if (!fnew.exists()) {
                System.out.println("Default image not found.");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Default image not found.");
                return;
            }

            BufferedImage originalImage = ImageIO.read(fnew);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);
            image = baos.toByteArray();
        }

        response.setContentType("image/jpeg");

        try (ServletOutputStream sos = response.getOutputStream()) {
            sos.write(image);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

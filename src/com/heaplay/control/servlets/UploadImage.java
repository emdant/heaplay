package com.heaplay.control.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.heaplay.model.ConnectionPool;
import com.heaplay.model.beans.UserBean;
import com.heaplay.model.dao.UserDao;

@WebServlet("/uploadImage")
@MultipartConfig(
		fileSizeThreshold = 2 * 1024 * 1024,
		maxFileSize = 30 * 1024 * 1024,
		maxRequestSize = 60 * 1024 * 1024 )

public class UploadImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Part image = request.getPart("image");
		System.out.println(image);
		if(image != null) {
			String imageFileName = image.getSubmittedFileName();
			System.out.println(imageFileName);
			String imageExt = imageFileName.substring(imageFileName.lastIndexOf('.'),imageFileName.length()).toLowerCase();
			InputStream imageStream =image.getInputStream();
			byte[] imageBytes = imageStream.readAllBytes();
			imageStream.close();
		
			UserDao userDao = new UserDao((ConnectionPool) getServletContext().getAttribute("pool"));
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			if(user == null)
				response.sendRedirect(getServletContext().getContextPath()+"/home");
			else {
				try {
					user.setUserImageExt(imageExt);
					user.setUserImage(imageBytes);
					userDao.doUpdate(user);
					response.sendRedirect(getServletContext().getContextPath()+"/user/"+user.getUsername());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else 
			response.sendRedirect(getServletContext().getContextPath()+"/home");
	}

}
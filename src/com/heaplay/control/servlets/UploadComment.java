package com.heaplay.control.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.heaplay.model.ConnectionPool;
import com.heaplay.model.beans.CommentBean;
import com.heaplay.model.beans.UserBean;
import com.heaplay.model.dao.CommentDao;

@WebServlet("/uploadComment")

public class UploadComment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String track_id = request.getParameter("track_id");
		String comment = request.getParameter("comment");
		
		if(user == null || track_id == null || comment == null)
			response.sendRedirect(getServletContext().getContextPath()+"/home");
		else {
			CommentDao commentDao = new CommentDao((ConnectionPool) getServletContext().getAttribute("pool"));
			CommentBean commentBean = new CommentBean();
			commentBean.setBody(comment);
			commentBean.setTrackId(Long.parseLong(track_id));
			commentBean.setUserId(user.getId());
			commentBean.setAuthor(user.getUsername());
			try {
				commentDao.doSave(commentBean);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

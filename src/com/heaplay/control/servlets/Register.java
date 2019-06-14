package com.heaplay.control.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.heaplay.model.ConnectionPool;
import com.heaplay.model.beans.UserBean;
import com.heaplay.model.dao.UserDao;

@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
		UserBean userBean = (UserBean) session.getAttribute("user");
		
		if(userBean != null)
			response.sendRedirect("/");
		else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/register.jsp");
			rd.forward(request, response);
		}
	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ConnectionPool pool = (ConnectionPool) getServletContext().getAttribute("pool");
    	UserBean userBean = new UserBean();
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	String email = request.getParameter("email");
    	String error = "";
    	
    	if(username != null && !username.trim().equals(""))
    		request.setAttribute("username", username);
    	else
    		error += "Username non inserito";
  
    	if(password != null && !password.trim().equals(""))
    		request.setAttribute("password", password);
    	else 
    		error += "Password non inserita";
    	
    	if(email != null && !email.trim().equals(""))
    		request.setAttribute("email", email);
    	else
    		error += "Email non inserita";
    	
    	if(error.equals("")) {
    		userBean.setEmail(email);
    		userBean.setPassword(password);
    		userBean.setUsername(username);
    		userBean.setActive(true);
    		userBean.setAuth("user");
    		UserDao userDao = new UserDao(pool);
    		
    		try {
				userDao.doSave(userBean);
				ArrayList<String> keys = new ArrayList<String>();
				keys.add(email);
				keys.add(password);
				userBean = userDao.doRetrieveByKey(keys);
				if(userBean != null && userBean.getId() != -1) {
					request.getSession().setAttribute("user", userBean);
					response.sendRedirect("/");
				}
				else {
					error = "Errore nella registrazione"; 
					request.setAttribute("errorMessage", error);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/register.jsp");
					rd.forward(request, response);
				}
					
			} catch (SQLException e) {
				error = e.getMessage();
			}
    	}
	}

}
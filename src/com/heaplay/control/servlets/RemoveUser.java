package com.heaplay.control.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heaplay.model.ConnectionPool;
import com.heaplay.model.beans.TrackBean;
import com.heaplay.model.beans.UserBean;
import com.heaplay.model.dao.TrackDao;
import com.heaplay.model.dao.UserDao;


@WebServlet("/removeUser")
public class RemoveUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String user_id = request.getParameter("user_id");
		
		if(user == null || !user.getAuth().equals("admin") || user_id == null)
			response.sendRedirect(getServletContext().getContextPath()+"/home");
		else {
			ConnectionPool pool = (ConnectionPool) getServletContext().getAttribute("pool");
			UserDao userDao = new UserDao(pool);
			TrackDao trackDao = new TrackDao(pool);
			
			try {
				ArrayList<TrackBean> list = trackDao.getTracksByAuthor(Long.parseLong(user_id), -1, -1);
				boolean flag = false;
				for (int i = 0 ; i < list.size(); i++)
					if(list.get(i).getType().equals("pagamento")) {
						flag=true;
						list.get(i).setIndexable(false);
						trackDao.doUpdate(list.get(i));
					} else 
						trackDao.doDelete(list.get(i).getKey());
				ArrayList<String> keys = new ArrayList<String>();
				keys.add(user_id);
				UserBean userBean = userDao.doRetrieveByKey(keys);
				if(!flag) 
					userDao.doDelete(keys);
				else {
					userBean.setActive(false);
					userDao.doUpdate(userBean);
				}
				response.sendRedirect(getServletContext().getContextPath()+"/home");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}

}

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
import com.heaplay.model.beans.PlaylistBean;
import com.heaplay.model.beans.TrackBean;
import com.heaplay.model.beans.UserBean;
import com.heaplay.model.dao.PlaylistDao;
import com.heaplay.model.dao.TrackDao;

@WebServlet("/uploadPlaylist")
public class UploadPlaylist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String track_id = request.getParameter("track_id");
		String playlistName = request.getParameter("playlistName");
		String privacy = "public";
		
		if(user == null || track_id == null || playlistName == null) 
			response.sendRedirect(getServletContext().getContextPath()+"/home");
		else {
			ConnectionPool pool = (ConnectionPool) getServletContext().getAttribute("pool");
			PlaylistDao playlistDao = new PlaylistDao(pool);
			TrackDao trackDao = new TrackDao(pool);
			ArrayList<String> keys = new ArrayList<String>();
			keys.add(playlistName);
			keys.add(user.getId()+"");
			try {
				PlaylistBean playlist = (PlaylistBean) playlistDao.doRetrieveByKey(keys);
				if(playlist == null) {
					playlist = new PlaylistBean();
					playlist.setAuthor(user.getId());
					playlist.setName(playlistName);
					playlist.setPrivacy(privacy);
				} 
				keys.clear();
				keys.add(track_id);
				TrackBean track = (TrackBean) trackDao.doRetrieveByKey(keys);
				ArrayList<TrackBean> list = (ArrayList<TrackBean>) playlist.getTracks();
				if(!list.contains(track)) {
					list.add(track);
					playlist.setTracks(list);
					if(list.size() == 1)
						playlistDao.doSave(playlist);
					else
						playlistDao.doUpdate(playlist);
				} else //Vedere cosa fare; 
					;
				response.sendRedirect(getServletContext().getContextPath()+"/library");
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
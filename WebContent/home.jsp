<%@page import="com.heaplay.model.ConnectionPool"%>
<%@page import="com.heaplay.model.dao.TrackDao"%>
<%@page import="com.heaplay.model.beans.TrackBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.heaplay.model.beans.UserBean"%>
<%
	UserBean user = (UserBean)session.getAttribute("user");
%>
<h2>Homepage</h2>
<hr class="hr-form">
<%if(user == null || !user.getAuth().equals("admin")){%>
	<div id="playlists">
		<h3>Playlist pi� ascoltate</h3>
		<div class="flex-container">
		</div>
	</div>

	<div id="songs">
		<h3>Canzoni pi� ascoltate</h3>
		<div class="flex-container">
		</div>
	</div>
<% } %>

<script src="${pageContext.servletContext.contextPath}/js/song.js" ></script>
<script src="${pageContext.servletContext.contextPath}/js/users.js" ></script>
<script src="${pageContext.servletContext.contextPath}/js/home.js" ></script>
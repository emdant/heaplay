<%@page import="com.heaplay.model.beans.UserBean"%>
<%@page import="com.heaplay.model.beans.TrackBean"%>
<div>
	
	<% TrackBean track = (TrackBean)request.getAttribute("currentTrack"); 
	if(track != null ) {
																		%>
		<%@ include file="/_player.jsp"%>
	<%} %>
	<% 
	UserBean user = (UserBean) session.getAttribute("user");
	if(user != null) {%>
		<div>
			<textarea rows="2" cols="20" maxlength="255" placeholder="Scrivi un commento"></textarea>
			<button onclick="uploadComment(this)">Invia</button>
		</div>
	<%} else { %>
		<p>Link per loggare oppure registrarsi per lasciare un commento</p>
	<%} %>
	
	<div class="comment-container">
	</div>
	
</div>

<script src="${pageContext.servletContext.contextPath}/js/song.js" ></script>
<script src="${pageContext.servletContext.contextPath}/js/users.js" ></script>
<script src="${pageContext.servletContext.contextPath}/js/comment.js" ></script>
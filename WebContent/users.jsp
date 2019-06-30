<%@page import="com.heaplay.model.beans.TrackBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.heaplay.model.beans.UserBean"%>
<%
	UserBean userPage= (UserBean)request.getAttribute("user");	
	ArrayList<TrackBean> listOfTracks = (ArrayList<TrackBean>)request.getAttribute("tracks");
	Integer begin = (Integer)request.getAttribute("begin");
%>
<div class="selection">
	<button class="trackButton selected" onclick="selection(this,$('.playlistButton'))">Brani</button>
	<button class="playlistButton" onclick="selection(this,$('.trackButton'))">Playlist</button>
</div>

<div class="user-tracks">
	<span class="userName"><%=userPage.getUsername()%></span>
	
	<%if(listOfTracks.size() == 0) { %>
		<p>Non sono presenti brani</p>
	<%} %>
	
	<%for(int i=begin;i<listOfTracks.size() && i<begin + 5;i++) {	//Problema al numero massimo di track che posso mantenere in player in una pagina --> Capire come poter passare ad un altra pagina  per vedere le restanti
		TrackBean track = listOfTracks.get(i);	
	%>
		<div class="song"> <!-- Classe del player che possiamo mettere in una jsp a parte -->
			<audio preload="metadata" class="audio" ontimeupdate="updateCurrentTime(this)" >				<!-- Problemi con il caricamento dell'audio -->
				<%if(track != null) {%>
					<source src="../getAudio?id=<%=track.getId()%>&extension=<%=track.getTrackExt()%>" type="audio/<%=track.getTrackExt().substring(1)%>"> 		
	 			<%} %>
	 		</audio>
	 		
			<div class="song-image">
				<%if(track != null) {%>			<!-- Conterr� l'immagine della track -->
					<img width="100px" src="../getImage?id=<%=track.getId()%>&extension=<%=track.getImageExt()%>" alt="Errore">
				<%} %>
			</div>
			<div class="author">
				<span><%=track.getName()%></span>
				<span><%=track.getAuthor()%></span><!-- Problema per trovare l'autore -->
			</div>
			<div class="controls">	<!-- Sar� la classe aventi i controlli del player -->
				<button class="back"><img src="../images/back-button.png" width="25px"></button>
				<button class="play"><img src="../images/play-button.png" width="25px"></button>
				<button class="forward"><img src="../images/forward-button.png" width="25px"></button>
				<button class="pause"><img src="../images/pause-button.png" width="25px"></button>
				<button class="replay"><img src="../images/replay-button.png" width="25px"></button>
				<button class="slidebar">
					<span class="song-time">00:00</span>
					<input type="range" name ="slider" step="1" class="slider-bar" onchange="setCurrentTime(this)" value="0" min="0"  max=<%=track!=null ? track.getDuration() : 100%>>
					<%if(track != null) {%>
						<span><%=String.format("%2d:%2d", track.getDuration()/60,track.getDuration()%60)%></span>
					<%} %>
				</button>
				<button class="volume-button" ><img src="../images/volume-button.png" width="25px"></button>
				<input type="range" name ="volume" step=".1" class="volume" onchange="setVolume(this)" value="1" min="0"  max="1" >
			</div>
		</div>
		<%} %>
		<br>
		<form action="/heaplay/user/<%=userPage.getUsername()%>" method="POST">
			<input type="hidden" value="<%=begin%>" name="begin">
			<%for( int i= 0; i< listOfTracks.size(); i+=5) {%>
				<input type="submit" value="<%=i/5+1%>" onclick="beginValue(this)" id="<%=i%>">	
			<%} %>
		</form>
</div>

<div class="user-playlist hidden">
	<h3>Playlist</h3>

</div>

<div id="content"></div>

<script src="https://code.jquery.com/jquery-3.4.1.js" type="text/javascript"></script>
<script src="${pageContext.servletContext.contextPath}/js/song.js" ></script>
<script src="${pageContext.servletContext.contextPath}/js/users.js" ></script>
<script src="${pageContext.servletContext.contextPath}/js/load.js" ></script>


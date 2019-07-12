<%@page import="com.heaplay.model.beans.PurchasableTrackBean"%>
<%@page import="com.heaplay.model.beans.TrackBean"%>
<%@page import="java.util.ArrayList"%>
<%
	long iscritti = (Long)request.getAttribute("iscritti");
	long numeroBrani = (Long)request.getAttribute("numeroBrani");
%>

<div>
	<h4>Numero iscritti : <%=iscritti%></h4>
	<br>
	<h4>Numero brani caricati : <%=numeroBrani%></h4>
	<br>

	<nav class="content-nav">
		<a class="mostViewedButton selected" onclick="selectOption($('.mostViewedSongs'),this),getInfo(this)" href="#plays" >Pi� Ascoltati</a>
		<a class="mostLikedButton" onclick="selectOption($('.mostLikedSongs'),this),getInfo(this)" href="#likes">Pi� votati</a>
		<a class="mostSoldButton" onclick="selectOption($('.mostSoldSongs'),this),getInfo(this)" href="#sold">Pi� venduti</a>
	</nav> 

	
	<div id = "info-bar">
	
	
	</div>	

</div>

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script src="${pageContext.servletContext.contextPath}/js/info.js" ></script>

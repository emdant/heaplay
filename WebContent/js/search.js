const timePadder = (data,pad="0") => (data < 10) ? pad.toString()+data.toString() : data.toString();

const selectOption = (toShow,button) => {
	$(".only").hide();
	$(".only").removeClass("only");
	$(toShow).show();
	$(toShow).addClass("only");
	
	let children = $(button).parent().children();
	for(let i=0; i < children.length; i++)
		if(!$(children[i]).is(button)) 
			$(children[i]).removeClass("selected");
	$(button).addClass("selected");		
};

$(document).ready( () => {

	const showHide = (x,y) => { 	
		$(x).show();
		$(y).hide();
	};

	$(".search-button").click(() => {	//Listener della ricerca
		let url = encodeSessionId("/heaplay/search")+"?q="+$(".search-box").val()+"&filter="+$(".search-select").val();//url creato dinamicamente (probabilmente bisogna filtrare ciò che è stato scritto dal utente)
		if($(".search-box").val().toString() != "") { 
			$.ajax({
				"type":"GET",
				"url" : url,
				"beforeSend": () => {
					$(".content-wrapper > *").not(".search-content").remove();
					showHide($(".loading"),$("#content"));
				},
				"complete"  : () => {showHide($("#content"),$(".loading"))},
				"success": (data) => {
					let typeOfSearch = url.substring(url.indexOf("&filter")+8,url.length); //Controllo il tipo di ricerca
					const headerDiv ="<p>Elementi trovati: <span id='found'>"+data.length+"</span></p>";
					
					//Estrazione del container e rimozione degli elementi precedenti/inserimento dell'header
					let container = $("#content .flex-container");
					$("#content p").remove();
					$(container).empty();
					$(headerDiv).prependTo($("#content"));	
				
					for(let i = 0 ; i < data.list.length; i++){
						//Estrazione del bean
						let bean = data.list[i];
						createDiv(bean,container,typeOfSearch);
					}	
					//Aggiunta dei vari handlers
					if(typeOfSearch == "track" || typeOfSearch == "tag")
						addEventHandlers();
				}
			});
		}	
	});

	$(window).scroll(() => {
		let container = $("#content .flex-container");
		let numberOfElements = $(container).children().length - 1;
		
		//Effettuo la chiamata solo quando ho già effettuato una ricerca e ho raggiunto il bottom della pagina
		if(numberOfElements > 0  && ($(window).scrollTop() + $(window).height() >= $(document).height()-1)) {
			let url = encodeSessionId("/heaplay/search")+"?q="+$(".search-box").val()+"&startFrom="+numberOfElements.toString()+"&filter="+$(".search-select").val(); //url creato dinamicamente (probabilmente bisogna filtrare ciò che è stato scritto dal utente)
			let found = parseInt($("#found").text(),10); //Numero di elementi trovati dalla ricerca
			//Effettuo la chiamata se esistono ancora elementi da caricare
			if($(".search-box").val().toString() != "" && found > numberOfElements) { 
				$.ajax({
					"type":"GET",
					"url" : url,
					"success": (data) => {
						let typeOfSearch = url.substring(url.indexOf("&filter")+8,url.length); //Controllo il tipo di ricerca
						numberOfElements = $(container).children().length - 1;
						
						//Ulteriore controllo 
						if(found > numberOfElements) {
							for(let i = 0 ; i < data.list.length; i++){
								//Estrazione del bean
								let bean = data.list[i];
								//Creazione del div
								createDiv(bean,container,typeOfSearch);	
							}	
							//Aggiunta dei vari handlers
							if(typeOfSearch == "track" || typeOfSearch == "tag")
								addEventHandlers();
						}
					}
				});
			}
		}	
	});

});


//Dato un bean crea il div corrispondente
function createDiv(bean,container,typeOfSearch) {
	//Vari div in base al bean	Completarli
	const trackDiv = (typeOfSearch == "track" || typeOfSearch == "tag")? "<div class='song'><audio preload='none' class='audio'ontimeupdate='updateCurrentTime(this)'><source src='/heaplay/getAudio?id="+ bean.id +"&extension="+bean.trackExt+"'type='audio/"+bean.trackExt+"'></audio><div class='song-image'><img width='100px' src='/heaplay/getImage?id="+ bean.id +"'alt='Errore'></div><div class='song-content'><div class='song-info'><button class='play'><i class='fa fa-play color-white'></i></button><div><span class='author'><a href='/heaplay/user/"+bean.authorName+"'>"+bean.authorName+"</a></span><br><span class='song-name'><a href='/heaplay/user/"+bean.authorName+"/"+bean.name.replace(/\s/g,'')+"?id="+bean.id+"'>"+bean.name+"</a></span></div></div><div class='controls'><table><tr><td><span class='song-time'>00:00</span></td><td><input type='range'name='slider' step='1' class='slider slider-bar'onchange='setCurrentTime(this)' value='0' min='0'max='"+bean.duration+"'></td><td><span>"+timePadder(Math.floor(bean.duration/60))+":"+timePadder(Math.floor(bean.duration%60)) + "</span></td></tr><tr class='hidden'><td><i class='fa fa-volume-down'></i></td><td><input type='range' name='volume' step='.1' class='slider volume'onchange='setVolume(this)' value='1' min='0' max='1'></td><td><i class='fa fa-volume-up'></i></td></tr></table></div><div class='song-buttons'><span>"+bean.plays +((bean.plays > 1) ? " riproduzioni" : " riproduzione")+"</span><span class='song-button' onclick='like(event)' title='Aggiungi Mi Piace'><i class='fa fa-thumbs-up'></i> "+ bean.likes+"</span></div></div>" : "";
	const userDiv="<div class='user-div'><a href='/heaplay/user/"+bean.username+"'><img width='100px' alt='Non trovata' src='/heaplay/getImage?id=" + bean.id + "&user=true'></a><span><a href='/heaplay/user/"+bean.username+"'>"+bean.username+"</a></span></div>";
	const playlistDiv= typeOfSearch == "playlist" ? "<div class='playlist'><div class='playlist-image'><img alt='Non trovata' src='/heaplay/getImage?id=" + (bean.tracks.length > 0 ? bean.tracks[0].id : -1) + "'></div><div class='playlist-content'><span class='author'><a href='/heaplay/user/" + bean.authorName + "'>" + bean.authorName + "</a></span><br/><span class='playlist-name'><a href='/heaplay/user/" + bean.authorName + "/playlist/" + bean.name.replace(/\s/g,'') + "?id=" + bean.id + "'>" + bean.name + "</a></span></div></div>" : "";
	const commentDiv ="<div class='comment'><span class='comment-author'><b><a href='/heaplay/user/"+bean.author+"'>"+bean.author+"</a></b></span><span class='comment-body'>"+bean.body+"</span></div>";
	
	//Scelta del div da usare
	let div = (typeOfSearch == "track"|| typeOfSearch == "tag" ) ? trackDiv : typeOfSearch == "user" ? userDiv : typeOfSearch == "playlist" ? playlistDiv : commentDiv;
	
	//Creazione del div e inserimento
	let ob = $(div);
	$(ob).appendTo($(container));
	
	//Necessita di risettare la src nel tag audio per permettere di poter scaricare la track chiamando la servlet
	if(typeOfSearch == "track" || typeOfSearch == "tag")
		ob.find(".audio").prop("src","/heaplay/getAudio?id="+ bean.id +"&extension="+bean.trackExt);
}


//Esegue una chiamata ad ogni pressione del tasto 

function autocompleteSearch(el,suggestions) {
	let url = encodeSessionId("/heaplay/search")+"?q="+$(el).val()+"&filter="+$(".search-select").val()+"&auto=true";
	autocomplete(el,suggestions,url);
}


function autocomplete(el,suggestions,url) {
	if($(el).val().length > 1) {
		$.ajax({
			"type":"GET",
			"url" : url,
			"cache":false,
			"success": (data) => {
				$(suggestions).empty();
				data = data.filter((item,pos,array) => {
					return array.indexOf(item) == pos;
				});
				for(let i=0;i<data.length;i++) {
					let option = "<option value='"+data[i]+"'>"+data[i]+"</option>";
					$(option).appendTo(suggestions);
				}	
			}
		});
	}
}

//Funzione per effettuare la ricerca al click del tasto Enter
function searchOnEnterButton(e) {
	if(e.keyCode === 13) //keyCode per il tasto Enter
		$(".search-button").trigger("click");
}

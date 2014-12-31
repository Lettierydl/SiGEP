

function ativarMenuBotoes(titulo) {
	$("#menu-botoes").removeClass("fadeIn");
	$("#" + titulo).addClass("active");
	$("#menu-botoes").removeClass("fadeIn");
}

function ativarBotaoPesquisa() {
	var campo = $("#input_pesquisa");
	if (campo.attr('class') == "input-inativo") {
		campo.removeClass("input-inativo");
		campo.addClass("input-ativo");
		campo.focus();
	} else {
		campo.removeClass("input-ativo");
		campo.addClass("input-inativo");
	}
}
function focus(idCampo) {
	$("#" + idCampo).focus();
	alert(idCampo);
}

//Abrir modal foundation
function abrirModa(idModal) {
	$( "#"+idModal ).foundation('reveal', 'open');
}

//Feixar modal foundation
function fecharModal(idModal) {
	$( "#"+idModal ).foundation('reveal', 'close');
}

function maskDatepicker(idCampo) {
	maskData(idCampo);
	$("#" + idCampo).datepicker({
		nextText : "Próximo",
		prevText : "Antes",
		changeMonth : true,
		changeYear : true
	});
}
function maskData(idCampo) {
	$("#" + idCampo).mask("99/99/9999");
}

function maskCPF(idCampo) {
	$("#" + idCampo).mask("999.999.999-99");
}

function maskTelefone(idCampo) {
	$("#" + idCampo).mask("(99) 9999-9999");
}


function requestFullScreen() {
	var element = document.body;
	// Supports most browsers and their versions.
	var requestMethod = element.requestFullScreen
			|| element.webkitRequestFullScreen
			|| element.mozRequestFullScreen
			|| element.msRequestFullscreen;

	if (requestMethod) { // Native full screen.
		requestMethod.call(element);
	} else if (typeof window.ActiveXObject !== "undefined") { // Older IE.
		var wscript = new ActiveXObject("WScript.Shell");
		if (wscript !== null) {
			wscript.SendKeys("{F11}");
		}
	}else if (document.exitFullscreen) {
	    document.exitFullscreen();
	}else if (document.mozCancelFullScreen) {
	    document.mozCancelFullScreen();
	}
	else if (document.webkitCancelFullScreen) {
	    document.webkitCancelFullScreen();
	}
}
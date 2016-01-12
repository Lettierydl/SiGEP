addPtBrCalendar();


function script_footer(){
	document.onkeydown = function(e) {
		var keychar;
		
		// Internet Explorer
		try {
			keychar = String.fromCharCode(event.keyCode);
			e = event;
		}

		// Firefox, Opera, Chrome, etc...
		catch (err) {
			keychar = String.fromCharCode(e.keyCode);
		}

		if (e.keyCode == 27) {
			ir_home();
		}
		atalhos_adicionas(e);
	}
	maiusculo('input[type="text"]');
	pular_enter();
}

function pular_enter(){
	$('input[type="text"]').on("keypress", function(e) {
        /* ENTER PRESSED*/
        if (e.keyCode == 13) {
            /* FOCUS ELEMENT */
            var inputs = $(this).parents("form").eq(0).find('input[type="text"]');
            var idx = inputs.index(this);
            
            if (idx == inputs.length - 1) {
            	$('form :input[type="submit"]').focus();
            	
            } else {
                inputs[idx + 1].focus(); //  handles submit buttons
                inputs[idx + 1].select();
            }
            /* impede o sumbit caso esteja dentro de um form */
            e.preventDefault(e);
            return false;
        }
    });
}


function upCodigo(event) {
	if (event.keyCode == 32) {
		$('#buscaNome').css({
			display : 'inherit'
		});
		$('#buscaNome_input').focus();
		$('#buscaNome_input').select();
		var cod = $("#codigo");
		cod.val($.trim(cod.val().replace('\n','')));
		return false;
	}if (event.keyCode == 13) {
		var cod = $("#codigo");
		cod.val($.trim(cod.val().replace('\n','')));
		enterkey();
		return false;
	}else if($("#codigo").val().replace('\n','?').replace('\r','?').indexOf('?') != -1){
		var cod = $("#codigo");
		cod.val($.trim(cod.val().replace('\n','')));
		enterkey();
		setTimeout(function(){$("#codigo").select();},400);
		return false;
	}
	var cod = $("#codigo");
	if(cod.val().length > 1){
		var ultimoc = cod.val().charAt(cod.val().length - 1);
		if(ultimoc == '*'){
			$("#quantidade").val(cod.val().replace('*',''));
			cod.val('');
		}
	}
	
	cod.val(cod.val().replace(/[^\d ,]+/g,''));
	
}

function upCodigoMercadorias(event) {
	if (event.keyCode == 32) {
		$('#buscaNomeProd').css({
			display : 'inherit'
		});
		$('#buscaNomeProd_input').focus();
		$('#buscaNomeProd_input').select();
		var cod = $("#codigoProd");
		cod.val($.trim(cod.val().replace('\n','')));
		return false;
	}if (event.keyCode == 13) {
		var cod = $("#codigoProd");
		cod.val($.trim(cod.val().replace('\n','')));
		enterProd();
		return false;
	}else if($("#codigoProd").val().replace('\n','?').replace('\r','?').indexOf('?') != -1){
		var cod = $("#codigoProd");
		cod.val($.trim(cod.val().replace('\n','')));
		enterProd();
		setTimeout(function(){$("#codigoProd").select();},400);
		return false;
	}
	var cod = $("#codigoProd");
	if(cod.val().length > 1){
		var ultimoc = cod.val().charAt(cod.val().length - 1);
		if(ultimoc == '*'){
			$("#quantidadeProd").val(cod.val().replace('*',''));
			cod.val('');
		}
	}
	
	cod.val(cod.val().replace(/[^\d ,]+/g,''));
	
}


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

// Abrir modal foundation
function abrirModa(idModal) {
	$("#" + idModal).foundation('reveal', 'open');
	
	$("#" + idModal +' :input[type="text"]')[0].focus();
	$("#" + idModal +' :input[type="text"]')[0].select();
}

// Feixar modal foundation
function fecharModal(idModal) {
	$("#" + idModal).foundation('reveal', 'close');
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

function maiusculo(idCampo){
	  $(idCampo).keyup(function() {
	    $(this).val($(this).val().toUpperCase());
	  });
}

function alerta(mensagem) {
	alert(mensagem);
}

function tabenter(event,campo){
	var tecla = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	if (tecla==13) {
		campo.focus();
	}
}

function requestFullScreen() {
	var element = document.body;
	// Supports most browsers and their versions.
	var requestMethod = element.requestFullScreen
			|| element.webkitRequestFullScreen || element.mozRequestFullScreen
			|| element.msRequestFullscreen;

	if (requestMethod) { // Native full screen.
		requestMethod.call(element);
	} else if (typeof window.ActiveXObject !== "undefined") { // Older IE.
		var wscript = new ActiveXObject("WScript.Shell");
		if (wscript !== null) {
			wscript.SendKeys("{F11}");
		}
	} else if (document.exitFullscreen) {
		document.exitFullscreen();
	} else if (document.mozCancelFullScreen) {
		document.mozCancelFullScreen();
	} else if (document.webkitCancelFullScreen) {
		document.webkitCancelFullScreen();
	}
}

function addPtBrCalendar() {
	PrimeFaces.locales['pt'] = {
		closeText : 'Fechar',
		prevText : 'Anterior',
		nextText : 'Próximo',
		currentText : 'Começo',
		monthNames : [ 'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio',
				'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro',
				'Dezembro' ],
		monthNamesShort : [ 'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul',
				'Ago', 'Set', 'Out', 'Nov', 'Dez' ],
		dayNames : [ 'Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta',
				'Sexta', 'Sábado' ],
		dayNamesShort : [ 'Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb' ],
		dayNamesMin : [ 'D', 'S', 'T', 'Q', 'Q', 'S', 'S' ],
		weekHeader : 'Semana',
		firstDay : 1,
		isRTL : false,
		showMonthAfterYear : false,
		yearSuffix : '',
		timeOnlyTitle : 'Só Horas',
		timeText : 'Tempo',
		hourText : 'Hora',
		minuteText : 'Minuto',
		secondText : 'Segundo',
		currentText : 'Data Atual',
		ampm : false,
		month : 'Mês',
		week : 'Semana',
		day : 'Dia',
		allDayText : 'Todo Dia'
	};
}


function script_footer_login(){
	document.onkeydown = function(e) {
		var keychar;
		
		// Internet Explorer
		try {
			keychar = String.fromCharCode(event.keyCode);
			e = event;
		}

		// Firefox, Opera, Chrome, etc...
		catch (err) {
			keychar = String.fromCharCode(e.keyCode);
		}

		if (e.ctrlKey == true && e.keyCode == 27) {
			ir_config();
		}
	}

}

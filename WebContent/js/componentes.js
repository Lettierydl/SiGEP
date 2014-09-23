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
	$( "#"+idModal ).foundation('reveal', 'open');
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
	$("#" + idCampo).mask("(99)9999-9999");
}
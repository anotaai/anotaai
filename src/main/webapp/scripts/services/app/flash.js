'use strict';

angular.module('anotaai').factory('flash', [ '$rootScope', 'growl', 'growlMessages', '$translate', 'constant', function($scope, growl, growlMessages, $translate, constant) {

	return {
		setMessage : setMessage,
		setExceptionMessage: setExceptionMessage,
		destroyAllMessages: destroyAllMessages,
		setMessages: setMessages
	};

	function setExceptionMessage(exception) {
		var messages = [];
		if (exception && exception.data && exception.data.anotaaiExceptionMessages) {
			$.each(exception.data.anotaaiExceptionMessages, function(index, message) {
				setMessage(message);
			});
		} else {
			//TODO - Abrir um chamado e notificar o usuario assim que o problema estiver corrigido
			setMessage({
				isKey: false,
				text: 'Erro inesperado! Os responsáveis foram notificados e o problema será solucionado em breve!',
				time: constant.MESSAGE_CONSTANT,
				type: constant.TYPE_MESSAGE.ERROR
			});
		}
	}
	
	function setMessages(messages) {
		$.each(messages, function(index, message) {
			setMessage(message);
		});
	}
	
	function setMessage(message) {

		var messageStr = message.isKey ? translateMessage(message.key, message.params) : message.text;
		var tipoMensagem = message.type;
		switch (tipoMensagem.type) {
			case constant.TYPE_MESSAGE.ERROR:
				growl.error(messageStr, {ttl: message.time});
				break;
			case constant.TYPE_MESSAGE.SUCCESS:
				growl.success(messageStr, {ttl: message.time});
				break;
			case constant.TYPE_MESSAGE.INFO:
				growl.info(messageStr, {ttl: message.time});
				break;
			case constant.TYPE_MESSAGE.WARNING:
				growl.warning(messageStr, {ttl: message.time});
				break;
		}
	}
	
	function destroyAllMessages() {
		growl.destroyAllMessages();
	}
	
	function translateMessage(key, params) {
		var message = $translate.instant(key);
		if (params && params.length > 0) {
			$.each(params, function(index, param) {
				message = message.replace('{' + index + '}', param);
			});
		}
		return message;
	}
	
	function destroyAllMessages() {
		if(growlMessages.getAllMessages().length) {
			growlMessages.destroyAllMessages();
		}
	}

}]);

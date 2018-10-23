var protocol = 'ws://',
	hostname = window.location.hostname,
	port     = ':8080',
	pathname = '/' + window.location.pathname.split('/')[1];
var socket = new WebSocket(protocol + hostname + port + pathname + '/ws');


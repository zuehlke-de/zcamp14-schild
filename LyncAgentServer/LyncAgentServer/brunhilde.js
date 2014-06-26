var conf = require('./config.json');

var express = require('express');
var rest = require('restless');

var app = express();

app.get('/test', test);
app.get('/location', sendLocatiion);

app.listen(conf.port);
console.log('listening on port ' + conf.port);


function test(request, response) {
    
    rest.get("http:\\www.google.de",function(error, data){
    response.send(200, data);
});
}

function sendLocatiion(request, response) {
    var username = request.param("username");
    if (username == undefined || username === "") {
        response.send(404);
    }
    response.send(200,{ 'username': username, location: 'Raum 2.02' });
}

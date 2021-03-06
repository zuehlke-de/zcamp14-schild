var conf = require('./config.json');
var util = require('util');

var express = require('express');
var rest = require('restless');
var app = express();

app.get('/test', test);
app.get('/location', sendLocation);

app.listen(conf.port);
console.log('listening on port ' + conf.port);


function test(request, response) {
    console.log("using querystring: "+JSON.stringify(conf.queryString));
    queryAndSendLocation("mfu",response);
}

function sendLocation(request, response) {
    console.log("Received sendLocation request.");
    var username = request.param("username");
    if (username == undefined || username === "") {
        console.log("Request did not contain username");
        response.send(404);
    }
    console.log("Querying for username: " + username);
    queryAndSendLocation(username,response);
}

function queryAndSendLocation(username, response) {
    var query = JSON.parse(util.format(JSON.stringify(conf.queryString), username));
    console.log("Using query: " + JSON.stringify(query));
    rest.post(conf.searchUrl, { data: query }, function(error, innerData, innerResponse) {
        if (error instanceof Error) {
            console.log('Error: ' + error.message);
            //this.retry(5000); // try again after 5 sec
            response.send(404);
        } else {
            var location = "not set!";
            if (innerData == undefined || innerData == "" || innerData.error != undefined || innerData.hits.total < 1) {
                console.log("Location not availabe for user: " + username);
                //response.send(404);
            } else {
                console.log(innerData);
                location = innerData.hits.hits[0]._source.name;
            }
            queryRoom(username, location, response);

        }
    });
}

function queryRoom(username, location, response) {
    var query = JSON.parse(util.format(JSON.stringify(conf.queryStringRoom), username));
    console.log("Using query: " + JSON.stringify(query));
    rest.post(conf.searchUrl, { data: query }, function (error, innerData, innerResponse) {
        if (error instanceof Error) {
            console.log('Error: ' + error.message);
            //this.retry(5000); // try again after 5 sec
            response.send(404);
        } else {
            var room = "not set!";
            if (innerData == undefined || innerData == "" || innerData.error != undefined || innerData.hits.total < 1) {
                console.log("Location not availabe for user: " + username);
                //response.send(404);
            } else {
                console.log(innerData);
                room =  innerData.hits.hits[0]._source.name;
            }
            response.send(200, { 'username': username, 'location': location, 'room': room });
        }
    });
}



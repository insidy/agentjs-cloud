var http       = require("http"),
    url        = require("url"),
    user_util  = require("./user_util"),
    db         = {}; // laster will be a reference to redis.io

function forwardRequest(server, request, response, data) {

    var split_url = server.replace("http://", "").split(":");
    var server_host = split_url[0];
    var server_port = split_url[1] ? split_url[1] : "80";

    var options  = {
        host: server_host,
        port : server_port,
        method: request.method,
        path: request.url,
        headers: request.headers
    };

    var newReq = http.request(options, function(newRes) {
        newRes.content = "";
        newRes.on("data", function(data) {
            newRes.content += data;
        });

        newRes.on("end", function() {
            response.writeHead(newRes.statusCode, newRes.headers);

            if(newRes.content) {
                response.write(newRes.content);
            }

            response.end();
        });
        
    });

    newReq.on("error", function(e) {
      console.log(" Error: problem with request " + e.message);
      user_util.notFound(response);
    });

    if(request.content) {
        newReq.write(request.content);
    }

    newReq.end();

}

function onRequest(request, response) {
    var server_option   = request.headers["x-ont-server"],
        user_key        = request.headers["x-ont-user_key"],
        user_server;

    request.content = "";
    request.on("data", function (data) {
        request.content += data;

        // nuke request if content is too large
    });
    
    request.on("end", function () {
        console.log("incoming request:");
        console.log(request.content);
        
        response.writeHead(200, {"Content-Type": "text/plain"});
        response.write(request.content);
        response.end();
        return; // just echo
        if(server_option != "any") {
            if(!user_key) {
                user_util.notFound(response);
                return;
            } else {
                db.hget(user_util.getKey(user_key), "server", function(err, reply) {
                    if(err || !reply) {
                        user_util.notFound(response);
                    } else {
                        user_server = reply;

                        forwardRequest(user_server, request, response);

                        //response.writeHead(200, {"Content-Type": "text/plain"});
                        //response.write("Server: " + user_server);
                        //response.end();
                    }
                });
            }
        } else {
            // pick best/random server

            //forwardRequest(user_server, request, response);
        }
    });

    /*
    var pathname = url.parse(request.url).pathname;
    console.log("Request for " + pathname + " received.");


    response.writeHead(200, {"Content-Type": "text/plain"});
    response.write("Hello World");
    response.end();*/
}

function createServer(redis_db, port) {
    db = redis_db;

    http.createServer(onRequest).listen(port); // later on will handle all types of requests
    console.log("Server has started at " + port + ".");
}


exports.createServer = createServer;
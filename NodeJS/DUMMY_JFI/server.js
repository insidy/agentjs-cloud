var url  = require("url"),
    http = require("http"),
    fs   = require("fs"),
    argv = require("optimist")
    .usage("Usage: $0 --port [num] --users [filename]")
    .demand(["users", "port"])
    .argv;

fs.readFile(argv.users, function(err, data) {
    if(err) {
        throw err;
    }

    var user_list = JSON.parse(data);

    function onRequest(request, response) {
        var pathname = url.parse(request.url).pathname;
        console.log("Request for " + pathname + " received.");

        function sleep(milliSeconds) {
            var startTime = new Date().getTime();
            while (new Date().getTime() < startTime + milliSeconds);
        }

        sleep(10000);


        response.writeHead(200, {"Content-Type": "text/plain"});
        response.write(JSON.stringify(user_list));
        response.end();
    }

    http.createServer(onRequest).listen(argv.port);

});
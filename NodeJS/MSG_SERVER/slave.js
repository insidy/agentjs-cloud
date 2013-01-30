var http       = require("http"),
    redis      = require("redis"),
    base       = require("./base.js"),
    db         = redis.createClient();

function startProcedure(port, masterip, masterport) {
    console.log("Connected to Redis.io");
    db.slaveof(masterip, masterport, function() {
        console.log("Slave connected to " + masterip + " at " + masterport );

        base.createServer(db, port);
    });

    
}

function start(port, masterip, masterport) {

    if(db.connected) {
        startProcedure(port, masterip, masterport);
    } else {
        db.on("connect", function() {
            startProcedure(port, masterip, masterport);
        });
    }
}

exports.start = start;
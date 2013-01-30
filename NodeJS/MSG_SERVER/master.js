var http       = require("http"),
    redis      = require("redis"),
    base       = require("./base.js"),
    user_util  = require("./user_util"),
    db         = redis.createClient();

var jfi = {};

function updateUser(server_url, user_list) {
    for (var i = user_list.length - 1; i >= 0; i--) {
        // should use buffer to concat strings
        db.hmset( user_util.getKey(user_list[i]), "server", server_url);
    }
}

function startProcedure(port) {
    console.log("Connected to Redis.io");
    var jfi_servers = jfi.getServers();

    for (var i = jfi_servers.length - 1; i >= 0; i--) {
        var server_users = jfi.getUsersFromServer(jfi_servers[i], updateUser);
    }

    base.createServer(db, port);
}

function start(port, jfi_ref) {
    jfi = jfi_ref;

    if(db.connected) {
        startProcedure(port);
    } else {
        db.on("connect", function() {
            startProcedure(port);
        });
    }
}

exports.start = start;
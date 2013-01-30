// Jena Fuseki Interface
var fs   = require("fs"),
    http = require("http");

var jfi_servers = [];

function load(jfi_type, fnCallback) {
    if(jfi_type == "local") {
        loadFromFile("server_list.json", fnCallback);
    } else {

    }
}

function loadFromFile(file_name, fnCallback) {
    console.log("Loading servers from file " + file_name);

    fs.readFile(file_name, function(err, data) {
        if(err) {
            throw err;
        }

        jfi_servers = JSON.parse(data);

        if(!Array.isArray(jfi_servers)) { // invalid configuration
            console.log("Error: Invalid server list.");
            jfi_servers = [];
        } else {
            console.log("Found " + jfi_servers.length + " servers.");
        }

        fnCallback();

    });
}

function getServers() {
    return jfi_servers;
}

function getUsersFromServer(server_url, fnCallback) {
    // Make HTTP GET to server for /jena/users
    var http_get_url = server_url + "/jena/users";

    http.get(http_get_url, function(res) {
        console.log("("+ http_get_url +"): Status code " + res.statusCode);
        res.on("data", function (chunk) {
            res.content += chunk;
            
        });

        res.on("end", function() {
            var user_list = [];
            try {
                user_list = JSON.parse(res.content);
                if(Array.isArray(user_list)) {
                    console.log("("+ http_get_url +"): " + user_list.length + " users.");
                }
            } catch (exception) {

            }
            fnCallback(server_url, user_list);
        });

    }).on('error', function(e) {
      console.log("Error retrieving users from (" + server_url + "): " + e.message);
    });
    // Get return user-id list

}

exports.load = load;
exports.loadFromFile = loadFromFile;
exports.getServers = getServers;
exports.getUsersFromServer = getUsersFromServer;
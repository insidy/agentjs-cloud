var net = require("net");

function validateArgs(port, type, masterip, masterport) {

    if( port != Number(port) ){
        console.log(port + " is not a valid port number.");
        return false;
    }

    if(type != "master" && type != "slave") {
        console.log(type + " is not a valid messaging server type.");
        return false;
    }

    if(type == "slave") {
        if(!net.isIPv4(masterip)) {
            console.log(masterip + " is not a valid IPv4 ip.");
            return false;
        } else if ( masterport != Number(masterport)) {
            console.log(masterport + " is not a valid port number.");
            return false;
        }
    }

    return true;

}

exports.validateArgs = validateArgs;
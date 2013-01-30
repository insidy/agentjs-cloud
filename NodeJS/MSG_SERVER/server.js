var validator = require("./validator"),
    master    = require("./master"),
    slave     = require("./slave"),
    jfi       = require("./jfi"), // Jena Fuseki Interface
    argv      = require("optimist")
    .usage("Usage: $0 --port [num] --type [master | slave] --jfi [local | url] --masterip [ip] --masterport [port]")
    .default("port", 8888)
    .default("jfi", "local")
    .demand(["type"])
    .argv;


if(validator.validateArgs(argv.port, argv.type, argv.masterip, argv.masterport)) {
    
    if(argv.type == "master") {
        jfi.load(argv.jfi, function() {
            console.log("Starting server at " + argv.port);
            master.start(argv.port, jfi);
        });

    } else if (argv.type == "slave") {
        slave.start(argv.port, argv.masterip, argv.masterport);
    }
    
} else {
    console.log("Error: check you parameters.");
}

function getKey(user_data){
    if(user_data.id) {
        return "user:" + user_data.id;
    } else {
        return "user:" + user_data;
    }
}

function notFound(response) {
    response.writeHead(404, {"Content-Type": "text/plain"});
    response.write("User not found");
    response.end();
            
}

exports.getKey = getKey;
exports.notFound = notFound;
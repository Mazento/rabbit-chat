const HOST = "localhost";

export const getChatSocketEndpoint = port => {
    return "ws://" + HOST + ":" + port + "/chat";
}

export const getChatApiEndpoint = port => {
    return "http://" + HOST + ":" + port;
}

export const SOCKET_ENDPOINTS = [
    3100,
    3101
];

export const getChatSocketEndpoint = server => {
    return "ws://" + server + "/chat";
}

export const getChatApiEndpoint = server => {
    return "http://" + server;
}

export const SOCKET_ENDPOINTS = [
    3100,
    3101
];

export const getChatSocketEndpoint = port => {
    return 'ws://localhost:' + port + '/chat';
}

export const getChatApiEndpoint = port => {
    return 'http://localhost:' + port;
}

export const getDiscoveryApiUrl = () => {
    // if (process.env.DISCOVERY_API_URL) {
    //     return process.env.DISCOVERY_API_URL;
    // }

    return getLocationRoot() + ":3002/api";
}

export const getChatSocketUrl = port => {
    return `ws://${location.hostname}:${port}/chat`;
}

export const getChatApiUrl = port => {
    return `${getLocationRoot()}:${port}`;
}

export const getFileServerUrl = () => {
    return `${getLocationRoot()}:3001`;
}

const getLocationRoot = () => {
    return `${location.protocol}//${location.hostname}`;
}

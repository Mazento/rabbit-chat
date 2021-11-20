import React, { createContext, useRef, useState } from "react";
import { Stomp } from '@stomp/stompjs';
import { getChatSocketEndpoint } from "const/socketEndpoints";

// const SOCKET_ENDPOINT = 'ws://localhost:3100/chat';
// const SOCKET_ENDPOINT = process.env.CHAT_SOCKET_ENDPOINT || 'ws://localhost:8080/chat';

const STATE_KEYS = {
    chatMessages: "chatMessages",
    usersList: "usersList",
    isConnectionEstablished: "isConnectionEstablished",
    isChatInitiated: "isChatInitiated",
    errors: "errors",
    pendingFileId: "pendingFileId",
    chatServerURL: "chatServerURL",
}

export const ChatContext = createContext({});

export const ChatContextProvider = props => {
    const initState = {
        [STATE_KEYS.chatMessages]: [
            {
                username: "Obi Wan",
                text: "Hello there!",
                timestamp: 1634761606,
            },
            {
                username: "General Grievous",
                text: "General Kenobi",
                timestamp: 1634764606,
            },
        ],
        [STATE_KEYS.usersList]: new Set(),
        [STATE_KEYS.isConnectionEstablished]: false,
        [STATE_KEYS.isChatInitiated]: false,
        [STATE_KEYS.errors]: {},
        [STATE_KEYS.pendingFileId]: null,
        [STATE_KEYS.chatServerURL]: null
    };

    const actions = {
        onJoinChat: (username) => handleJoinChat(username),
        onSendMessage: (message, username) => handleSendMessage(message, username),
        onConnectToWebSocket: (chatServer) => handleConnectToWebSocket(chatServer),
    };

    const [clientRef, setClientRef] = useState(null);
    const [state, setState] = useState(initState);
    const stateRef = useRef({});
    stateRef.current = state;

    const updateState = (key, value) => {
        setState(prevState => ({...prevState, [key]: value}));
    }

    const handleJoinChat = username => {
        clientRef.send('/app/getChatHistory',  {});
        clientRef.send('/app/getUsersList',  {}, username);
    }

    const handleSendMessage = (message, username) => {
        if ((!message.text.trim() && !message.fileRecord) || !username)
            return;

        const endpoint = message.fileRecord ? '/app/fileMessage' : '/app/message';

        const data = {
            username,
            text: message.text.trim(),
            fileRecord: message.fileRecord,
            timestamp: Math.floor(Date.now())
        };

        clientRef.send(endpoint, {}, JSON.stringify(data));
    }

    const handleReceiveMessage = message => {
        if (!message?.body)
            return;

        console.log("Got new message: ", message.body);

        updateState(STATE_KEYS.chatMessages, [...stateRef.current.chatMessages, JSON.parse(message.body)]);
    }

    const handleAddUser = data => {
        const tmpSet = new Set(stateRef.current.usersList);
        tmpSet.add(data?.body);
        updateState(STATE_KEYS.usersList, tmpSet);
    }

    const handleRemoveUser = data => {
        const tmpSet = new Set(stateRef.current.usersList);
        tmpSet.delete(data?.body);
        updateState(STATE_KEYS.usersList, tmpSet);
    }

    const handlePopulateChat = data => {
        if (!data?.body)
            return;

        console.log(JSON.parse(data.body))

        setState(prevState => ({
            ...prevState,
            [STATE_KEYS.chatMessages]: JSON.parse(data.body),
            [STATE_KEYS.isChatInitiated]: true
        }));
    }

    const handlePopulateUsersList = data => {
        if (!data?.body)
            return;

        console.log(JSON.parse(data.body))

        setState(prevState => ({
            ...prevState,
            [STATE_KEYS.usersList]: new Set(JSON.parse(data.body))
        }));
    }

    const handleGetFileMessageId = data => {
        const fileId = JSON.parse(data?.body)

        if (fileId) {
            console.log("File ID from backend: " + fileId);
            updateState(STATE_KEYS.pendingFileId, fileId);
        }
    }

    const handleFileStatusUpdate = message => {
        if (!message?.body)
            return;

        const chatMessage = JSON.parse(message.body);

        const chatMessages = stateRef.current.chatMessages;
        let index = chatMessages.length - 1;
        let isUpdate = false;
        for ( ; index >= 0; index--) {
            if (chatMessages[index]?.id === chatMessage.id) {
                chatMessages[index] = chatMessage;
                isUpdate = true;
                break;
            }
        }

        if (isUpdate) {
            updateState(STATE_KEYS.chatMessages, chatMessages);
            console.log(chatMessage);
        } else {
            handleReceiveMessage(message);
        }
    }

    const handleConnectToWebSocket = chatServer => {
        const endpoint = getChatSocketEndpoint(chatServer);

        const socket = new WebSocket(endpoint);
        const client = Stomp.over(socket);
        setClientRef(client);
        client.connect({username: "new awesome username"}, () => subscribe(client), setErrors);

        updateState(STATE_KEYS.chatServerURL, endpoint);
    }

    const subscribe = client => {
        client.subscribe('/queue/message', handleReceiveMessage);
        client.subscribe('/queue/usersListAdd', handleAddUser);
        client.subscribe('/queue/usersListRemove', handleRemoveUser);
        client.subscribe('/queue/fileStatusUpdate', handleFileStatusUpdate);
        client.subscribe('/user/queue/replyFileMessage', handleGetFileMessageId);
        client.subscribe('/user/queue/replyJoin', handlePopulateChat);
        client.subscribe('/user/queue/replyChatHistory', handlePopulateChat);
        client.subscribe('/user/queue/replyUsersList', handlePopulateUsersList);

        updateState(STATE_KEYS.isConnectionEstablished, true);
    }

    const setErrors = errors => {
        console.error(errors);
        updateState(STATE_KEYS.errors, errors);
    }

    return (
        <ChatContext.Provider value={{state, actions}}>
            {props.children}
        </ChatContext.Provider>
    );
}

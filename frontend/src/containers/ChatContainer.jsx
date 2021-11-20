import React, { useContext, useEffect } from "react";
import styled from "styled-components";
import { Skeleton } from "@mui/material";

import { UserContext } from "contexts/UserContext";
import { ChatContext, ChatContextProvider } from "contexts/ChatContext";
import ChatSendMessageContainer from "containers/ChatSendMessageContainer";
import MessagesListContainer from "containers/MessagesListContainer";
import UsersListContainer from "containers/UsersListContainer";

const StyleWrapper = styled.div`
    display: flex;
    justify-content: center;
    flex-direction: column;
    
    max-width: 100%;
    max-height: 100%;
    width: 50rem;
    height: 50rem;
    padding: 1rem;
    
    background-color: #fff;
    box-shadow: 0 0 1.5rem 0 rgba(173, 179, 181, 0.4), 0 .25rem .3rem 0 rgba(173, 179, 181, 0.1);
    border-radius: 0.5rem;
`

const ScrollWrapper = styled.div`    
    display: flex;
    flex-direction: column-reverse;
    flex: 1;
    overflow: auto;
`

const ChatMessagesContainer = styled.div`
    display: flex;
    flex-direction: column;
    flex: 1;
    padding: 1rem;
`

const CustomSkeleton = () => <Skeleton height="6rem" />;

const LoadingPlaceholder = () => (
    <>
        <CustomSkeleton />
        <CustomSkeleton />
        <CustomSkeleton />
    </>
);

const ChatComponent = () => {
    const {state: userState} = useContext(UserContext);
    const {state: chatState, actions: chatActions} = useContext(ChatContext);

    useEffect(() => {
        if (!chatState.isConnectionEstablished) {
            chatActions.onConnectToWebSocket(userState.chatServer);
        } else {
            chatActions.onJoinChat(userState.username);
        }
    }, [chatState.isConnectionEstablished]);

    return (
        <StyleWrapper>
            <ScrollWrapper>
                <ChatMessagesContainer>
                    {chatState.isChatInitiated ? <MessagesListContainer /> : <LoadingPlaceholder />}
                </ChatMessagesContainer>
            </ScrollWrapper>
            <ChatSendMessageContainer />
        </StyleWrapper>
    );
}

const ChatContainer = () => {
    const {state: userState} = useContext(UserContext);

    return (
        <>
            {userState.username &&
            <ChatContextProvider>
                <ChatComponent />
                <UsersListContainer />
            </ChatContextProvider>}
        </>
    );
}
export default React.memo(ChatContainer);

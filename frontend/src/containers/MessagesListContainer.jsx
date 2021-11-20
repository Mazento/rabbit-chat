import React, { useContext } from "react";
import { ChatContext } from "contexts/ChatContext";
import ChatMessageItem from "components/ChatMessageItem";
import { UserContext } from "contexts/UserContext";

const MessagesListContainer = () => {
    const {state: userState} = useContext(UserContext);
    const {state: chatState} = useContext(ChatContext);

    return (
        <>
            {chatState.chatMessages.map(message => (
                <ChatMessageItem
                    key={message.id}
                    message={message}
                    isCurrentUser={message.username === userState.username}
                />
            ))}
        </>
    );
}

export default React.memo(MessagesListContainer);

import React from "react";
import styled from "styled-components";
import AttachedFile from "components/AttachedFile";

const Wrapper = styled.div`
    display: flex;
    flex-direction: column;
    word-break: break-all;
    ${props => props.isCurrentUser && "align-items: end"};
    
    &:not(:first-child) {
        margin-top: 1rem;
    }
`

const MessageBubbleBase = styled.div`
    width: fit-content;
    max-width: 20rem;
    padding: 1rem;
    box-shadow: 0 0 1rem 0 rgba(173, 179, 181, 0.4), 0 .25rem .3rem 0 rgba(173, 179, 181, 0.1)
`

const MessageBubbleInterlocutor = styled(MessageBubbleBase)`
    border-radius: 0.125rem 0.75rem 0.75rem 0.75rem;
    background: linear-gradient(135deg, #74bbff 20%, #0096ff);
`

const MessageBubbleSelf = styled(MessageBubbleBase)`
    border-radius: 0.75rem 0.75rem 0.125rem 0.75rem;
    background: #fff;
`

const MessageUsername = styled.div`
    margin: 0 0.25rem 0.25rem;
    color: #080064;
    font-weight: 600;
    font-size: 0.875rem;
`

const MessageText = styled.div`
    margin-bottom: 0.25rem;
    color: ${props => props.isCurrentUser ? "#000000d1" : "#fff"};
    //font-size: 0.875rem;
`

const MessageDatetime = styled.div`
    display: flex;
    justify-content: end;
    
    color: ${props => props.isCurrentUser ? "rgba(23, 23, 23, 0.5)" : "rgba(255, 255, 255, 0.5)"};
    font-size: 0.875rem;
    cursor: default;
    user-select: none;
`

const getFormattedTime = timestamp => {
    const date = new Date(timestamp);
    const hours = date.getHours();
    const minutes = "0" + date.getMinutes();

    return hours + ":" + minutes.substr(-2);
}

const ChatMessageItem = (props) => {
    const {message, isCurrentUser} = props;

    if (!message)
        return;

    const BubbleElement = props => (
        isCurrentUser
            ? <MessageBubbleSelf>
                {props.children}
            </MessageBubbleSelf>
            : <MessageBubbleInterlocutor>
                {props.children}
            </MessageBubbleInterlocutor>
    );

    return (
        <Wrapper isCurrentUser={isCurrentUser}>
            <MessageUsername>{message.username}</MessageUsername>
            <BubbleElement>
                <MessageText isCurrentUser={isCurrentUser}>{message.text}</MessageText>
                <AttachedFile isCurrentUser={isCurrentUser} file={message.fileRecord} />
                <MessageDatetime isCurrentUser={isCurrentUser}>{getFormattedTime(message.timestamp)}</MessageDatetime>
            </BubbleElement>
        </Wrapper>
    );
}

export default React.memo(ChatMessageItem);

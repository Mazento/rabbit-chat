import React from 'react';
import PageWrapper from "containers/PageWrapper";
import { UserContextProvider } from "contexts/UserContext";
import ChatContainer from "containers/ChatContainer";
import { ChatContextProvider } from "contexts/ChatContext";

const App = () => {
    return (
        <ChatContextProvider>
            <UserContextProvider>
                <PageWrapper>
                    <ChatContainer />
                </PageWrapper>
            </UserContextProvider>
        </ChatContextProvider>
    );
}

export default React.memo(App);

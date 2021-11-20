import React from 'react';
import PageWrapper from "containers/PageWrapper";
import { UserContextProvider } from "contexts/UserContext";
import ChatContainer from "containers/ChatContainer";

const App = () => {
    return (
        <UserContextProvider>
                <PageWrapper>
                    <ChatContainer />
                </PageWrapper>
        </UserContextProvider>
    );
}

export default React.memo(App);

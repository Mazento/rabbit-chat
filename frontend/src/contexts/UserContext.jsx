import React, { createContext, useState } from "react";
import JoinDialog from "components/JoinDialog";
import { SOCKET_ENDPOINTS } from "const/socketEndpoints";

export const UserContext = createContext();

export const UserContextProvider = props => {
    const initState = {
        username: '',
        chatServer: SOCKET_ENDPOINTS[0],
    };

    const actions = {
        onSetUsername: (username) => handleSetUsername(username),
    };

    const [state, setState] = useState(initState);

    const handleSetUsername = (username, chatServer) => {
        setState({...state, username, chatServer});
    }

    return (
        <UserContext.Provider value={{state, actions}}>
            {!state.username && <JoinDialog onSubmit={handleSetUsername}/>}
            {props.children}
        </UserContext.Provider>
    );
}

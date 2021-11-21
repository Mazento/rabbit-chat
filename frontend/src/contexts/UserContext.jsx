import React, { createContext, useState } from "react";

export const UserContext = createContext();

export const UserContextProvider = props => {
    const initState = {
        username: '',
    };

    const actions = {
        onSetUsername: (username) => handleSetUsername(username),
    };

    const [state, setState] = useState(initState);

    const handleSetUsername = username => {
        setState({...state, username});
    }

    return (
        <UserContext.Provider value={{state, actions}}>
            {props.children}
        </UserContext.Provider>
    );
}

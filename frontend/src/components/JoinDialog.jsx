import React, { useContext, useState } from "react";

import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { CircularProgress, Slide } from "@mui/material";

import SelectServerDropdown from "components/SelectServerDropdown";
import { getChatApiEndpoint, SOCKET_ENDPOINTS } from "const/socketEndpoints";
import { ChatContext } from "contexts/ChatContext";

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});

const DialogForm = props => {
    const {chatServerUrlList, onSetUsername, onSetChatServer} = props;
    const [username, setUsername] = useState('');
    const [chatServer, setChatServer] = useState(chatServerUrlList[0]);
    const [usernameUnavailable, setUsernameUnavailable] = useState(false);

    const handleSetUsernameValue = event => {
        setUsernameUnavailable(false);
        setUsername(event?.target?.value);
    }

    const handleSelectServer = event => {
        setChatServer(event?.target?.value);
    }

    const handleSubmit = () => {
        if (!username || !chatServer)
            return;

        const chatServerUrl = getChatApiEndpoint(chatServerUrlList[0]);
        fetch(chatServerUrl + "/checkUsername/" + username,
            {
                method: "GET",
            }
        ).then((res) => {
            res.json().then(resObject => {
                if (resObject?.usernameAvailable) {
                    onSetChatServer(chatServer);
                    onSetUsername(username);
                } else {
                    setUsernameUnavailable(true);
                }
            });
        });
    }

    return (
        <>
            <DialogTitle>Choose username and server</DialogTitle>
            <DialogContent>
                <TextField
                    autoFocus
                    fullWidth
                    margin="dense"
                    id="username"
                    label="Username"
                    variant="standard"
                    error={usernameUnavailable}
                    helperText={usernameUnavailable && "This username is unavailable"}
                    onChange={handleSetUsernameValue}
                />
                <SelectServerDropdown
                    selected={chatServer}
                    chatServerUrlList={chatServerUrlList}
                    onChange={handleSelectServer}
                />
            </DialogContent>
            <DialogActions>
                <Button fullWidth onClick={handleSubmit}>Join</Button>
            </DialogActions>
        </>
    )
}

const JoinDialog = props => {
    const {state: chatState} = useContext(ChatContext);

    if (!chatState.chatServerUrlList) {
        return <CircularProgress />;
    }

    return (
        <Dialog open TransitionComponent={Transition}>
            <DialogForm
                chatServerUrlList={chatState.chatServerUrlList}
                {...props}
            />
        </Dialog>
    );
}

export default React.memo(JoinDialog);

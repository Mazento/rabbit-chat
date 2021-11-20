import React, { useState } from "react";
import styled from "styled-components";

import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { Slide } from "@mui/material";
import SelectServerDropdown from "components/SelectServerDropdown";
import { getChatApiEndpoint, SOCKET_ENDPOINTS } from "const/socketEndpoints";

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});

const JoinDialog = (props) => {
    const [username, setUsername] = useState('');
    const [chatServer, setChatServer] = useState(SOCKET_ENDPOINTS[0]);
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

        const chatServerUrl = getChatApiEndpoint(SOCKET_ENDPOINTS[0]);
        fetch(chatServerUrl + "/checkUsername/" + username,
            {
                method: "GET",
            }
        ).then((res) => {
            if (res.ok) {
                res.json().then(isUsernameAvailable => {
                    if (isUsernameAvailable) {
                        props.onSubmit(username, chatServer);
                    } else {
                        setUsernameUnavailable(true);
                    }
                });
            }
        });
    }

    return (
        <div>
            <Dialog open TransitionComponent={Transition}>
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
                        onChange={handleSelectServer}
                    />
                </DialogContent>
                <DialogActions>
                    <Button fullWidth onClick={handleSubmit}>Join</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

export default React.memo(JoinDialog);

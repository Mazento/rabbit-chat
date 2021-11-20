import React, { useContext, useEffect, useRef, useState } from 'react';
import { IconButton, TextField } from "@mui/material";
import styled from "styled-components";
import Button from "@mui/material/Button";
import SendIcon from '@mui/icons-material/Send';
import CancelIcon from "@mui/icons-material/Cancel";

import { ChatContext } from "contexts/ChatContext";
import { UserContext } from "contexts/UserContext";
import ChatFileUpload from "components/ChatFileUpload";

// TODO check docker env
const FILE_SERVER_URL = "http://localhost:3001";
// const FILE_SERVER_URL = process.env.FILE_SERVER_URL || "http://localhost:3001";

const Container = styled.div`
    width: 100%;
    height: 3rem;
    display: flex;
    margin-top: 2rem;
    
    align-items: center;
`

const MessageTextField = styled(TextField)({
    '&.MuiTextField-root': {
        height: '100%',
        display: 'flex',
        flex: 1,
        marginRight: '1rem',
    },
    '& .MuiInputLabel-root': {
        lineHeight: '1.9rem',
    },
    '& .MuiInputBase-root': {
        height: '100%',
    },
});

const ButtonWrapper = styled.div`
    display: flex;
    height: 100%;
`

const SendButton = styled(Button)`
    width: 6rem;
`

const RemoveFileButton = styled(IconButton)`
      background-color: transparent !important;
`

const EndAdornment = props => {
    const {attachedFile, fileRef, onRemoveFile, onChangeFile} = props;

    return (
        <>
            {attachedFile &&
                <RemoveFileButton onClick={onRemoveFile}>
                    <CancelIcon />
                </RemoveFileButton>
            }
            <ChatFileUpload fileRef={fileRef} onChangeFile={onChangeFile} />
        </>
    )
}

const ChatSendMessageContainer = () => {
    const {state: chatState, actions: chatActions} = useContext(ChatContext);
    const {state: userState} = useContext(UserContext);

    const [value, setValue] = useState('');
    const [attachedFile, setAttachedFile] = useState(null);
    const attachedFileRef = useRef(null);

    const handleSetTextValue = (event) => {
        if (event?.target?.value?.length > 500)
            return;

        setValue(event?.target?.value);
    }

    const handleSend = () => {
        let fileRecord = null;

        if (attachedFile) {
            fileRecord = {
                filename: attachedFile.name,
                size: attachedFile.size
            }
        }

        const data = {
            text: value,
            fileRecord
        }

        chatActions.onSendMessage(data, userState.username);

        setValue("");
    }

    const handleCheckEnter = ev => {
        if (ev.key === "Enter") {
            handleSend();
        }
    }

    const handleChangeFile = file => {
        if (file?.name) {
            setAttachedFile(file);
        }
    }

    const handleRemoveFile = () => {
        setAttachedFile(null);
    }

    useEffect(() => {
        if (chatState.pendingFileId && attachedFile && attachedFileRef.current) {
            const file = attachedFileRef?.current?.files?.[0] || null;

            let formData = new FormData();
            formData.append("file", file);
            formData.append("fileId", chatState.pendingFileId);

            fetch(FILE_SERVER_URL + "/upload",
                {
                    mode: 'no-cors',
                    method: "POST",
                    body: formData,
                }
            ).then(() => {
                setAttachedFile(null);
            });
        }
    }, [chatState.pendingFileId])

    return (
        <Container>
            <MessageTextField
                id="message-field"
                label="Your message"
                variant="outlined"
                size="small"
                value={value}
                InputProps={{
                    endAdornment: (
                        <EndAdornment
                            attachedFile={attachedFile}
                            fileRef={attachedFileRef}
                            onRemoveFile={handleRemoveFile}
                            onChangeFile={handleChangeFile}
                        />
                    ),
                }}
                onChange={handleSetTextValue}
                onKeyPress={handleCheckEnter}
            />
            <ButtonWrapper>
                <SendButton
                    variant="contained"
                    endIcon={<SendIcon />}
                    size="small"
                    onClick={handleSend}
                >
                    Send
                </SendButton>
            </ButtonWrapper>
        </Container>
    );
}

export default React.memo(ChatSendMessageContainer);

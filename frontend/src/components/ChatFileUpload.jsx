import { InputAdornment } from "@mui/material";
import React from "react";
import styled from "styled-components";
import { FileUpload } from "@mui/icons-material";

const MAX_FILE_LIMIT = 100 * 1024 * 1024; // 100 MiB

const FileDownloadIcon = styled(FileUpload)`
    &:hover {
        cursor: pointer;
        fill: #0096ff;
    }
`

const FileUploadButton = props => {
    const {fileRef, onChangeFile} = props;

    const handleChooseFile = ev => {
        const file = ev?.target?.files?.[0];

        if (!file || file.size > MAX_FILE_LIMIT)
            return;

        onChangeFile(file);
    }

    return (
        <>
            <input
                hidden
                id="file-input"
                type="file"
                accept="*/*"
                ref={fileRef}
                onChange={handleChooseFile}
            />
            <label htmlFor="file-input" >
                <InputAdornment position="end">
                    <FileDownloadIcon />
                </InputAdornment>
            </label>
        </>
    );
}

export default React.memo(FileUploadButton);

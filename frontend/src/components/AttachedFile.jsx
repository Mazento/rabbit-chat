import React from "react";
import styled from "styled-components";

import FileDownloadIcon from '@mui/icons-material/FileDownload';
import CircularProgressCustom from "components/CircularProgressCustom";
import formatBytes from "utils/FormatBytes";
import { FILE_STATUS } from "const/fileStatus";
import { getFileServerUrl } from "utils/Network";

const Container = styled.div`
    margin-top: 0.5rem;
    display: flex;
  
    opacity: 0.7;
    user-select: none;
  
    transition: opacity 150ms cubic-bezier(.4,0,.2,1);
  
    &:hover {
      opacity: 1;
    }
`

const InfoContainer = styled.div`
    display: flex;
    flex-direction: column;
`

const ActionsContainer = styled.div`
    margin-left: 0.5rem;
    display: flex;
    align-items: center;
    justify-content: center;
`

const Text = styled.div`
    margin-bottom: 0.25rem;
    color: ${props => props.isCurrentUser ? "#4e88e8" : "#fff"};
    font-size: 0.875rem;
`

const FilenameText = styled(Text)`
    font-weight: 700;
`

const SizeText = styled(Text)`
`

const DownloadLink = styled.a`
    cursor: pointer;
`

const AttachedFile = props => {
    const {isCurrentUser, file, onClick} = props;

    if (!file)
        return null;

    const isReady = file.status === FILE_STATUS.SUCCESS;

    return (
        <Container
            onClick={isReady ? onClick : f => f}
        >
            <InfoContainer>
                <FilenameText isCurrentUser={isCurrentUser}>
                    {file.filename}
                </FilenameText>

                <SizeText isCurrentUser={isCurrentUser}>
                    {formatBytes(file.size)}
                </SizeText>
            </InfoContainer>

            <ActionsContainer>
                {isReady
                    ? <DownloadLink
                        target="_blank"
                        download={file.filename}
                        href={getFileServerUrl() + "/download/" + file.url + "?original=" + file.filename}
                    >
                        <FileDownloadIcon
                            htmlColor={isCurrentUser ? "#4e88e8" : "#fff"}
                            fontSize='large'
                        />
                    </DownloadLink>
                    : <CircularProgressCustom
                        sx={{ color: isCurrentUser ? "#4e88e8" : "#fff" }}
                        size="2rem"
                    />
                }

            </ActionsContainer>
        </Container>
    )
}

export default React.memo(AttachedFile);

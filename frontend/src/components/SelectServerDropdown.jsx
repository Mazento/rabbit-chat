import React from "react";
import styled from "styled-components";

import { InputLabel, MenuItem, Select } from "@mui/material";
import { SOCKET_ENDPOINTS } from "const/socketEndpoints";

const Wrapper = styled.div`
    margin-top: 2rem;
  
    display: flex;
    align-items: center;
`;

const Label = styled(InputLabel)`
    margin-right: 1rem;
`;

const SelectServerDropdown = (props) => {
    const {selected, onChange} = props;

    return (
        <Wrapper>
            <Label>Chat endpoint</Label>
            <Select
                value={selected}
                variant="standard"
                onChange={onChange}
            >
                {SOCKET_ENDPOINTS.map(port => (
                    <MenuItem
                        key={port}
                        value={port}
                    >
                        {port}
                    </MenuItem>
                ))}
            </Select>
        </Wrapper>
    );
}

export default React.memo(SelectServerDropdown);

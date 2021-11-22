import React from "react";
import styled from "styled-components";

import { InputLabel, MenuItem, Select } from "@mui/material";

const Wrapper = styled.div`
    margin-top: 2rem;
  
    display: flex;
    align-items: center;
  
    @media screen and (max-width: 768px) {
      align-items: start;
      flex-direction: column;
    }
`;

const Label = styled(InputLabel)`
    margin-right: 1rem;
`;

const SelectServerDropdown = (props) => {
    const {selected, chatServerPortsList, onChange} = props;

    return (
        <Wrapper>
            <Label>Chat endpoint port</Label>
            <Select
                value={selected}
                variant="standard"
                onChange={onChange}
            >
                {chatServerPortsList.map(port => (
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

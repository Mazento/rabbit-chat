import React from "react";
import CircularProgress from '@mui/material/CircularProgress';
import Box from '@mui/material/Box';

const CircularProgressCustom = props => {
    return (
        <Box sx={{ display: 'flex' }}>
            <CircularProgress {...props} />
        </Box>
    );
}

export default React.memo(CircularProgressCustom);

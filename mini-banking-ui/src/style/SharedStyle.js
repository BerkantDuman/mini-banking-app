import { Button, TextField, Paper, Typography } from '@mui/material';
import { styled } from '@mui/system';

export const StyledContainer = styled('div')({
    display: 'flex',
    height: '100vh', // This fills the screen vertically
    alignItems: 'center',
    justifyContent: 'center',
    background: 'linear-gradient(120deg, #f6d365 0%, #fda085 100%)',
});


export const StyledButton = styled(Button)({
    marginTop: '10px',
    marginBottom: '20px',
    backgroundColor: 'rgb(253 163 131)',
    color: '#fff',
    '&:hover': {
        backgroundColor: 'rgb(246 208 104)',
    },
});

export const StyledPaper = styled(Paper)({
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: "center",
    padding: '20px',
    background: 'rgba(255, 255, 255, 0.7)',
    textAlign: "center",
    minWidth: '40vw'
});

export const CustomTextField = styled(TextField)({
    '& label.Mui-focused': {
        color: 'rgb(253 163 131)',
    },
    '& .MuiInput-underline:after': {
        borderBottomColor: 'rgb(253 163 131)',
    },
    '& .MuiOutlinedInput-root': {
        '&.Mui-focused fieldset': {
            borderColor: 'rgb(253 163 131)',
        },
    },
});

export const HoverTypography = styled(Typography)({
    color: 'rgb(253 163 131)',
    '&:hover': {
        cursor: 'pointer',
        color: 'rgb(246 208 104)',
    },
});
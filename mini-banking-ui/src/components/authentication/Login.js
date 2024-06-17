import { Box, Typography} from '@mui/material';
import { styled } from '@mui/system';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthProvider';
import { StyledButton, StyledPaper, StyledContainer, CustomTextField } from '../../style/SharedStyle';



export const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMsg, setErrorMsg] = useState('');
    const navigate = useNavigate();
    const auth = useAuth();


    const validateForm = () => {
        return username.length > 0 && password.length > 0;
    }

    const handleSubmit = async (event) => {
        event.preventDefault()
        try {
            await auth.loginAction({ username: username, password: password });
            navigate('/account');
        } catch (error) {
            setErrorMsg("Invalid username or password");
        }

    }



    return (
        <StyledContainer component="main" maxWidth="sm">
            <StyledPaper elevation={6}>
                <Typography component="h1" variant="h4" color="text.primary">Welcome, please sign in!</Typography>
                {errorMsg && <Typography variant='body2' color="error">{errorMsg}</Typography>}
                <Box component="form" width="100%" mt={1} onSubmit={handleSubmit}>
                    <CustomTextField
                        variant="standard"
                        margin="normal"
                        required
                        fullWidth
                        label="Username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                        autoComplete="username"
                        autoFocus
                    />

                    <CustomTextField
                        variant="standard"
                        margin="normal"
                        required
                        fullWidth
                        label="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        type="password"
                        autoComplete="current-password"
                    />

                    <StyledButton
                        type="submit"
                        fullWidth
                        variant="contained"
                        disabled={!validateForm()}
                    >
                        Sign in
                    </StyledButton>

                </Box>
                <HoverTypography variant="caption" onClick={() => navigate('register')} alignSelf={'center'} color="text.primary">Don't you have an account, please sing up!</HoverTypography>
            </StyledPaper>
        </StyledContainer>
    );
}

const HoverTypography = styled(Typography)({
    color: 'rgb(253 163 131)',
    '&:hover': {
        cursor: 'pointer',
        color: 'rgb(246 208 104)',
    },
});



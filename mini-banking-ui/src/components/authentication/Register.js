import { Box, Typography } from '@mui/material';
import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthProvider';
import { StyledContainer, StyledPaper, CustomTextField, StyledButton, HoverTypography } from '../../style/SharedStyle';
import { AlertContext } from '../../context/AlertContext';


export const Register = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const navigate = useNavigate();
    const auth = useAuth();
    const { setAlert } = useContext(AlertContext);

    const validateForm = () => {
        return username.length > 0 && password.length > 0 && email.length > 0;
    }

    const clearForm = () => {
        setEmail('');
        setPassword('');
        setUsername('');
    }

    const handleSubmit = async (event) => {
        event.preventDefault()
        const res = await auth.register({ username: username, password: password, email: email })
        console.log(res)
        if (res === 200) {
            setAlert({ open: true, message: 'Registeration has successfully completed', severity: 'success' });
            clearForm()
        } else {
            setAlert({ open: true, message: res, severity: 'error' });
        }
    }

    return (
        <StyledContainer component="main" maxWidth="sm">
            <StyledPaper elevation={6}>
                <Typography component="h1" variant="h4" color="text.primary">Register!</Typography>
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

                    <CustomTextField
                        variant="standard"
                        margin="normal"
                        required
                        fullWidth
                        label="Email"
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                        type="email"
                        autoComplete="current-password"
                    />

                    <StyledButton
                        type="submit"
                        fullWidth
                        variant="contained"
                        disabled={!validateForm()}
                    >
                        Create account
                    </StyledButton>

                    <HoverTypography variant="caption" onClick={() => navigate("/")} alignSelf={'center'} color="text.primary">SIGN IN</HoverTypography>

                </Box>
            </StyledPaper>
        </StyledContainer>
    );
}




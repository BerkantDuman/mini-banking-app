import React, { useContext, useState } from 'react';
import { CustomTextField, StyledButton, StyledContainer, StyledPaper } from '../../style/SharedStyle';
import { Box, InputAdornment, Typography } from '@mui/material';
import { makeTransfer } from '../../services/ApiServices';
import { AlertContext } from '../../context/AlertContext';


export const Transaction = () => {

    const [amount, setAmount] = useState(0);
    const [fromAccountNumber, setFromAccountNumber] = useState('');
    const [toAccountNumber, setToAccountNumber] = useState('');
    const [errors, setErrors] = useState({});
    const { setAlert } = useContext(AlertContext);

    const validate = (amount, fromAccountNumber, toAccountNumber) => {
        const errors = {};

        if (amount <= 0) {
            errors.amount = 'Amount must be greater than 0.';
        }
        if (fromAccountNumber === toAccountNumber) {
            errors.fromAccountNumber = "The transfer can not be made between same";
        }
        if (fromAccountNumber === '') {
            errors.fromAccountNumber = 'From Account is required.';
        }
        if (toAccountNumber === '') {
            errors.toAccountNumber = 'To Account is required.';
        }

        return errors;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const errors = validate(amount, fromAccountNumber, toAccountNumber);
        setErrors(errors);

        if (Object.keys(errors).length === 0) {
            const transfer = { amount, fromAccountNumber, toAccountNumber };
            try {
                const transaction = await makeTransfer(transfer)
                setAlert({ open: true, message: 'Transfer has successfully completed', severity: 'success' });
            } catch (error) {
                setAlert({ open: true, message: error.response.data, severity: 'error' });
            }
        }
    };

    return (

        <StyledContainer component="main" maxWidth="sm">
            <StyledPaper elevation={6}>
            <Typography sx={{ color: '#fda184', marginBottom: '5%' }} variant="h4">
                TRANSACTION
            </Typography>
                <Box component="form" sx={{ width: '100%' }} onSubmit={handleSubmit} autoComplete="off">
                    <Box marginBottom={2}>
                        <CustomTextField
                            label="Amount"
                            variant="standard"
                            fullWidth
                            type="number"
                            value={amount}
                            onChange={({ target }) => setAmount(target.value)}
                            error={!!errors.amount}
                            helperText={errors.amount}
                            InputProps={{
                                startAdornment: <InputAdornment position="start">$</InputAdornment>,
                            }}

                        />
                    </Box>
                    <Box marginBottom={2}>
                        <CustomTextField
                            label="From Account Number"
                            variant="standard"
                            fullWidth
                            value={fromAccountNumber}
                            onChange={({ target }) => setFromAccountNumber(target.value)}
                            error={!!errors.fromAccountNumber}
                            helperText={errors.fromAccountNumber}
                            autoFocus
                        />
                    </Box>
                    <Box marginBottom={2}>
                        <CustomTextField
                            label="To Account Number"
                            variant="standard"
                            fullWidth
                            value={toAccountNumber}
                            onChange={({ target }) => setToAccountNumber(target.value)}
                            error={!!errors.toAccountNumber}
                            helperText={errors.toAccountNumber}
                        />
                    </Box>
                    <StyledButton type='submit' variant='contained' color='primary'>
                        Make Transfer
                    </StyledButton>
                </Box>
            </StyledPaper>
        </StyledContainer>

    );

}
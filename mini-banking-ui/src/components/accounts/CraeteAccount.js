import { TextField, Paper, Box, Modal, styled, IconButton } from '@mui/material';
import { useContext, useState } from 'react';
import { CustomTextField, StyledButton } from '../../style/SharedStyle';
import { createAccount } from '../../services/ApiServices';
import { useAuth } from '../../context/AuthProvider';
import { AlertContext } from '../../context/AlertContext';


export default function CraeteAccount({ onClose }) {
    const [balance, setBalance] = useState(0);
    const [name, setName] = useState('');
    const [number, setNumber] = useState('');
    const { user } = useAuth();
    const { setAlert } = useContext(AlertContext);

    const validateForm = () => {
        return number.length > 0 && name.length > 0 && balance !== null;
    }

    const create = async () => {
        try {
            await createAccount({ name: name, number: number, balance: balance, user: { id: user.userId } })
        } catch (error) {
            setAlert({ open: true, message: error.response.data, severity: 'error' });
        }

    }

    return (
        <StyledModal
            open={true}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <ModalPaper>
                <CloseButton onClick={() => onClose(false)}>X</CloseButton>
                <Box component="form" width="100%" mt={1} onSubmit={() => { create() }}>
                    <CustomTextField
                        variant="standard"
                        margin="normal"
                        required
                        fullWidth
                        label="Name"
                        value={name}
                        onChange={e => setName(e.target.value)}
                        autoComplete="current-name"
                    />

                    <CustomTextField
                        variant="standard"
                        margin="normal"
                        required
                        fullWidth
                        label="Number"
                        value={number}
                        onChange={e => setNumber(e.target.value)}
                        autoComplete="current-number"
                    />

                    <CustomTextField
                        variant="standard"
                        margin="normal"
                        required
                        fullWidth
                        label="Balance"
                        value={balance}
                        onChange={e => setBalance(e.target.value)}
                        autoComplete="Balance"
                        type='number'
                        autoFocus
                    />

                    <StyledButton
                        type="submit"
                        fullWidth
                        variant="contained"
                        disabled={!validateForm()}
                    >
                        Create account
                    </StyledButton>


                </Box>
            </ModalPaper>
        </StyledModal>
    );
}

const StyledModal = styled(Modal)(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
}));

const ModalPaper = styled(Paper)({
    position: "relative",
    padding: '20px',
    backgroundColor: '#fff',
});

const CloseButton = styled(IconButton)({
    position: 'absolute',
    right: '10px',
    top: '10px',
});

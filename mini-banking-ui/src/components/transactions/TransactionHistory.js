import { Paper, Box, Modal, styled, IconButton, ListItem, ListItemText, ListItemSecondaryAction, List, ListItemIcon, Typography } from '@mui/material';
import { useEffect, useState } from 'react';
import { getTransactionHistory } from '../../services/ApiServices';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ErrorIcon from '@mui/icons-material/Error';
import { formatDate } from '../../common/commonFunctions';

const TransactionStatus = Object.freeze({
    FAILED: "FAILED",
    SUCCESS: "SUCCESS"
});

const useFetch = (accountId) => {
    const [transactions, setAccounts] = useState([]);


    const fetchTransactions = async () => {
        try {
            const transactions = await getTransactionHistory(accountId);
            setAccounts(transactions)
        } catch (error) {
            console.log("Get Accounts Error", error)
        }
    };

    useEffect(() => {
        fetchTransactions();
    }, []);

    return { transactions };
};


export default function TransactionHistory({ onClose, accountId }) {
    const { transactions } = useFetch(accountId);

    return (
        <StyledModal
            open={true}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <ModalPaper>
                <Box>
                    <Typography component="h1" variant="h5" color="text.primary">Transaction History</Typography>
                <CloseButton sx={{zIndex: 10}} onClick={() => onClose(false)}>X</CloseButton>

                </Box>
                <Box component="form" width="100%" maxHeight={500} overflow={'auto'} mt={1}>
                    <List>
                        {transactions.map((transaction, index) => {
                            return (
                                <ListItem key={index}>
                                    <ListItemIcon>
                                        {transaction.status === TransactionStatus.SUCCESS ?
                                            (<CheckCircleIcon sx={{ color: 'green' }} />)
                                            :
                                            (<ErrorIcon sx={{ color: 'red' }} />)
                                        }
                                    </ListItemIcon>
                                    <ListItemText
                                        primary={`Transaction ${transaction.status.toLowerCase()} from  Account ${transaction.from.name} to  Account ${transaction.to.name}`}
                                        secondary={`${formatDate(transaction.transactionDate)}`}
                                        sx={{ marginRight: "30px" }} />
                                    <ListItemSecondaryAction>
                                        {transaction.amount}$
                                    </ListItemSecondaryAction>
                                </ListItem>
                            )
                        })}
                    </List>
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
    width: '70vw'
});

const CloseButton = styled(IconButton)({
    position: 'absolute',
    right: '10px',
    top: '10px',
});

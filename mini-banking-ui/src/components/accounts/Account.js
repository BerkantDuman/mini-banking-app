import {
    Button, Paper,
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    Box,
    TablePagination,
    IconButton,
    Divider,
    InputBase
} from '@mui/material';
import { styled } from '@mui/system';
import React, { useEffect, useState } from 'react';
import { getAccounts } from '../../services/ApiServices';
import CraeteAccount from './CraeteAccount';
import { StyledContainer, StyledPaper } from '../../style/SharedStyle';
import TransactionHistory from '../transactions/TransactionHistory';
import SearchIcon from '@mui/icons-material/Search';

export const Account = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [search, setSearch] = useState('');
    const [isTransactionModalOpen, setIsTransactionModelOpen] = useState(false);
    const [transactionHistoryId, setTransactionHistoryId] = useState(false);
    const [accounts, setAccounts] = useState({ totalElements: 0, number: 0, content: [], pageable: { pageSize: 5, pageNumber: 0 } });

    useEffect(() => {
        fetchAccounts();
    }, []);

    const fetchAccounts = async (search, pageable = { pageSize: 5, pageNumber: 0 }) => {
        try {
            let accountFilter = [];
            if (search) {
                accountFilter.push({ columnName: 'name', columnValue: search }, { columnName: 'number', columnValue: search })
            }
            const accounts = await getAccounts(accountFilter, pageable);
            setAccounts(accounts)
        } catch (error) {
            console.log("Get Accounts Error", error)
        }
    };

    const handleChangePage = (event, newPage) => {
        fetchAccounts(search, { pageSize: accounts.pageable.pageSize, pageNumber: newPage })
    };

    const handleChangeRowCount = (event, newRowCount) => {
        fetchAccounts(search, { pageSize: newRowCount.props.value, pageNumber: 0 })
    };

    const handleAddAccountModal = (isOpen) => {
        setIsModalOpen(isOpen);
    }

    const handleTransactionHisyoryModal = (isOpen, accountId) => {
        setIsTransactionModelOpen(isOpen);
        setTransactionHistoryId(accountId);
    }


    return (
        <StyledContainer component="main" maxWidth="sm">
            <StyledPaper elevation={6}>
                <Paper
                    component="form"
                    sx={{ display: 'flex', alignItems: 'center', width: '100%' }}
                >
                    <InputBase
                        sx={{ ml: 1, flex: 1 }}
                        placeholder="Search Account"
                        inputProps={{ 'aria-label': 'search account' }}
                        onChange={e => setSearch(e.target.value)}
                    />
                    <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />
                    <IconButton type="button" sx={{ p: '10px' }} onClick={() => { fetchAccounts(search) }} aria-label="search">
                        <SearchIcon />
                    </IconButton>

                </Paper>
                <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    {isModalOpen && <CraeteAccount onClose={handleAddAccountModal} />}
                    <TableContainer component={Paper} sx={{ marginTop: 2 }}>
                        <Table sx={{ minWidth: "70vw" }} aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <th><CreateButton color='secondary' onClick={() => handleAddAccountModal(true)} >Add Account</CreateButton></th>
                                </TableRow>
                            </TableHead>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Name</TableCell>
                                    <TableCell>Number</TableCell>
                                    <TableCell>Balance</TableCell>
                                    <TableCell sx={{ textAlign: 'center' }}>History</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {accounts.content.map((account) => (
                                    <TableRow
                                        key={account.id}
                                        sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                    >
                                        <TableCell>{account.name}</TableCell>
                                        <TableCell>{account.number}</TableCell>
                                        <TableCell>{account.balance.toLocaleString()}$</TableCell>
                                        <TableCell sx={{ textAlign: 'center' }}>
                                            <CreateButton color='secondary' onClick={() => handleTransactionHisyoryModal(true, account.id)} >Transaction History</CreateButton>
                                            {isTransactionModalOpen && transactionHistoryId === account.id &&
                                                <TransactionHistory key={account.id} accountId={account.id} onClose={handleTransactionHisyoryModal} />}
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                        <TablePagination
                            rowsPerPageOptions={[1, 5, 10, 20, 25]}
                            component="div"
                            count={accounts.totalElements}
                            rowsPerPage={accounts.pageable.pageSize}
                            page={accounts.number}
                            onPageChange={handleChangePage}
                            onRowsPerPageChange={handleChangeRowCount}
                        />
                    </TableContainer>

                </Box>
            </StyledPaper>
        </StyledContainer>
    );
}



const CreateButton = styled(Button)({
    margin: "2%",
    padding: 0,
    color: '#fbb37a',

});




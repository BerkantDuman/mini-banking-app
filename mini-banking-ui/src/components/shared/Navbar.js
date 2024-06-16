import * as React from 'react';
import { AppBar, Toolbar, IconButton, Typography, Button, Drawer, List, ListItem, styled, Box } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import AccountCircleRoundedIcon from '@mui/icons-material/AccountCircleRounded';
import { useAuth } from '../../context/AuthProvider';
import { useNavigate } from 'react-router-dom';

export default function Navbar() {
    const [mobileView, setMobileView] = React.useState(false);
    const [drawerOpen, setDrawerOpen] = React.useState(false);
    const auth = useAuth();
    const navigate = useNavigate()

    const handleResize = () => {
        return window.innerWidth <= 900 ? setMobileView(true) : setMobileView(false);
    };

    React.useEffect(() => {
        handleResize();
    }, []);

    window.addEventListener('resize', handleResize);

    const displayDesktop = () => {
        return (
            <Toolbar>
                <Box display="flex" flexGrow={1} alignItems={'center'} >
                    <AccountBalanceIcon sx={{ width: 50, height: 50, marginRight: 5 }} />
                    <Typography variant="h6" component="div"> MINI BANKING APP </Typography>
                </Box>
                <Box display="flex">
                    <Button color="inherit" href='/account'>ACCOUNT</Button>
                    <Button color="inherit" href='/transaction'>TRANSACTION</Button>
                    <AccountCircleRoundedIcon onClick={() => { auth.logOut() }} sx={{ width: 50, height: 50, cursor: 'pointer' }} />
                </Box>
            </Toolbar>
        );
    };

    const displayMobile = () => {
        const handleDrawerOpen = () => setDrawerOpen(true);
        const handleDrawerClose = () => setDrawerOpen(false);

        return (
            <Toolbar>
                <IconButton
                    edge="start"
                    color="inherit"
                    aria-label="menu"
                    onClick={handleDrawerOpen}
                >
                    <MenuIcon />
                </IconButton>

                <Drawer
                    anchor="left"
                    open={drawerOpen}
                    onClose={handleDrawerClose}
                >
                    <List sx={{ background: '#ab893dc7', height: '100vh', minWidth: 200 }}>
                        <StyledListItem button  onClick={()=> {handleDrawerClose(); navigate('/account')}}>Account</StyledListItem>
                        <StyledListItem button  onClick={()=> {handleDrawerClose(); navigate('/transaction')}}>Transaction</StyledListItem>
                        <StyledListItem button  onClick={()=> {handleDrawerClose(); auth.logOut()}}>LOGOUT</StyledListItem>
                    </List>
                </Drawer>

                <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    My App
                </Typography>
            </Toolbar>
        );
    };

    return (
        <AppBar position="static" sx={{ background: '#ab893dc7' }}>
            {mobileView ? displayMobile() : displayDesktop()}
        </AppBar>
    );
}


const StyledListItem = styled(ListItem)({
    color: '#fff'
});

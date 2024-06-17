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
                    <Button color="inherit" onClick={() => { auth.logOut() }}>LogOut</Button>
                    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }} >
                        <AccountCircleRoundedIcon sx={{ width: 50, height: 50 }} />
                        <Typography variant="caption"> {auth.user.sub} </Typography>
                    </Box>
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
                    <Box sx={{ background: '#ab893dc7', display: 'flex', flexDirection: 'column', height: '100vh', minWidth: 200 }}>
                        <List sx={{ flexGrow: 1 }}>
                            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }} >
                                <AccountCircleRoundedIcon sx={{ width: 50, height: 50, color: 'white' }} />
                                <Typography variant="caption" color={'white'}> {auth.user.sub} </Typography>
                            </Box>
                            <StyledListItem button onClick={() => { handleDrawerClose(); navigate('/account') }}>Account</StyledListItem>
                            <StyledListItem button onClick={() => { handleDrawerClose(); navigate('/transaction') }}>Transaction</StyledListItem>
                        </List>
                        <StyledListItem button sx={{paddingBlock: '10%'}} onClick={() => { handleDrawerClose(); auth.logOut() }} >LOGOUT</StyledListItem>
                    </Box>
                </Drawer>

                <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    My App
                </Typography>
            </Toolbar>
        );
    };

    return (
        <AppBar position="static" sx={{ background: '#ab893dc7', height: 64 }}>
            {mobileView ? displayMobile() : displayDesktop()}
        </AppBar>
    );
}


const StyledListItem = styled(ListItem)({
    color: '#fff'
});

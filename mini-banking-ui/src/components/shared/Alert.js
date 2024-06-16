import { useContext } from 'react';
import MuiAlert from '@mui/material/Alert';
import { AlertContext } from '../../context/AlertContext';
import { Snackbar } from '@mui/material';

function AlertComponent() {
  const { alert, setAlert } = useContext(AlertContext);

  const handleClose = () => {
    setAlert(prev => ({ ...prev, open: false }));
  };

  return (
    <Snackbar open={alert.open} autoHideDuration={6000} onClose={handleClose}>
      <MuiAlert onClose={handleClose} severity={alert.severity} variant="filled">
        {alert.message}
      </MuiAlert>
    </Snackbar>
  );
}

export default AlertComponent;
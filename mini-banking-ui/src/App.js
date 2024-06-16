import React from 'react';
import { Route, Routes } from 'react-router-dom';

import { Login } from './components/authentication/Login';
import { Register } from './components/authentication/Register';
import AuthProvider from './context/AuthProvider';
import PrivateRoute from './routes/PrivateRoute';
import { Account } from './components/accounts/Account';
import { Transaction } from './components/transactions/Transaction';
import { AlertProvider } from './context/AlertContext';
import AlertComponent from './components/shared/Alert';

function App() {
  return (

    <AuthProvider>
      <AlertProvider>
        <Routes>
          <Route path="/" exact element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route element={<PrivateRoute />}>
            <Route path="/account" element={<Account />} />
            <Route path="/transaction" element={<Transaction />} />
          </Route>
        </Routes>
        <AlertComponent />
      </AlertProvider>
    </AuthProvider>
  );
}

export default App;

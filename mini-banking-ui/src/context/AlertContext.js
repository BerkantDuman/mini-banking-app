// AlertContext.js
import React, { createContext, useState } from 'react';

export const AlertContext = createContext();

export const AlertProvider = props => {
  const [alert, setAlert] = useState({ open: false, message: '', severity: 'success' });

  return (
    <AlertContext.Provider value={{ alert, setAlert }}>
      {props.children}
    </AlertContext.Provider>
  );
};
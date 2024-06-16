import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const apiService = axios.create({
    baseURL: API_BASE_URL,
});

let logoutCallback = null;

export const setLogoutCallback = (cb) => {
  logoutCallback = cb;
}

apiService.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    Promise.reject(error)
});


apiService.interceptors.response.use(
    (response) => { return response }, 
    (error) => {
      if (error.response && (error.response.status === 403 || error.response.status === 401) && logoutCallback) {
        logoutCallback();
      }
      return Promise.reject(error);
    }
  );

export const getAccounts = async (accountFilter, pageable={pageNumber: 0, pageSize: 1}) => {
    try {
        return (await apiService.post(`/accounts/search?size=${pageable.pageSize}&page=${pageable.pageNumber}`, accountFilter)).data;
    } catch (error) {
        throw error;
    }
};

export const createAccount = async (account) => {
    try {
        return (await apiService.post('/accounts', account)).data;
    } catch (error) {
        throw error;
    }
};

export const makeTransfer = async (transfer) => {
    try {
        return (await apiService.post('/transactions/transfer', transfer)).data;
    } catch (error) {
        throw error;
    }
};


export const getTransactionHistory = async (accountId) => {
    try {
        return (await apiService.get(`/transactions/account/${accountId}`)).data;
    } catch (error) {
        throw error;
    }
};
import axios from "axios";
import { useContext, createContext, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/shared/Navbar";
import { jwtDecode } from 'jwt-decode';
import { setLogoutCallback } from "../services/ApiServices";

const AuthContext = createContext();

const parseUser = () => {
    if (localStorage.getItem("token"))
        return jwtDecode(localStorage.getItem("token"));
    else
        return null;


}

const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(parseUser());
    const [token, setToken] = useState(localStorage.getItem("token") || "");
    const navigate = useNavigate();

    const loginAction =  async (data) => {
        try {
            return axios.post("http://localhost:8080/api/users/login", data)
                .then(response => {
                    console.log("Response", response)
                    setToken(response.data.accessToken)
                    setUser(jwtDecode(response.data.accessToken))
                    localStorage.setItem("token", response.data.accessToken);
                })
        } catch (error) {
            throw error;
        }
    };

    const logOut = () => {
        setUser(null);
        setToken("");
        localStorage.removeItem("token");
        navigate("/");
    };

    useEffect(() => {
        setLogoutCallback(logOut);
      }, [logOut]);


    const register = async (data) => {
        try {
            const res = (await axios.post("http://localhost:8080/api/users/register", data)).data
            if (res) {
                return 200
            }

        } catch (err) {
            console.log(err);
            return "Email and username must be unique";
        }
    }

    return (
        <AuthContext.Provider value={{ token, user, loginAction, logOut, register }}>
            {token && <Navbar></Navbar>}
            {children}
        </AuthContext.Provider>
    );

};

export default AuthProvider;

export const useAuth = () => {
    return useContext(AuthContext);
};
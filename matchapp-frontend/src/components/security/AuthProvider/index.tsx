import {AxiosError} from "axios";
import {PropsWithChildren, useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {IUserInfo} from "@/interfaces/user";
import AccessTokenUtil from "@/utils/AccessTokenUtil";
import {IAuthRegister, IAuthRequest} from "@/interfaces/auth";
import AuthService from "@/services/AuthService";
import RefreshTokenUtil from "@/utils/RefreshTokenUtil";
import Paths from "@/constants/Paths";
import {ISpringValidationError} from "@/interfaces/spring";
import AuthContext from "@/contexts/security/AuthContext";

const AuthProvider = ({children}: PropsWithChildren) => {
  const [userInfo, setUserInfo] = useState<IUserInfo>();
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const navigate = useNavigate();
  const {pathname} = useLocation();
  const accessToken = AccessTokenUtil.token;
  const isAuthenticated = Boolean(accessToken);

  const login = async (authRequest: IAuthRequest) => {
    try {
      const authResponse = await AuthService.login(authRequest);
      AccessTokenUtil.token = authResponse.data.accessToken;
      RefreshTokenUtil.token = authResponse.data.refreshToken;
      const authInfo = await AuthService.getUserInfo();
      setUserInfo(authInfo.data);
      navigate(Paths.HOME_PATH);
    } catch (e) {
      return e as AxiosError<ISpringValidationError>;
    }
  }

  const register = async (authRegister: IAuthRegister) => {
    try {
      const authResponse = await AuthService.register(authRegister);
      AccessTokenUtil.token = authResponse.data.accessToken;
      RefreshTokenUtil.token = authResponse.data.refreshToken;
      const authInfo = await AuthService.getUserInfo();
      setUserInfo(authInfo.data);
      navigate(Paths.HOME_PATH);
    } catch (e) {
      return e as AxiosError<ISpringValidationError>;
    }
  }

  const logout = async () => {
    AccessTokenUtil.token = undefined;
    RefreshTokenUtil.token = undefined;
    setUserInfo(undefined);
    navigate(Paths.HOME_PATH);
  }

  const updateUserCredentials = async () => {
    setIsLoading(true);
    try {
      const userResponse = await AuthService.getUserInfo();
      setUserInfo(userResponse.data);
    } catch (e) {
      setUserInfo(undefined);
      return e as AxiosError<ISpringValidationError>;
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    if (!accessToken) {
      setUserInfo(undefined);
      setIsLoading(false);
    }
  }, [navigate, pathname, accessToken]);

  useEffect(() => {
    if (accessToken) updateUserCredentials();
  }, []);

  return (<AuthContext.Provider value={
    {
      userInfo,
      isAuthenticated,
      isLoading,
      login,
      register,
      update: updateUserCredentials,
      logout
    }
  }>{children}</AuthContext.Provider>);
}

export default AuthProvider;
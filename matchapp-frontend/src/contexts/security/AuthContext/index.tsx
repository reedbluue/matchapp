import {createContext} from "react";
import {IAuthRegister, IAuthRequest} from "@/interfaces/auth";
import {AxiosError} from "axios";
import {ISpringValidationError} from "@/interfaces/spring";
import {IUserInfo} from "@/interfaces/user";

export interface IAuthContextData {
  userInfo: IUserInfo | undefined;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (authLogin: IAuthRequest) => Promise<void | AxiosError<ISpringValidationError>>;
  register: (authRegister: IAuthRegister) => Promise<void | AxiosError<ISpringValidationError>>;
  update: () => Promise<void | AxiosError<ISpringValidationError>>;
  logout: () => Promise<void | AxiosError>;
}

const AuthContext = createContext<IAuthContextData>({} as IAuthContextData);

export default AuthContext;
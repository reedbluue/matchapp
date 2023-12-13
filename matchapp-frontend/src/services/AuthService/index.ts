import {IAuthRegister, IAuthRequest, IAuthResponse, IRefreshRequest} from "@/interfaces/auth";
import {baseHttp, clearHttp} from "@/configurations/AxiosConfig";
import {IUserInfo} from "@/interfaces/user";
import Paths from "@/constants/Paths";

abstract class AuthService {
  static getUserInfo() {
    return baseHttp.get<IUserInfo>(Paths.USER_INFO_PATH);
  }

  static login(authRequest: IAuthRequest) {
    return baseHttp.post<IAuthResponse>(Paths.AUTH_REQUEST_PATH, authRequest);
  }

  static register(authRegister: IAuthRegister) {
    return baseHttp.post<IAuthResponse>(Paths.AUTH_REGISTER_PATH, authRegister);
  }

  static refresh(refreshRequest: IRefreshRequest) {
    return clearHttp.post<IAuthResponse>(Paths.AUTH_REFRESH_PATH, refreshRequest);
  }
}

export default AuthService;
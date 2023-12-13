import {IAuthInfo} from "@/interfaces/auth";

abstract class AuthUtil {
  static validateUserRole(roles?: string[], authInfo?: IAuthInfo) {
    if (!roles) return true;
    if (!authInfo) return false;
    return roles.some(role => authInfo.roles.includes(role));
  }
}

export default AuthUtil;
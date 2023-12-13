abstract class AccessTokenUtil {
  private static ACCESS_TOKEN_KEY = "STOMPY_ACCESS_TOKEN";

  public static get token(): string | undefined {
    return sessionStorage.getItem(AccessTokenUtil.ACCESS_TOKEN_KEY) ?? undefined;
  }

  public static set token(token: string | undefined) {
    if (!token) {
      sessionStorage.removeItem(AccessTokenUtil.ACCESS_TOKEN_KEY);
    } else {
      sessionStorage.setItem(AccessTokenUtil.ACCESS_TOKEN_KEY, token);
    }
  }
}

export default AccessTokenUtil;
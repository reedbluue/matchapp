abstract class RefreshTokenUtil {
  private static REFRESH_TOKEN_KEY = "STOMPY_REFRESH_TOKEN";

  public static get token(): string | undefined {
    return sessionStorage.getItem(RefreshTokenUtil.REFRESH_TOKEN_KEY) ?? undefined;
  }

  public static set token(token: string | undefined) {
    if (!token) {
      sessionStorage.removeItem(RefreshTokenUtil.REFRESH_TOKEN_KEY);
    } else {
      sessionStorage.setItem(RefreshTokenUtil.REFRESH_TOKEN_KEY, token);
    }
  }
}

export default RefreshTokenUtil;
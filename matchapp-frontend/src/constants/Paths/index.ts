abstract class Paths {
  public static readonly BASE_URL = import.meta.env.VITE_BASE_URL;

  public static readonly USER_INFO_PATH = "/api/user/info";
  public static readonly AUTH_REFRESH_PATH = "/api/auth/refresh";
  public static readonly AUTH_REGISTER_PATH = "/api/auth/register";
  public static readonly AUTH_REQUEST_PATH = "/api/auth/login";

  public static readonly USER_PROFILE_IMAGE_PATH = "/api/user-image/profile";
  public static readonly USER_COMPATIBLE_PATH = "/api/user/compatible";

  public static readonly SKILL_SEPARATED_AREA_PATH = "/api/skill/separated-by-area";
  public static readonly TEACH_SKILL_UPDATE_PATH = "/api/user-skill/teach";
  public static readonly LEARN_SKILL_UPDATE_PATH = "/api/user-skill/learn";

  public static readonly MATCH_ACCEPT_PATH = "/api/match/accept";
  public static readonly MATCH_REJECT_PATH = "/api/match/reject";
  public static readonly ALL_MATCHES_ACCEPTED_PATH = "/api/match/accepted";

  public static readonly HOME_PATH = "/";
  public static readonly LOGIN_PATH = "/login";
  public static readonly REGISTER_PATH = "/register";
  public static readonly PRIVATE_PATH = "/private";
  public static readonly MATCH_PATH = "/match";
  public static readonly PROFILE_PATH = "/profile";
  public static readonly CHAT_PATH = "/chat";
}

export default Paths;
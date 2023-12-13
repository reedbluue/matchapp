import axios from "axios";
import createAuthRefreshInterceptor from "axios-auth-refresh";
import Paths from "@/constants/Paths";
import AccessTokenUtil from "@/utils/AccessTokenUtil";
import AuthService from "@/services/AuthService";
import RefreshTokenUtil from "@/utils/RefreshTokenUtil";

export const clearHttp = axios.create({
  baseURL: Paths.BASE_URL
});

export const baseHttp = axios.create({
  baseURL: Paths.BASE_URL
});

baseHttp.interceptors.request.use(config => {
  const token = AccessTokenUtil.token;
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

createAuthRefreshInterceptor(baseHttp, async err => {
  const refreshToken = RefreshTokenUtil.token;
  if (!refreshToken) return Promise.reject(err);

  try {
    const res = await AuthService.refresh({refreshToken});
    AccessTokenUtil.token = res.data.accessToken;
    RefreshTokenUtil.token = res.data.refreshToken;
    return Promise.resolve();
  } catch (e) {
    AccessTokenUtil.token = undefined;
    RefreshTokenUtil.token = undefined;
    return Promise.reject(e);
  }
});
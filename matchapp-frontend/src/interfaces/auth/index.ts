export interface IAuthResponse {
  accessToken: string;
  refreshToken: string;
}

export interface IAuthRequest {
  email: string;
  password: string;
}

export interface IAuthRegister {
  email: string;
  fullName: string;
  password: string;
  birthDate: string; // yyyy-MM-dd
}

export interface IRefreshRequest {
  refreshToken: string;
}

export interface IAuthInfo {
  id: number;
  email: string;
  fullName: string;
  username: string;
  roles: string[];
}
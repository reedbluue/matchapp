import {baseHttp} from "@/configurations/AxiosConfig";
import Paths from "@/constants/Paths";
import {IUserBasic} from "@/interfaces/user";

abstract class UserService {
  static getUserProfileImage(userId: number) {
    return baseHttp.get<ArrayBuffer>(`${Paths.USER_PROFILE_IMAGE_PATH}/${userId}`,
        {responseType: "arraybuffer"});
  }

  static setUserProfileImage(image: File) {
    const formData = new FormData();
    formData.append("file", image);
    return baseHttp.post<void>(Paths.USER_PROFILE_IMAGE_PATH, formData);
  }

  static getCompatibleUsers() {
    return baseHttp.get<IUserBasic[]>(Paths.USER_COMPATIBLE_PATH);
  }
}

export default UserService;
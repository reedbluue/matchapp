import TextUtil from "@/utils/TextUtil";
import {useEffect, useState} from "react";
import UserService from "@/services/UserService";
import ImageUtil from "@/utils/ImageUtil";
import {IUserInfo} from "@/interfaces/user";

interface IBigAvatarProps {
  userInfo?: IUserInfo,
  className?: string
}

const BigAvatar = ({userInfo, className}: IBigAvatarProps) => {
  const [profilePicture, setProfilePicture] = useState<string>();

  useEffect(() => {
    const getProfilePicture = async () => {
      if (userInfo) {
        try {
          const {data: arrayBuffer} = await UserService.getUserProfileImage(userInfo.authInfo.id);
          const base64 = ImageUtil.arrayBufferToBase64(arrayBuffer);
          setProfilePicture(`data:image/jpeg;base64,${base64}`);
        } catch (e) {
          setProfilePicture(undefined);
        }
      } else {
        setProfilePicture(undefined);
      }
    }
    getProfilePicture();
  }, [userInfo]);

  return (
      <div className={className ?? ""}>
        {!userInfo &&
            <div className="avatar placeholder">
              <div className="mask mask-squircle bg-neutral text-neutral-content w-36">
                <span className="loading loading-spinner loading-lg"></span>
              </div>
            </div>
        }
        {userInfo && profilePicture &&
            <div className="avatar">
              <div className="mask mask-squircle bg-neutral text-neutral-content w-36">
                <img alt="profile picture" src={profilePicture} className={"m-0"}/>
              </div>
            </div>
        }
        {userInfo && !profilePicture &&
            <div className="avatar placeholder">
              <div className="mask mask-squircle bg-neutral text-neutral-content w-36">
                    <span className="text-3xl scale-150 select-none">{TextUtil.getUpperCaseInitials(
                        userInfo.authInfo.fullName)}</span>
              </div>
            </div>
        }
      </div>
  );
}

export default BigAvatar;
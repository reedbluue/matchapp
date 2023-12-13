import TextUtil from "@/utils/TextUtil";
import {useEffect, useState} from "react";
import UserService from "@/services/UserService";
import ImageUtil from "@/utils/ImageUtil";
import {IUserBasic} from "@/interfaces/user";
import classNames from "classnames";

interface ISmallAvatarProps {
  userInfo?: IUserBasic,
  className?: string,
  online: boolean,
  onClick?: () => void
}

const SmallAvatar = ({userInfo, className, online, onClick}: ISmallAvatarProps) => {
  const [profilePicture, setProfilePicture] = useState<string>();

  useEffect(() => {
    const getProfilePicture = async () => {
      if (userInfo) {
        try {
          const {data: arrayBuffer} = await UserService.getUserProfileImage(userInfo.id);
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
      <div className={"flex-none active:scale-95 transition-all m-auto cursor-pointer " + className || ""}
           onClick={onClick}>
        {!userInfo &&
            <div className={"avatar placeholder " + classNames({"online": online})}>
              <div className="bg-neutral text-neutral-content rounded-full w-10">
                <span className="loading loading-spinner"></span>
              </div>
            </div>
        }
        {userInfo && profilePicture &&
            <div className={"avatar " + classNames({"online": online})}>
              <div className="bg-neutral text-neutral-content rounded-full w-10">
                <img alt="profile picture" src={profilePicture} className={"m-0"}/>
              </div>
            </div>
        }
        {userInfo && !profilePicture &&
            <div className={"avatar placeholder " + classNames({"online": online})}>
              <div className="bg-neutral text-neutral-content rounded-full w-10">
                    <span className="text-xl select-none">{TextUtil.getUpperCaseInitials(
                        userInfo.fullName)}</span>
              </div>
            </div>
        }
      </div>
  );
}

export default SmallAvatar;
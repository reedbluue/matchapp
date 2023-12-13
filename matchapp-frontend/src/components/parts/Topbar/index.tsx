import General from "@/constants/General";
import {useNavigate} from "react-router-dom";
import Paths from "@/constants/Paths";
import {useContext} from "react";
import AuthContext from "@/contexts/security/AuthContext";
import classNames from "classnames";
import SmallAvatar from "@/components/parts/ProfileImage/SmallAvatar";

const Topbar = () => {
  const navigate = useNavigate();
  const {userInfo, isAuthenticated} = useContext(AuthContext);

  return (
      <div className="navbar flex-none">
        <div className={"menu menu-horizontal w-full bg-base-200 rounded-box"}>
          <div className={"flex-1 flex " + classNames({"justify-center": !isAuthenticated})}>
            <button className="btn btn-ghost text-xl"
                    onClick={() => navigate(Paths.HOME_PATH)}>{General.APP_NAME}</button>
          </div>
          {isAuthenticated && <SmallAvatar online={true} userInfo={
            userInfo ? {
              id: userInfo.authInfo.id,
              fullName: userInfo.authInfo.fullName,
              userSkillSet: userInfo.userSkillSet
            } : undefined
          } onClick={() => navigate(Paths.PROFILE_PATH)}/>}
        </div>
      </div>
  );
}

export default Topbar;
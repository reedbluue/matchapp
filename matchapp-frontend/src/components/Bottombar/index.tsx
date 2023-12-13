import AuthContext from "@/contexts/security/AuthContext";
import {useContext} from "react";
import AuthBottombarItems from "@/components/Bottombar/AuthBottombarItems";
import NoAuthBottombarItems from "@/components/Bottombar/NoAuthBottombarItems";

const Bottombar = () => {
  const {isLoading, isAuthenticated} = useContext(AuthContext);

  return (
      <div className="navbar flex-none">
        <ul className="menu menu-horizontal bg-base-200 rounded-box w-full justify-around">
          {isLoading && <li><span className={"btn loading loading-dots"}></span></li>}
          {!isLoading && isAuthenticated && <AuthBottombarItems/>}
          {!isLoading && !isAuthenticated && <NoAuthBottombarItems/>}
        </ul>
      </div>
  );
}

export default Bottombar;
import {PropsWithChildren, useContext} from "react";
import AuthContext from "@/contexts/security/AuthContext";
import AuthUtil from "@/utils/AuthUtil";

interface ICanAccessProps extends PropsWithChildren {
  roles: string[];
}

const CanAccess = ({roles, children}: ICanAccessProps) => {
  const {isAuthenticated, userInfo} = useContext(AuthContext);
  const isValidRole = AuthUtil.validateUserRole(roles, userInfo?.authInfo);

  if (!userInfo || !isAuthenticated || !isValidRole) return null;

  return children;
}

export default CanAccess;
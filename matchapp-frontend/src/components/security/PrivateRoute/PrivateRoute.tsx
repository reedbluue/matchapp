import {PropsWithChildren, Suspense, useContext} from "react";
import {Navigate} from "react-router-dom";
import {ErrorBoundary} from "react-error-boundary";
import Paths from "@/constants/Paths";
import AuthContext from "@/contexts/security/AuthContext";
import AuthUtil from "@/utils/AuthUtil";
import ErrorPage from "@/components/pages/ErrorPage";
import LoadingPage from "@/components/pages/LoadingPage";

interface IPrivateRouteProps extends PropsWithChildren {
  roles: string[];
  redirectTo?: string;
}

const PrivateRoute = ({children, roles, redirectTo = Paths.LOGIN_PATH}: IPrivateRouteProps) => {
  const {isAuthenticated, userInfo, isLoading} = useContext(AuthContext);
  const isValidRole = AuthUtil.validateUserRole(roles, userInfo?.authInfo);

  if (isLoading) return null;

  if (!userInfo || !isAuthenticated) return <Navigate to={redirectTo}/>;

  if (!isValidRole) return <Navigate to={Paths.HOME_PATH}/>;

  return (
      <ErrorBoundary fallback={<ErrorPage/>}>
        <Suspense fallback={<LoadingPage/>}>
          {children}
        </Suspense>
      </ErrorBoundary>
  );
};

export default PrivateRoute;
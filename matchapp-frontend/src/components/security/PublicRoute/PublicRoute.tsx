import {PropsWithChildren, Suspense, useContext} from "react";
import {Navigate} from "react-router-dom";
import {ErrorBoundary} from "react-error-boundary";
import AuthContext from "@/contexts/security/AuthContext";
import Paths from "@/constants/Paths";
import LoadingPage from "@/components/pages/LoadingPage";
import ErrorPage from "@/components/pages/ErrorPage";

const PublicRoute = ({children}: PropsWithChildren) => {
  const {isAuthenticated} = useContext(AuthContext);

  if (isAuthenticated) return <Navigate to={Paths.MATCH_PATH}/>

  return (
      <ErrorBoundary fallback={<ErrorPage/>}>
        <Suspense fallback={<LoadingPage/>}>
          {children}
        </Suspense>
      </ErrorBoundary>
  );
}

export default PublicRoute;
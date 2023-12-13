import {createBrowserRouter} from "react-router-dom";
import Paths from "@/constants/Paths";
import HomePage from "@/components/pages/HomePage";
import PublicRoute from "@/components/security/PublicRoute/PublicRoute.tsx";
import LoginPage from "@/components/pages/LoginPage";
import PrivateRoute from "@/components/security/PrivateRoute/PrivateRoute.tsx";
import RegisterPage from "@/components/pages/RegisterPage";
import MatchPage from "@/components/pages/MatchPage";
import Roles from "@/enums/Roles";
import Base from "@/components/pages/Base";
import ProfilePage from "@/components/pages/ProfilePage";
import ChatPage from "@/components/pages/ChatPage";

const Router = createBrowserRouter([
  {
    path: Paths.HOME_PATH,
    element: <Base/>,
    children: [
      {
        index: true,
        element: <PublicRoute><HomePage/></PublicRoute>
      },
      {
        path: Paths.LOGIN_PATH,
        element: <PublicRoute><LoginPage/></PublicRoute>
      },
      {
        path: Paths.REGISTER_PATH,
        element: <PublicRoute><RegisterPage/></PublicRoute>
      },
      {
        path: Paths.MATCH_PATH,
        element:
            <PrivateRoute roles={[Roles.ROLE_USER, Roles.ROLE_ADMIN]} redirectTo={Paths.LOGIN_PATH}>
              <MatchPage/>
            </PrivateRoute>
      },
      {
        path: Paths.PROFILE_PATH,
        element:
            <PrivateRoute roles={[Roles.ROLE_USER, Roles.ROLE_ADMIN]} redirectTo={Paths.LOGIN_PATH}>
              <ProfilePage/>
            </PrivateRoute>
      },
      {
        path: Paths.CHAT_PATH,
        element:
            <PrivateRoute roles={[Roles.ROLE_USER, Roles.ROLE_ADMIN]} redirectTo={Paths.LOGIN_PATH}>
              <ChatPage/>
            </PrivateRoute>
      },
    ]
  }
]);

export default Router;
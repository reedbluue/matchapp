import {Outlet} from "react-router-dom";
import {Toaster} from "react-hot-toast";
import AuthProvider from "@/components/security/AuthProvider";
import Bottombar from "@/components/Bottombar";
import Topbar from "@/components/parts/Topbar";

const Base = () => {
  return (
      <AuthProvider>
        <div className={"flex flex-col h-screen w-screen"}>
          <Topbar/>
          <div
              className={"flex flex-1 flex-grow mx-2 rounded-box p-3 bg-neutral/30 shadow-md overflow-y-scroll justify-center"}>
            <Outlet/>
          </div>
          <Toaster
              position="top-center"
              reverseOrder={false}
          />
          <Bottombar/>
        </div>
      </AuthProvider>
  );
}

export default Base;
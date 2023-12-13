import {HiLogin, HiPencilAlt} from "react-icons/hi";
import {useNavigate} from "react-router-dom";
import Paths from "@/constants/Paths";

const NoAuthBottombarItems = () => {
  const navigate = useNavigate();

  return (
      <>
        <li>
          <a className={"btn btn-ghost cursor-pointer"} onClick={() => navigate(Paths.LOGIN_PATH)}>
            <HiLogin size={28}/>
            Entrar
          </a>
        </li>
        <li>
          <a className={"btn btn-ghost cursor-pointer"} onClick={() => navigate(Paths.REGISTER_PATH)}>
            <HiPencilAlt size={28}/>
            Registrar
          </a>
        </li>
      </>
  );
}

export default NoAuthBottombarItems;
import {HiChatAlt2, HiNewspaper} from "react-icons/hi";
import {HiGlobeEuropeAfrica} from "react-icons/hi2";
import {useNavigate} from "react-router-dom";
import Paths from "@/constants/Paths";

const AuthBottombarItems = () => {
  const navigate = useNavigate();

  return (
      <>
        <li>
          <a className={"btn btn-ghost"} onClick={() => navigate(Paths.MATCH_PATH)}>
            <HiGlobeEuropeAfrica size={28}/>
            Match
          </a>
        </li>
        <li>
          <a className={"btn btn-ghost"} onClick={() => navigate(Paths.CHAT_PATH)}>
            <HiChatAlt2 size={28}/>
            Chat
            <span className="badge badge-primary badge-xs"></span>
          </a>
        </li>
        <li>
          <a className={"btn btn-ghost"}>
            <HiNewspaper size={28}/>
            Fórum
          </a>
        </li>
      </>
  );
}

export default AuthBottombarItems;
import AuthContext from "@/contexts/security/AuthContext";
import {useContext, useEffect, useState} from "react";
import TextUtil from "@/utils/TextUtil";
import {IMatchBasic} from "@/interfaces/match";
import MatchService from "@/services/MatchService";
import toast from "react-hot-toast";
import SmallAvatar from "@/components/parts/ProfileImage/SmallAvatar";
import {HiOutlineEmojiSad} from "react-icons/hi";

const ChatPage = () => {
  const {userInfo} = useContext(AuthContext);
  const [matches, setMatches] = useState<IMatchBasic[]>([]);

  useEffect(() => {
    const getMatches = async () => {
      try {
        const matches = await MatchService.getAllAcceptedMatches();
        setMatches(matches.data);
      } catch (e) {
        toast.error("Algo deu errado, tente novamente mais tarde");
      }
    }
    getMatches();
  }, []);

  return (
      <div className={"flex flex-col prose prose-sm flex-grow"}>
        <div className={"flex-none flex flex-col gap-6"}>
          {matches.map(match => {
            const secondUser = match.userInfo1.user.id == userInfo?.authInfo.id
                ? match.userInfo2.user : match.userInfo1.user;
            return (
                <div className={"card shadow-md p-4 gap-4 flex-row bg-gray-700 active:scale-95 transition-all"}
                     key={match.id + "match"}>
                  <SmallAvatar userInfo={secondUser} online={true}/>
                  <div className={"flex-grow flex flex-col gap-1"}>
                    <h3 className={"m-0"}>{TextUtil.getSignificantNames(secondUser.fullName)}</h3>
                    <div className={"flex-grow flex gap-2"}>
                      <span className={"badge badge-warning badge-sm"}>{match.userInfo1.skillToLearn.name}</span>
                      <span className={"badge badge-info badge-sm"}>{match.userInfo2.skillToLearn.name}</span>
                    </div>
                  </div>
                </div>
            );
          })}
        </div>
        {matches.length == 0 &&
            <div className={"h-full w-full flex flex-col justify-center items-center"}>
              <HiOutlineEmojiSad  size={38}/>
              <h3>Está meio vazio por aqui...</h3>
            </div>
        }
        <div className={"flex-grow"}></div>
      </div>
  );
}

export default ChatPage;
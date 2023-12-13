import {HiHeart, HiXCircle} from "react-icons/hi2";
import {useEffect, useState} from "react";
import {IUserBasic} from "@/interfaces/user";
import UserService from "@/services/UserService";
import ImageUtil from "@/utils/ImageUtil";
import MatchUserDisplay from "@/components/parts/MatchUserDisplay";
import toast from "react-hot-toast";
import MatchService from "@/services/MatchService";
import SkillMatchSelectModal from "@/components/parts/SkillMatchSelectModal";

const MatchPage = () => {
  const [users, setUsers] = useState<(IUserBasic & { imageBase64?: string })[]>([]);
  const actualUser = users[0] ?? undefined;

  useEffect(() => {
    const getUsers = async () => {
      const {data: users} = await UserService.getCompatibleUsers();
      const usersWithImage = [] as (IUserBasic & { imageBase64?: string })[];

      for (const user of users) {
        try {
          const {data: arrayBuffer} = await UserService.getUserProfileImage(user.id);
          const base64 = ImageUtil.arrayBufferToBase64(arrayBuffer);
          usersWithImage.push({
            ...user,
            imageBase64: `data:image/jpeg;base64,${base64}`
          })
        } catch (e) {
          usersWithImage.push(user);
        }
      }
      setUsers(usersWithImage);
    }
    getUsers();
  }, []);

  const popUser = () => {
    if (users.length > 0) {
      setUsers(users.slice(1));
    }
  }

  const rejectHandler = async () => {
    if (actualUser) {
      try {
        await MatchService.rejectMatch({secondaryUserId: actualUser.id});
        popUser();
      } catch (e) {
        toast.error("Algo deu errado, tente novamente mais tarde!");
      }
    }
  }

  return (
      <div className={"flex-grow flex flex-col prose prose-md gap-3"}>
        <SkillMatchSelectModal id={"match-select-modal"} user={actualUser} popUser={popUser}/>
        <MatchUserDisplay key={actualUser?.id} user={actualUser}/>
        <div className={"flex justify-around gap-5"}>
          <button className={"btn btn-circle flex-grow"} onClick={() => (document.getElementById(
              "match-select-modal") as any)?.showModal()}><HiHeart size={28}/></button>
          <button className={"btn btn-circle flex-grow"}><span className={"bold text-lg"}>15</span>
          </button>
          <button className={"btn btn-circle flex-grow"} onClick={rejectHandler}><HiXCircle
              size={28}/></button>
        </div>
      </div>
  );
}

export default MatchPage;
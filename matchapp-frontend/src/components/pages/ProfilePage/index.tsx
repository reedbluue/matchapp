import AuthContext from "@/contexts/security/AuthContext";
import {ChangeEventHandler, useContext, useRef} from "react";
import BigAvatar from "@/components/parts/ProfileImage/BigAvatar";
import SkillUpdateModal from "@/components/parts/SkillUpdateModal";
import SkillType from "@/enums/SkillType";
import {HiMiniPencilSquare} from "react-icons/hi2";
import toast from "react-hot-toast";
import UserService from "@/services/UserService";
import TextUtil from "@/utils/TextUtil";

const ProfilePage = () => {
  const {userInfo, logout, update} = useContext(AuthContext);
  const inputFile = useRef<HTMLInputElement>(null);

  const handleFileUpload: ChangeEventHandler<HTMLInputElement> = async e => {
    if (e.target.files) {
      const file = e.target.files[0];
      if (file.type != "image/jpeg" || file.type != "image/jpeg") {
        toast.error("Selecione uma imagem JPG ou JPEG");
        return;
      }
      try {
        await UserService.setUserProfileImage(file);
        await update();
        toast.success("Foto de perfil alterada com sucesso!");
      } catch (e) {
        toast.error("Algo deu errado, tente novamente mais tarde!");
      }
    }
  }

  return (
      <div className={"flex flex-col prose prose-sm flex-grow"}>
        <h1 className={"text-center flex-none"}>{TextUtil.getSignificantNames(
            userInfo?.authInfo?.fullName ?? "")}</h1>

        <SkillUpdateModal id={"teachSkillUpdateModal"} mode={SkillType.TEACH}/>
        <SkillUpdateModal id={"learnSkillUpdateModal"} mode={SkillType.LEARN}/>
        <input type='file' ref={inputFile} className={"hidden"} onChange={handleFileUpload}
               accept={".jpg, .jpeg"}/>

        <div className={"flex-none flex flex-col gap-6"}>
          <button onClick={() => inputFile.current?.click()}
                  className={"m-auto active:scale-95 transition indicator"}>
            <div className="indicator-item indicator-bottom">
              <HiMiniPencilSquare size={28}/>
            </div>
            <BigAvatar userInfo={userInfo}/>
          </button>
          <div className={"flex-none flex flex-col gap-2"}>
            <div className={"card bg-neutral shadow-md p-4 gap-4"}>
              <div className={"flex justify-between"}>
                <h2 className={"m-0"}>Para ensinar:</h2>
                <button onClick={() => (document.getElementById(
                    "teachSkillUpdateModal") as any)?.showModal()}
                        className={"active:scale-95 transition w-min"}><HiMiniPencilSquare
                    size={28}/>
                </button>
              </div>
              {userInfo?.userSkillSet.teachSkills &&
                  <div className={"flex flex-wrap gap-2"}>
                    {userInfo?.userSkillSet.teachSkills.map((ts) => <span key={ts.id + ts.name}
                                                                          className={"badge badge-info"}>{ts.name}</span>)}
                  </div>
              }
              {!userInfo?.userSkillSet.teachSkills && <p className={"m-0"}>Você ainda não escolheu
                nada
                para ensinar</p>}
            </div>

            <div className={"card bg-neutral shadow-md p-4 gap-4"}>
              <div className={"flex justify-between"}>
                <h2 className={"m-0"}>Para aprender:</h2>
                <button onClick={() => (document.getElementById(
                    "learnSkillUpdateModal") as any)?.showModal()}
                        className={"active:scale-95 transition w-min"}><HiMiniPencilSquare
                    size={28}/>
                </button>
              </div>
              {userInfo?.userSkillSet.learnSkills &&
                  <div className={"flex flex-wrap gap-2"}>
                    {userInfo?.userSkillSet.learnSkills.map((ts) => <span key={ts.id + ts.name}
                                                                          className={"badge badge-warning"}>{ts.name}</span>)}
                  </div>
              }
              {!userInfo?.userSkillSet.learnSkills && <p className={"m-0"}>Você ainda não escolheu
                nada
                para ensinar</p>}
            </div>
          </div>
        </div>

        <div className={"flex-grow"}></div>

        <button className={"btn btn-error flex-none"} onClick={() => logout()}>Sair</button>
      </div>
  );
}

export default ProfilePage;
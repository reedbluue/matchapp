import toast from "react-hot-toast";
import {useForm} from "react-hook-form";
import {IUserBasic} from "@/interfaces/user";
import MatchService from "@/services/MatchService";
import {useState} from "react";
import classNames from "classnames";

interface ISkillMatchSelectModalProps {
  id: string;
  user: IUserBasic;
  popUser: () => void;
}

interface IMatchAcceptFormData {
  skillId: number;
}

const SkillMatchSelectModal = ({id, user, popUser}: ISkillMatchSelectModalProps) => {
  const {
    register,
    handleSubmit,
    watch,
    formState: {isSubmitting}
  } = useForm<IMatchAcceptFormData>();

  const {skillId} = watch();
  const [matchAnimation, setMatchAnimation] = useState(false);
  const [lockAnimation, setLockAnimation] = useState(false);

  const matchHandler = async () => {
    setMatchAnimation(true);
    setLockAnimation(true);
    setTimeout(() => {
      setLockAnimation(false);
    }, 2500);
  }

  const onSubmit = async (data: IMatchAcceptFormData) => {
    try {
      const response = await MatchService.acceptMatch({
        secondaryUserId: user.id,
        skillToLearnId: data.skillId
      });
      console.log(response.status)
      if (response.status === 204) {
        matchHandler();
      } else {
        popUser();
      }
    } catch (e) {
      toast.error("Algo deu errado, tente novamente mais tarde!");
    } finally {
      (document.getElementById(id) as any)?.close();
    }
  }

  if (!user) return null;

  return (
      <>
        {matchAnimation &&
            <div
                className={"cursor-pointer h-screen w-screen fixed top-0 left-0 z-40 bg-base-300 flex justify-center items-center select-none animate-fade animate-once animate-duration-500 animate-ease-out animate-fill-forwards"}
                onClick={() => {
                  if (!lockAnimation) {
                    setMatchAnimation(false);
                    popUser();
                  }
                }}>
              <h1 className={"text-center m-0 animate-jump animate-jump-in animate-once animate-duration-[2000ms] animate-fill-forwards"}>Foi
                um Match!</h1>
            </div>
        }
        <dialog id={id} className="modal prose prose-sm">
          <div className="modal-box flex flex-col gap-5">
            <form className={"form-control space-y-5"} onSubmit={handleSubmit(onSubmit)}>
              <h2 className={"m-0"}>Selecione o que deseja aprender:</h2>
              <div className={"flex flex-wrap gap-2"}>
                {user.userSkillSet.teachSkills.map(skill => (
                    <label key={skill.id} className="cursor-pointer join flex-grow">
                    <span
                        className={"badge badge-neutral join-item flex-grow h-full"}>{skill.name}</span>
                      <input type="radio" id={"skillId"} {...register("skillId")} value={skill.id}
                             className="radio radio-lg join-item"
                      />
                    </label>
                ))}
              </div>
              <button className={"btn btn-primary " + classNames({
                "btn-disabled": !skillId
              })} disabled={!skillId}>{isSubmitting ? <span
                  className={"loading loading-dots"}></span> : "Match!"}</button>
            </form>
            <button className={"btn btn-sm btn-block"} onClick={() => (document.getElementById(
                id) as any)?.close()}>{"Voltar"}</button>
          </div>
        </dialog>
      </>
  )
      ;
}

export default SkillMatchSelectModal;
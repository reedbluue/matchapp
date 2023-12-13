import {useContext, useEffect, useState} from "react";
import SkillService from "@/services/SkillService";
import {ISkillSeparetedByArea} from "@/interfaces/skill";
import toast from "react-hot-toast";
import {useForm} from "react-hook-form";
import classNames from "classnames";
import AuthContext from "@/contexts/security/AuthContext";
import SkillType from "@/enums/SkillType";

interface ISkillUpdateModalProps {
  id: string;
  mode: SkillType;
}

interface ISkillUpdateFormData {
  skillsIds: number[];
}

const SkillUpdateModal = ({id, mode}: ISkillUpdateModalProps) => {
  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: {isSubmitting}
  } = useForm<ISkillUpdateFormData>(
      {defaultValues: {skillsIds: []}});
  const [skillsSeparated, setSkillsSeparated] = useState<ISkillSeparetedByArea[]>([]);
  const {userInfo, update} = useContext(AuthContext);
  const skillsIds = watch("skillsIds");

  const isSelected = (id: number) => {
    return skillsIds.join("").includes(id.toString());
  }

  const isOtherSide = (id: number) => {
    if (!userInfo) return true;
    if (mode === SkillType.TEACH) {
      return userInfo.userSkillSet.learnSkills.some(ls => ls.id === id);
    } else if (mode === SkillType.LEARN) {
      return userInfo.userSkillSet.teachSkills.some(ls => ls.id === id);
    }
  }

  useEffect(() => {
    const getSkills = async () => {
      try {
        setSkillsSeparated((await SkillService.getAllSkillsSeparatedByArea()).data);
      } catch (e) {
        toast.error("Algo deu errado, tente novamente mais tarde");
      }
    }
    getSkills();
  }, []);

  useEffect(() => {
    if (userInfo) {
      if (mode === SkillType.LEARN) {
        setValue("skillsIds", userInfo.userSkillSet.learnSkills.map(ls => ls.id));
      } else if (mode === SkillType.TEACH) {
        setValue("skillsIds", userInfo.userSkillSet.teachSkills.map(ls => ls.id));
      }
    }
  }, [userInfo]);

  const onSubmit = async (data: ISkillUpdateFormData) => {
    console.log(data);
    if (isSubmitting) return;
    if (data.skillsIds.length === 0) {
      toast.error("Selecione pelo menos um item!");
      return;
    }
    try {
      await SkillService.updateSkills(data.skillsIds, mode);
      toast.success("Sucesso!");
    } catch (e) {
      console.log(e)
      toast.error("Algo deu errado, tente novamente mais tarde");
    } finally {
      update();
      (document.getElementById(id) as any)?.close();
    }
  }

  return (
      <dialog id={id} className="modal prose prose-sm">
        <div className="modal-box flex flex-col gap-2">
          <form className={"form-control space-y-5"} onSubmit={handleSubmit(onSubmit)}>
            {mode === "TEACH" && <h2 className={"m-0"}>Selecione o que deseja ensinar:</h2>}
            {mode === "LEARN" && <h2 className={"m-0"}>Selecione o que deseja aprender:</h2>}
            <div className={"flex flex-wrap gap-5 overflow-y-scroll"}>
              {skillsSeparated.map(sa => (
                  <div key={sa.skillArea.id + sa.skillArea.name}>
                    <label className="label">
                      <span className="label-text">{sa.skillArea.name}</span>
                    </label>
                    <div className={"flex flex-wrap gap-2"}>
                      {sa.skills.map(skill => (
                          <label key={skill.id} className="cursor-pointer join">
                            <span className={"badge badge-neutral join-item " + classNames(
                                {
                                  "badge-success": isSelected(skill.id),
                                  "badge-error": isOtherSide(skill.id)
                                })}>{skill.name}</span>
                            <input type="checkbox" {...register("skillsIds")} value={skill.id}
                                   className="checkbox checkbox-sm join-item"
                                   disabled={(skillsIds.length
                                       >= 2
                                       && !isSelected(skill.id)
                                       || isOtherSide(skill.id))}
                                   checked={isSelected(skill.id)}
                            />
                          </label>
                      ))}
                    </div>
                  </div>
              ))}
            </div>
            <span className={""}>{`* As skills em vermelho são skills já escolhidas para ${mode
            === SkillType.TEACH ? "aprender" : "ensinar"}!`}</span>
            <button className={"btn btn-primary"}>{isSubmitting ? <span
                className={"loading loading-dots"}></span> : "Salvar"}</button>
          </form>
          <button className={"btn btn-sm btn-block"} onClick={() => {
            update();
            return (document.getElementById(id) as any)?.close();
          }}>{"Voltar"}</button>
        </div>
      </dialog>
  );
}

export default SkillUpdateModal;
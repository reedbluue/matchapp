import {baseHttp} from "@/configurations/AxiosConfig";
import Paths from "@/constants/Paths";
import {ISkillSeparetedByArea} from "@/interfaces/skill";
import SkillType from "@/enums/SkillType";

abstract class SkillService {
  static getAllSkillsSeparatedByArea() {
    return baseHttp.get<ISkillSeparetedByArea[]>(Paths.SKILL_SEPARATED_AREA_PATH);
  }

  static updateSkills(skillsIds: number[], mode: SkillType) {
    const params = new URLSearchParams();
    skillsIds.forEach(si => params.append("skillsIds", si.toString()));

    if (mode === SkillType.TEACH) {
      return baseHttp.put<void>(Paths.TEACH_SKILL_UPDATE_PATH, {}, {params});
    } else if (mode === SkillType.LEARN) {
      return baseHttp.put<void>(Paths.LEARN_SKILL_UPDATE_PATH, {}, {params});
    }
  }
}

export default SkillService;
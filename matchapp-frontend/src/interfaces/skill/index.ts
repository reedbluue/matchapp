import {ISkillArea} from "@/interfaces/skillArea";

export interface ISkill {
  id: number;
  name: string;
  description: string;
  skillArea: ISkillArea;
}

export interface ISkillSeparetedByArea {
  skillArea: ISkillArea;
  skills: Array<ISkill>;
}
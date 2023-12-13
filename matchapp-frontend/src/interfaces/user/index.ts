import {IAuthInfo} from "@/interfaces/auth";
import {ISkill} from "@/interfaces/skill";

export interface IUserBasic {
  id: number;
  fullName: string;
  userSkillSet: IUserSkillSet;
}

export interface IUserInfo {
  authInfo: IAuthInfo;
  userSkillSet: IUserSkillSet;
}

export interface IUserSkillSet {
  learnSkills: ISkill[];
  teachSkills: ISkill[];
}
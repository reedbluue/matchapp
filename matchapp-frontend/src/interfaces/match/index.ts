import {IUserBasic} from "@/interfaces/user";
import MatchStatus from "@/enums/MatchStatus";
import {ISkill} from "@/interfaces/skill";

export interface IMatchBasic {
  id: number;
  userInfo1: IMatchUserInfoBasic;
  userInfo2: IMatchUserInfoBasic;
}

export interface IMatchUserInfoBasic {
  id: number;
  user: IUserBasic;
  status: MatchStatus;
  skillToLearn: ISkill;
}

export interface IMatchAccpet {
  secondaryUserId: number;
  skillToLearnId: number;
}

export interface IMatchReject {
  secondaryUserId: number;
}
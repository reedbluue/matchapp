import {baseHttp} from "@/configurations/AxiosConfig";
import Paths from "@/constants/Paths";
import {IMatchAccpet, IMatchBasic, IMatchReject} from "@/interfaces/match";

abstract class MatchService {
  static getAllAcceptedMatches() {
    return baseHttp.get<IMatchBasic[]>(Paths.ALL_MATCHES_ACCEPTED_PATH);
  }


  static acceptMatch(matchAcceptData: IMatchAccpet) {
    return baseHttp.post<void>(Paths.MATCH_ACCEPT_PATH, matchAcceptData);
  }

  static rejectMatch(matchRejectData: IMatchReject) {
    return baseHttp.post<void>(Paths.MATCH_REJECT_PATH, matchRejectData);
  }
}

export default MatchService;
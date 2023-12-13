import {IUserBasic} from "@/interfaces/user";
import {AiFillSound} from "react-icons/ai";

interface IMatchUserDisplayProps {
  user?: (IUserBasic & { imageBase64?: string });
}

const MatchUserDisplay = ({user}: IMatchUserDisplayProps) => {
  return (
      <div className={"flex-grow relative rounded-xl overflow-hidden"}>
        {user &&
            <div className={"absolute z-10 bottom-0 p-3"}>
              <h2 className={"m-0 badge"}>{user.fullName}</h2>
              <div className={"flex-none flex flex-col gap-0.5 m-0 p-0"}>
                <div className={"badge flex-col items-start h-min p-2 gap-2 rounded-xl"}>
                  <h4 className={"m-0"}>Para ensinar:</h4>
                  {user.userSkillSet.teachSkills &&
                      <div className={"flex flex-wrap gap-2"}>
                        {user.userSkillSet.teachSkills.map(
                            (ts) => <span key={ts.id + ts.name}
                                          className={"badge badge-info"}>{ts.name}</span>)}
                      </div>
                  }
                </div>

                <div className={"badge flex-col items-start h-min p-2 gap-2 rounded-xl"}>
                  <h4 className={"m-0"}>Para aprender:</h4>
                  {user.userSkillSet.learnSkills &&
                      <div className={"flex flex-wrap gap-2"}>
                        {user.userSkillSet.learnSkills.map(
                            (ts) => <span key={ts.id + ts.name}
                                          className={"badge badge-warning"}>{ts.name}</span>)}
                      </div>
                  }
                </div>
              </div>
            </div>
        }
        <div className={"top-0 absolute h-full w-full"}>
          {user?.imageBase64 &&
              <img src={user.imageBase64}
                   className={"m-0 object-cover h-full w-full"} alt="profile picture"/>
          }
          {!user &&
              <div className={"h-full w-full flex flex-col justify-center items-center"}>
                <AiFillSound size={38}/>
                <h3>Alguém por perto??!</h3>
              </div>
          }
        </div>
      </div>
  );
}

export default MatchUserDisplay;
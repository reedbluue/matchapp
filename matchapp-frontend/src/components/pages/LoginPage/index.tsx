import {useContext} from "react";
import {AxiosError} from "axios";
import toast from "react-hot-toast";
import {useForm} from "react-hook-form";
import AuthContext from "@/contexts/security/AuthContext";
import {IAuthRequest} from "@/interfaces/auth";
import {ISpringValidationError} from "@/interfaces/spring";

const LoginPage = () => {
  const {register, handleSubmit, formState: {errors}} = useForm<IAuthRequest>();
  const {login} = useContext(AuthContext);

  const submit = async (data: IAuthRequest) => {
    const err = await login(data) as AxiosError<ISpringValidationError>;
    if (err) {
      if (err.response?.status === 401) {
        toast.error("Email ou senha inválidos!");
      } else {
        toast.error("Algo deu errado, tente novamente mais tarde!");
      }
    } else {
      toast.success("Bem-vindo ao MatchApp!");
    }
  }

  return (
      <div className={"flex flex-col prose prose-sm flex-grow"}>
        <h1 className={"text-center flex-none"}>Entre no MatchApp</h1>
        <form onSubmit={handleSubmit(submit)}
              className={"form-control space-y-4 flex-1 flex-grow"}>
          <div className={"flex-none"}>
            <label className="label">
              <span className="text-base label-text">Email</span>
            </label>
            <input type="email" placeholder="Digite aqui o seu e-mail..." {...register("email", {
              required: "O email é obrigatório!",
              maxLength: {value: 100, message: "O tamanho máximo é de 100 caracteres!"},
            })} className={"w-full input input-bordered"}/>
            {errors.email && <div
                className="badge badge-outline badge-error w-full animate-pulse mt-2">{errors.email?.message}</div>}
          </div>

          <div className={"flex-none"}>
            <label className="label">
              <span className="text-base label-text">Senha</span>
            </label>
            <input type="password" placeholder="Digite aqui a sua senha..." {...register("password",
                {required: "A senha é obrigatória!"})}
                   className={"w-full input input-bordered"}/>
            {errors.password && <div
                className="badge badge-outline badge-error w-full animate-pulse mt-2">{errors.password?.message}</div>}
          </div>

          <div className={"flex-grow"}></div>

          <div className={"flex-none"}>
            <button type="submit" className={"btn btn-block text-base"}>Login</button>
          </div>
        </form>
      </div>
  )
}

export default LoginPage;
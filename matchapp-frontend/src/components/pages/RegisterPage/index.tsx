import {useContext, useEffect} from "react";
import {AxiosError} from "axios";
import toast from "react-hot-toast";
import {useForm} from "react-hook-form";
import AuthContext from "@/contexts/security/AuthContext";
import {IAuthRegister} from "@/interfaces/auth";
import {ISpringValidationError} from "@/interfaces/spring";
import DateUtil from "@/utils/DateUtil";
import TextUtil from "@/utils/TextUtil";

interface IRegisterFormData extends IAuthRegister {
  confirmPassword: string
}

const RegisterPage = () => {
  const {
    register,
    handleSubmit,
    formState: {errors, isSubmitting},
    getValues,
    trigger,
    watch
  } = useForm<IRegisterFormData>();
  const {register: registerFn} = useContext(AuthContext);
  const {password, confirmPassword, fullName} = watch();

  useEffect(() => {
    if (password) {
      trigger("password");
      trigger("confirmPassword");
    }
  }, [password]);

  useEffect(() => {
    if (confirmPassword) trigger("confirmPassword");
  }, [confirmPassword]);

  useEffect(() => {
    if (fullName) trigger("fullName");
  }, [fullName]);

  const submit = async (data: IRegisterFormData) => {
    if (isSubmitting) return;
    const err = await registerFn(data) as AxiosError<ISpringValidationError>;
    if (err) {
      if (err.response?.status === 400) {
        if (err.response.data.fieldsErrors) {
          for (const fieldError of err.response.data.fieldsErrors) {
            toast.error(fieldError.message);
          }
        } else {
          toast.error(err.response.data.message);
        }
      } else {
        toast.error("Algo deu errado, tente novamente mais tarde!");
      }
    } else {
      toast.success("Bem-vindo ao MatchApp!");
    }
  }

  return (
      <div className={"flex flex-col prose prose-sm flex-grow"}>
        <h1 className={"text-center flex-none"}>Registre-se no MatchApp!</h1>
        <form onSubmit={handleSubmit(submit)} className={"form-control space-y-4 flex-1 flex-grow"}>
          <div className={"flex-none"}>
            <label className="label">
              <span className="text-base label-text">Nome</span>
            </label>
            <input type="text" placeholder="Digite aqui o seu nome..." {...register("fullName", {
              required: "O nome é obrigatório!",
              maxLength: {value: 100, message: "O tamanho máximo é de 100 caracteres!"},
              minLength: {value: 3, message: "O nome deve ter pelo menos 3 caracteres!"},
            })} className={"w-full input input-bordered"}
                   onChange={async (event) => {
                     event.currentTarget.value =
                         TextUtil.processFullName((event.currentTarget.value as string));
                   }}
            />
            {errors.fullName && <div
                className="badge badge-outline badge-error w-full animate-pulse mt-2">{errors.fullName?.message}</div>}
          </div>

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
                {
                  required: "A senha é obrigatória!",
                  minLength: {value: 8, message: "A senha deve ter pelo menos 8 caracteres!"}
                })}
                   className={"w-full input input-bordered"}/>
            {errors.password && <div
                className="badge badge-outline badge-error w-full animate-pulse mt-2">{errors.password?.message}</div>}
          </div>

          <div className={"flex-none"}>
            <label className="label">
              <span className="text-base label-text">Confirme sua senha</span>
            </label>
            <input type="password" placeholder="Confirme aqui a sua senha..." {...register(
                "confirmPassword",
                {
                  required: "É obrigatório confirmar a senha!", validate: {
                    match: (value) => value === getValues("password") || "As senhas não conferem!",
                  }
                })} className={"w-full input input-bordered"}/>
            {errors.confirmPassword && <div
                className="badge badge-outline badge-error w-full animate-pulse mt-2">{errors.confirmPassword?.message}</div>}
          </div>

          <div className={"flex-none"}>
            <label className="label">
              <span className="text-base label-text">Data de nascimento</span>
            </label>
            <input type="date" {...register("birthDate", {
              required: "A data de nascimento é obrigatória!", validate: {
                min: (value) => DateUtil.checkIfIsAdult(new Date(value))
                    || "Deve ter pelo menos 18 anos!",
              }
            })} className={"w-full input input-bordered"}/>
            {errors.birthDate && <div
                className="badge badge-outline badge-error w-full animate-pulse mt-2">{errors.birthDate?.message}</div>}
          </div>

          <div className={"flex-1 flex-grow"}></div>

          <div className={"flex-none"}>
            <button type="submit" className={"btn btn-block text-base"}>{isSubmitting ? <span
                className={"loading loading-dots"}></span> : "Registrar"}</button>
          </div>
          <div className={"h-1/6"}></div>
        </form>
      </div>
  )
}

export default RegisterPage;
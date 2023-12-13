const HomePage = () => {
  return (
      <div className={"prose flex-grow flex flex-col items-center justify-center"}>
        <div className={"flex-grow flex flex-col gap-4 justify-center"}>
          <h1 className={"m-0 text-center"}>Bem-vindo ao MathApp!</h1>
          <h2 className={"m-0 text-center"}>Compartilhe e aprenda enquanto faz novas amizades!</h2>
        </div>
        <img alt={"home art"} src={"/assets/home-art.svg"} className={"w-full"}/>

      </div>
  );
}

export default HomePage;
import daisyui from "daisyui";
import typography from "@tailwindcss/typography";
import tailwindAnimated from "tailwindcss-animated";

/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [tailwindAnimated, typography, daisyui],
}


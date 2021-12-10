import { createContext } from "react";
import AuthContextType from "./AuthContextType";

let AuthContext = createContext<AuthContextType>(null!);


export default AuthContext;
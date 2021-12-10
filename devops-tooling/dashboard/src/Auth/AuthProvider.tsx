import { useState } from "react";
import AuthContext from "./AuthContext";
import AuthService from "./AuthService";

 
export default function AuthProvider({ children }: { children: React.ReactNode }) {
    let [user, setUser] = useState<any>(null);
  
    let signin = (newUser: string, callback: VoidFunction) => {
      console.log('logged in');
      setUser(newUser);
      AuthService.signIn(newUser);
      callback();
    };
  
    let signout = (callback: VoidFunction) => {
       AuthService.signOut();
       window.location.href = '/';
       callback();
    };
  
    let value = { user, signin, signout };
  
    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
  }
  
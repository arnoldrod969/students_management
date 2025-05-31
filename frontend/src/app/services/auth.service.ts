import { Injectable } from '@angular/core';
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  public users:any = {
    'arnoldrod9696@gmail.com' : { password: '1234', role: ['admin','student'] },
    'rodrigueken969@gmail.com' : { password: '1234', role: ['student'] },
  }

  public email: any;
  public isAuthenticated: boolean=false;
  public roles : string[]=[];

  constructor( private router : Router) { }

  public login(email:string, password:string) : boolean {

    if (this.users[email] && this.users[email].password === password) {
      this.email = email;
      this.isAuthenticated = true;
      this.roles = this.users[email]['role'];
      return true;
    }
    return false;

  }

  logout() {
    this.email = null;
    this.isAuthenticated = false;
    this.roles = [];
    this.router.navigateByUrl('/login');
  }
}

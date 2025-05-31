import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginForm: FormGroup;

  constructor(private fb: FormBuilder , private authService : AuthService , private router : Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      console.log('Formulaire soumis', this.loginForm.value);
      this.login();
      // Ajoutez ici la logique d'authentification
    }
  }

  login() {

    let email = this.loginForm.value.email;
    let password = this.loginForm.value.password;
    let auth:boolean = this.authService.login(email, password);
    if (auth) {
      this.router.navigate(['/admin']);
    }else {
      alert('Email ou mot de passe incorrect');
    }
  }
}

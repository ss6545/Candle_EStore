import { Component, OnInit } from '@angular/core';
import { AppComponent } from '../app.component';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { User } from '../user';
import { UserService } from '../user.service';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  users: User[] | undefined;
  user: User | undefined;
  errorMessage: string | undefined;

  constructor( private userService: UserService,
              private loginService: LoginService,
              private router: Router ) { }

  ngOnInit() {  
    this.userService.getUsers().subscribe({
      next: (users) => {this.users = users;},
      error: (e) => {
        console.error("Check estore-api: "+e);
      }
    });
  }

  checkLogin(username: string, password: string): void {
    
    if (this.users == undefined) {
      return;
    }
    if (username == "") {
      this.errorMessage = "Please enter your username.";
      return;
    }

    this.user = undefined;
    for(let i = 0; i < this.users.length; i++) {
      if(this.users[i].name == username) {
        this.user = this.users[i];
      }
    }

    if (this.user == undefined) {
      let toUrl = "/user-signup/"+username;
      this.router.navigate([toUrl]);
      return;
    }
      
    if (password == this.user.password) {
      this.loginService.setCurrentUser(this.user);
      if (this.user.isAdmin)
      {
        this.router.navigate(['/candles']);
      }
      else {
        this.router.navigate(['/user-dashboard']);
      }
    } else {
      this.errorMessage = "Please check your password.";
    }
  }
}
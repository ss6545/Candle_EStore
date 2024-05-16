import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { LoginService } from '../login.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user-signup',
  templateUrl: './user-signup.component.html',
  styleUrl: './user-signup.component.css'
})
export class UserSignupComponent implements OnInit {
  providedUsername: string | null | undefined;
  users: User[] | undefined;
  errorMessage: string | undefined;

  containsNumber: RegExp;
  containsCapital: RegExp;
  containsLowercase: RegExp;
  containsSpecial: RegExp;
  tooShort: boolean | undefined;
  tooLong: boolean | undefined;
  missingNumber: boolean | undefined;
  missingCapital: boolean | undefined;
  missingLowercase: boolean | undefined;
  missingSpecial: boolean | undefined;

  constructor(
    private userService: UserService,
    private loginService: LoginService,
    private router: Router,
    private route: ActivatedRoute 
  ) {
    this.containsNumber = new RegExp("[0-9]");
    this.containsCapital = new RegExp("[A-Z]");
    this.containsLowercase = new RegExp("[a-z]");
    this.containsSpecial = new RegExp("[\!\@\&\#\$\%\^\&\*\+\=]");
  }

  ngOnInit() {
    this.userService.getUsers().subscribe({
      next: (users) => {this.users = users;},
      error: (e) => {
        console.error("Check estore-api: "+e);
      }
    });
    this.providedUsername = this.route.snapshot.paramMap.get('username');
  }

  checkSignup(username: string, password: string): void {
    if (this.users == undefined) {return;}
    if (!this.validUsername(username)) {return;}
    if (!this.validPassword(password)) {return;}
    
    let newUser:User = {
      id: 0,
      name: username,
      cart: [],
      isAdmin: false,
      password: password,
      creditCard: null
    }
    this.userService.addUser(newUser)
      .subscribe(user => {
        this.loginService.setCurrentUser(user);
      });
      this.router.navigate(['/user-dashboard']);
  }

  validUsername(username: string): boolean {
    this.errorMessage = "";
    if (this.users == undefined) {return false;}
    if (username == "") {
      this.errorMessage = "Please enter a username.";
      return false;
    }
    for(let i = 0; i < this.users.length; i++) {
      if(this.users[i].name == username) {
        this.errorMessage = "Username is already taken. Please choose a different username.";
        return false;
      }
    }
    return true;
  }

  validPassword(password: string): boolean {
    if (this.users == undefined) {return false;}
    
    this.tooShort = true;
    this.tooLong = true;
    this.missingNumber = true;
    this.missingCapital = true;
    this.missingLowercase = true;
    this.missingSpecial = true;

    if (password.length >= 8) {
      this.tooShort = false;
    }
    if (password.length <= 32) {
      this.tooLong = false;
    }
    if (this.containsNumber.test(password)) {
      this.missingNumber = false;
    }
    if (this.containsCapital.test(password)) {
      this.missingCapital = false;
    }
    if (this.containsLowercase.test(password)) {
      this.missingLowercase = false;
    }
    if (this.containsSpecial.test(password)) {
      this.missingSpecial = false;
    }
    return !(this.tooShort||this.tooLong||this.missingNumber||this.missingCapital||this.missingLowercase||this.missingSpecial);
  }
}

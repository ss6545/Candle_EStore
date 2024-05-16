import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../user';
import { UserService } from '../user.service';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-purchase-successful',
  templateUrl: './purchase-successful.component.html',
  styleUrl: './purchase-successful.component.css'
})

export class PurchaseSuccessfulComponent {
  user: User | undefined;

  constructor(private userService: UserService,
              private loginService: LoginService,
              private router: Router) { }

  ngOnInit(): void {
    this.getUser();
  }

  getUser(): void {
    const userId = this.loginService.getCurrentUser().id;
    this.userService.getUser(userId)
      .subscribe(user => {
        this.user = user;
      });
  }

  goToCart(): void {
    this.router.navigate(['../shopping-cart']);
  }

  goToDashboard(): void {
    this.router.navigate(['../user-dashboard']);
  }
}

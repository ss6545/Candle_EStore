import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

import { Candle } from '../candle';
import { CandleService } from '../candle.service';
import { UserService } from '../user.service';
import { User } from '../user';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-candle-detail',
  templateUrl: './candle-detail.component.html',
  styleUrls: [ './candle-detail.component.css' ]
})

export class CandleDetailComponent implements OnInit {
  candle: Candle | undefined;
  user: User | undefined;
  errorMessage: String = '';
  

  constructor(
    private route: ActivatedRoute,
    private candleService: CandleService,
    private userService: UserService,
    private location: Location,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getCandle();
    this.getUser();
  }

  getCandle(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.candleService.getCandle(id)
      .subscribe(candle => {
        this.candle = candle;
      });
  }

  getUser(): void {
    const userId = this.loginService.getCurrentUser().id;
    this.userService.getUser(userId)
      .subscribe(user => {
        this.user = user;
      });
  }

  goBack(): void {
    this.location.back();
  }

  addProduct(candle: Candle): void {
    this.errorMessage = '';
    if(!this.user || candle.quantity == 0) {
      this.errorMessage = 'Product is out of stock.';
      return;
    }
    for(let i = 0; i < this.user.cart.length; i++) {
      if(this.user.cart[i].id == candle.id) {
        this.errorMessage = 'Product is already in your cart. Please update the quantity in your cart to add more.';
        return;
      }
    }
    let myCandle = candle;
    myCandle.quantity = 1;
    this.user.cart.push(myCandle);
    this.userService.updateUser(this.user).subscribe(() => {this.router.navigate(['../shopping-cart']);});
  }
}
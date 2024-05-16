import { Component, OnInit } from '@angular/core';
import { Candle } from '../candle';

import { User } from '../user';
import { UserService } from '../user.service';
import { LoginService } from '../login.service';
import { Router } from '@angular/router';
import { CandleService } from '../candle.service';
import { Observable, forkJoin } from 'rxjs';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.css'
})

export class ShoppingCartComponent implements OnInit {
  user: User | undefined;
  errorMessage: String = '';

  constructor(private userService: UserService,
              private loginService: LoginService,
              private candleService: CandleService,
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

  removeProduct(id: number): void {
    if(!this.user) {
      return;
    }
    for(let i = 0; i < this.user.cart.length; i++) {
      if(this.user.cart[i].id == id) {
        this.user.cart.splice(i, 1);
        this.userService.updateUser(this.user).subscribe(() => {});
      }
    }
  }

  updateProduct(candle: Candle, quantityInput: HTMLInputElement): void {
    if(!this.user) {
      return;
    }
    var quantity = parseInt(quantityInput.value);
    if(isNaN(quantity)) {
      return;
    }
    
    let observableProduct: Observable<Candle> = this.candleService.getCandle(candle.id);
    observableProduct.subscribe((product: Candle) => {
      if(quantity > product.quantity) {
        this.errorMessage = "Not enough of this product in stock."
      } else {
        if(this.user) {
          for(let i = 0; i < this.user.cart.length; i++) {
            if(this.user.cart[i].id == candle.id) {
              this.user.cart[i].quantity = quantity;
              this.userService.updateUser(this.user).subscribe(() => {});
            }
          }
        }
      }
    });
    this.errorMessage = '';
  }

  checkout(): void {
    if(this.user) {
      let error = false;
      const observables = this.user.cart.map((userCandle: Candle) => {
        return this.candleService.getCandle(userCandle.id);
      });
  
      forkJoin(observables).subscribe((candles: Candle[]) => {
        candles.forEach((candle: Candle, index: number) => {
          if(this.user) {
            const userCandle = this.user.cart[index];
            if(userCandle.quantity > candle.quantity) {
              console.log(userCandle.id);
              error = true;
            }
          }
        });
  
        if(error) {
          this.errorMessage = "There is not enough inventory for an item in your cart. Please check what is in stock.";
        } else {
          this.router.navigate(["../checkout"]);
        }
      });
      this.errorMessage = '';
      error = false;
    }
  }
}

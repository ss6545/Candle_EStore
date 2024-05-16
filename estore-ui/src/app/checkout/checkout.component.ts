import { Component } from '@angular/core';
import { UserService } from '../user.service';
import { LoginService } from '../login.service';
import { Router } from '@angular/router';
import { User } from '../user';
import { Candle } from '../candle';
import { CreditCard } from '../credit-card';
import { CandleService } from '../candle.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent {
  user: User | undefined;
  showCreditCardSection: boolean = false;
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

  getTotalPrice(cart: Candle[]): number {
    let total = 0;
    for(let i = 0; i < cart.length; i++) {
      total += (cart[i].price * cart[i].quantity);
    }
    return total;
  }

  toggleCreditCardSection() {
    this.showCreditCardSection = !this.showCreditCardSection;
  }

  editCard(cardNumber:string, cardHolderName:String, expiryDate:string, secCode:string): void {
    this.errorMessage = '';
    if(!this.user) {
      return;
    }
    
    if(isNaN(parseInt(cardNumber)) || cardNumber.length != 16) {
      this.errorMessage = "Card number should be a 16 digit number";
      return;
    }

    if(isNaN(parseInt(secCode)) || secCode.length != 3) {
      this.errorMessage = "Security code should be a 3 digit number";
      return;
    }

    if(isNaN(parseInt(expiryDate)) || expiryDate.length != 4) {
      this.errorMessage = "Expiration date should be 4 digits (MMYY)";
      return;
    }

    if(cardHolderName.length <= 0) {
      this.errorMessage = "Please enter a name for your card";
      return;
    }

    let newCreditCard:CreditCard = {
      cardHolderName: cardHolderName,
      cardNumber: cardNumber,
      expiryDate: expiryDate,
      secCode: secCode
    }
    
    this.user.creditCard = newCreditCard;
    this.userService.updateUser(this.user).subscribe(() => {});
  }

  purchase(): void {
    this.errorMessage = '';
    if(this.user) {
      
      if(!this.user.creditCard) {
        this.errorMessage = 'Please enter your credit card information to purchase your cart';
        return;
      }

      const cardNumber = this.user.creditCard.cardNumber.toString();
      const secCode = this.user.creditCard.secCode.toString();
      const expiryDate = this.user.creditCard.expiryDate.toString();
      const cardHolderName = this.user.creditCard.cardHolderName;

      if (isNaN(parseInt(cardNumber)) || cardNumber.length !== 16 ||
        isNaN(parseInt(secCode)) || secCode.length !== 3 ||
        isNaN(parseInt(expiryDate)) || expiryDate.length !== 4 ||
        cardHolderName.trim().length === 0) {
        this.errorMessage = 'Please ensure your credit card information is correct';
        return;
      }

      this.userService.getUser(this.user.id).subscribe((currentUser: User) => {
        let shoppingCart: Candle[] = currentUser.cart;
        shoppingCart.forEach((userCandle: Candle) => {
          this.candleService.getCandle(userCandle.id).subscribe((candle: Candle) => {
            candle.quantity -= userCandle.quantity;
            this.candleService.updateCandle(candle).subscribe(() => {});
          });
        });
      });
      this.user.cart = [];
      this.userService.updateUser(this.user).subscribe(() => {});
      this.router.navigate(['../purchase-successful']);
    }
  }
}

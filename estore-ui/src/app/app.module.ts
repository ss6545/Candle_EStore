import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CandleDetailComponent } from './candle-detail/candle-detail.component';
import { CandlesComponent } from './candles/candles.component';
import { CandleSearchComponent } from './candle-search/candle-search.component';
import { MessagesComponent } from './messages/messages.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { LoginComponent } from './login/login.component';
import { HeaderComponent } from './header/header.component';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import { UserHeaderComponent } from './user-header/user-header.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { AdminDetailComponent } from './admin-detail/admin-detail.component';
import { PurchaseSuccessfulComponent } from './purchase-successful/purchase-successful.component';
import { UserSignupComponent } from './user-signup/user-signup.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  declarations: [
    AppComponent,
    DashboardComponent,
    CandlesComponent,
    CandleDetailComponent,
    MessagesComponent,
    CandleSearchComponent,
    ShoppingCartComponent,
    LoginComponent,
    HeaderComponent,
    UserDashboardComponent,
    UserHeaderComponent,
    CheckoutComponent,
    AdminDetailComponent,
    PurchaseSuccessfulComponent,
    UserSignupComponent
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
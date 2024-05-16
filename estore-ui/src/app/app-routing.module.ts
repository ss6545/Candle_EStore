import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CandlesComponent } from './candles/candles.component';
import { CandleDetailComponent } from './candle-detail/candle-detail.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { LoginComponent } from './login/login.component';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { AdminDetailComponent } from './admin-detail/admin-detail.component';
import { PurchaseSuccessfulComponent } from './purchase-successful/purchase-successful.component';
import { UserSignupComponent } from './user-signup/user-signup.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'detail/:id', component: CandleDetailComponent },
  { path: 'candles', component: CandlesComponent },
  { path: 'shopping-cart/:id', component: ShoppingCartComponent},
  { path: 'shopping-cart', component: ShoppingCartComponent},
  { path: 'login', component: LoginComponent },
  { path: 'user-dashboard', component: UserDashboardComponent},
  { path: 'checkout', component: CheckoutComponent},
  { path: 'admin-detail/:id', component: AdminDetailComponent},
  { path: 'purchase-successful', component: PurchaseSuccessfulComponent},
  { path: 'user-signup', component: UserSignupComponent},
  { path: 'user-signup/:username', component: UserSignupComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
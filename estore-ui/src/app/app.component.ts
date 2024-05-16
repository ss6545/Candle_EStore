import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'Candle E-Store';
  public isAdmin: boolean = false;

  constructor(private router: Router) {}

  ngOnInit() {
    this.setAdmin();
    //Some backend call to check if the current user is an admin
  }

  public setAdmin() {
    
  }
}
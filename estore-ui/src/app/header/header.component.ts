import { Component, OnChanges, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  constructor( private router: Router ) { }
  logOff() {
    this.router.navigate(['/login']);
  }
}

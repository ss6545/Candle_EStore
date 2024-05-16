import { Component, OnInit } from '@angular/core';
import { Candle } from '../candle';
import { CandleService } from '../candle.service';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: [ './user-dashboard.component.css' ]
})

export class UserDashboardComponent implements OnInit {
  candles: Candle[] = [];

  constructor(private candleService: CandleService) { }

  ngOnInit(): void {
    this.getCandles();
  }

  getCandles(): void {
    this.candleService.getCandles()
      .subscribe(candles => this.candles = candles);
  }
}
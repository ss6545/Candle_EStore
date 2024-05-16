import { Component, OnInit } from '@angular/core';
import { Candle } from '../candle';
import { CandleService } from '../candle.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})

export class DashboardComponent implements OnInit {
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
import { Component, OnInit } from '@angular/core';

import { Candle } from '../candle';
import { CandleService } from '../candle.service';

@Component({
  selector: 'app-candles',
  templateUrl: './candles.component.html',
  styleUrls: ['./candles.component.css']
})
export class CandlesComponent implements OnInit {
  candles: Candle[] = [];

  constructor(private candleService: CandleService) { }

  ngOnInit(): void {
    this.getCandles();
  }

  getCandles(): void {
    this.candleService.getCandles()
    .subscribe(candles => this.candles = candles);
  }

  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    let newCandle:Candle = {
      id: 0,
      name: name,
      price: 10,
      quantity: 0
    }
    this.candleService.addCandle(newCandle)
      .subscribe(candle => {
        this.candles.push(candle);
      });
  }

  delete(candle: Candle): void {
    this.candles = this.candles.filter(h => h !== candle);
    this.candleService.deleteCandle(candle.id).subscribe();
  }

}
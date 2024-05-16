import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CandleService } from '../candle.service';
import { Candle } from '../candle';

@Component({
  selector: 'app-admin-detail',
  templateUrl: './admin-detail.component.html',
  styleUrl: './admin-detail.component.css'
})
export class AdminDetailComponent {
  candle: Candle | undefined;

  constructor(
    private route: ActivatedRoute,
    private candleService: CandleService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getCandle();
  }

  getCandle(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.candleService.getCandle(id)
      .subscribe(candle => {
        this.candle = candle;
      });
  }

  editName(name: string): void {
    if(this.candle) {
      this.candle.name = name;
      this.candleService.updateCandle(this.candle).subscribe();
      this.router.navigate(['../candles']);
    }
  }

  editPrice(price: number): void {
    if(this.candle && !isNaN(price) && price >= 0) {
      this.candle.price = price;
      this.candleService.updateCandle(this.candle).subscribe();
      this.router.navigate(['../candles']);
    }
  }

  editQuantity(quantity: number): void {
    if(this.candle && !isNaN(quantity) && quantity >= 0) {
      this.candle.quantity = quantity;
      this.candleService.updateCandle(this.candle).subscribe();
      this.router.navigate(['../candles']);
    }
  }
}

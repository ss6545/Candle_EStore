import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
   debounceTime, distinctUntilChanged, switchMap
 } from 'rxjs/operators';

import { Candle } from '../candle';
import { CandleService } from '../candle.service';

@Component({
  selector: 'app-candle-search',
  templateUrl: './candle-search.component.html',
  styleUrls: [ './candle-search.component.css' ]
})
export class CandleSearchComponent implements OnInit {
  candles$!: Observable<Candle[]>;
  private searchTerms = new Subject<string>();

  constructor(private candleService: CandleService) {}

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    this.candles$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.candleService.searchCandles(term)),
    );
  }
}
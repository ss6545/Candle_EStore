import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';


import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';


import { Candle } from './candle';
import { MessageService } from './message.service';




@Injectable({ providedIn: 'root' })
export class CandleService {


  private candlesUrl = 'http://localhost:8080/candles';


  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };


  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }


  /** GET candles from the server */
  getCandles(): Observable<Candle[]> {
    return this.http.get<Candle[]>(this.candlesUrl)
      .pipe(
        tap(_ => this.log('fetched candles')),
        catchError(this.handleError<Candle[]>('getCandles', []))
      );
  }


  /** GET candle by id. Return `undefined` when id not found */
  getCandleNo404<Data>(id: number): Observable<Candle> {
    const url = `${this.candlesUrl}/?id=${id}`;
    return this.http.get<Candle[]>(url)
      .pipe(
        map(candles => candles[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? 'fetched' : 'did not find';
          this.log(`${outcome} candle id=${id}`);
        }),
        catchError(this.handleError<Candle>(`getCandle id=${id}`))
      );
  }


  /** GET candle by id. Will 404 if id not found */
  getCandle(id: number): Observable<Candle> {
    const url = `${this.candlesUrl}/${id}`;
    return this.http.get<Candle>(url).pipe(
      tap(_ => this.log(`fetched candle id=${id}`)),
      catchError(this.handleError<Candle>(`getCandle id=${id}`))
    );
  }


  /* GET candles whose name contains search term */
  searchCandles(term: string): Observable<Candle[]> {
    if (!term.trim()) {
      // if not search term, return empty candle array.
      return of([]);
    }
    return this.http.get<Candle[]>(`${this.candlesUrl}/?name=${term}`).pipe(
      tap(x => x.length ?
         this.log(`found candles matching "${term}"`) :
         this.log(`no candles matching "${term}"`)),
      catchError(this.handleError<Candle[]>('searchCandles', []))
    );
  }


  //////// Save methods //////////


  /** POST: add a new candle to the server */
  addCandle(candle: Candle): Observable<Candle> {
    return this.http.post<Candle>(this.candlesUrl, candle, this.httpOptions).pipe(
      tap((newCandle: Candle) => this.log(`added candle w/ id=${newCandle.id}`)),
      catchError(this.handleError<Candle>('addCandle'))
    );
  }


  /** DELETE: delete the candle from the server */
  deleteCandle(id: number): Observable<Candle> {
    const url = `${this.candlesUrl}/${id}`;


    return this.http.delete<Candle>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted candle id=${id}`)),
      catchError(this.handleError<Candle>('deleteCandle'))
    );
  }


  /** PUT: update the candle on the server */
  updateCandle(candle: Candle): Observable<any> {
    return this.http.put(this.candlesUrl, candle, this.httpOptions).pipe(
      tap(_ => this.log(`updated candle id=${candle.id}`)),
      catchError(this.handleError<any>('updateCandle'))
    );
  }


  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {


      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead


      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);


      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }


  /** Log a CandleService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`CandleService: ${message}`);
  }
}

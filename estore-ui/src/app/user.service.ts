import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, forkJoin, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { User } from './user';
import { MessageService } from './message.service';
import { Candle } from './candle';
import { CreditCard } from './credit-card';

@Injectable({ providedIn: 'root' })
export class UserService {

  private usersUrl = 'http://localhost:8080/users';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }
  
  /** Shopping Cart Updating */
  addProduct(candle: Candle): Observable<Candle[]> {
    return this.http.post<Candle[]>(this.usersUrl, candle, this.httpOptions)
    .pipe(
      tap(_ => this.log('added product')),
      catchError(this.handleError<Candle[]>('addProduct', []))
    );
  }

  removeProduct(id: number): Observable<Candle[]> {
    const url = `${this.usersUrl}/${id}`;
    return this.http.delete<Candle[]>(url, this.httpOptions)
    .pipe(
      tap(_ => this.log(`removed product id=${id}`)),
      catchError(this.handleError<Candle[]>('removeProduct', []))
    );
  }

  updateProduct(candle: Candle): Observable<Candle[]> {
    const url = `${this.usersUrl}/${candle.id}`;
    return this.http.put<Candle[]>(url, candle, this.httpOptions)
    .pipe(
      tap(_ => this.log(`updated product id=${candle.id}`)),
      catchError(this.handleError<Candle[]>('updateProduct', []))
    );
  }
  
  /** GET users from the server */
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl)
      .pipe(
        tap(_ => this.log('fetched users')),
        catchError(this.handleError<User[]>('getUsers', []))
      );
  }
  
  /** GET user by id. Return `undefined` when id not found */
  getUserNo404<Data>(id: number): Observable<User> {
    const url = `${this.usersUrl}/?id=${id}`;
    return this.http.get<User[]>(url)
      .pipe(
        map(users => users[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? 'fetched' : 'did not find';
          this.log(`${outcome} user id=${id}`);
        }),
        catchError(this.handleError<User>(`getUser id=${id}`))
      );
  }
  
  /** GET user by id. Will 404 if id not found */
  getUser(id: number): Observable<User> {
    const url = `${this.usersUrl}/${id}`;
    return this.http.get<User>(url).pipe(
      tap(_ => this.log(`fetched user id=${id}`)),
      catchError(this.handleError<User>(`getUser id=${id}`))
    );
  }

  /* GET users whose name contains search term */
  searchUsers(term: string): Observable<User[]> {
    if (!term.trim()) {
      // if not search term, return empty user array.
      return of([]);
    }
    return this.http.get<User[]>(`${this.usersUrl}/?name=${term}`).pipe(
      tap(x => x.length ?
         this.log(`found users matching "${term}"`) :
         this.log(`no users matching "${term}"`)),
      catchError(this.handleError<User[]>('searchUsers', []))
    );
  }

  //////// Save methods //////////

  /** POST: add a new user to the server */
  addUser(user: User): Observable<User> {
    return this.http.post<User>(this.usersUrl, user, this.httpOptions).pipe(
      tap((newUser: User) => this.log(`added user w/ id=${newUser.id}`)),
      catchError(this.handleError<User>('addUser'))
    );
  }

  /** POST: add a new credit card to the user's data */
  addCreditCard(creditCard:CreditCard): Observable<CreditCard>{
    return this.http.post<CreditCard>(this.usersUrl, creditCard, this.httpOptions).pipe(
      tap(_ => this.log(`added credit card`)),
      catchError(this.handleError<CreditCard>('addCreditCard'))
    );
  }

  /** DELETE: delete the user from the server */
  deleteUser(id: number): Observable<User> {
    const url = `${this.usersUrl}/${id}`;
    return this.http.delete<User>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted user id=${id}`)),
      catchError(this.handleError<User>('deleteUser'))
    );
  }

  /** PUT: update the user on the server */
  updateUser(user: User): Observable<any> {
    return this.http.put(this.usersUrl, user, this.httpOptions).pipe(
      tap(_ => this.log(`updated user id=${user.id}`)),
      catchError(this.handleError<any>('updateUser'))
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
    this.messageService.add(`UserService: ${message}`);
  }
}

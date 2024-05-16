import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserDataService {
  username: String | undefined;
  userId: number | undefined;

  constructor() { }

  getUserId() {
    return this.userId;
  }

  setUserId(userId: number) {
    this.userId = userId;
  }

  getUsername() {
    return this.username;
  }

  setUsername(username: String) {
    this.username = username;
  }
}

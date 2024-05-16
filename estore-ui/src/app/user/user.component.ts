import { Component, OnInit } from '@angular/core';

import { User } from '../user'
import { UserService } from '../user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  users: User[] = [];

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers(): void {
    this.userService.getUsers()
    .subscribe(users => this.users = users);
  }

  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.userService.addUser({ name } as unknown as User)
      .subscribe(user => {
        this.users.push(user);
      });
  }

  delete(user: User): void {
    this.users = this.users.filter(h => h !== user);
    this.userService.deleteUser(user.id).subscribe();
  }
}
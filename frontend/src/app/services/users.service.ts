import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface CreateUserRequest {
  name: string;
  email: string;
  password: string;
}

@Injectable({ providedIn: 'root' })
export class UsersService {
  constructor(private http: HttpClient) {}

  createUser(request: CreateUserRequest) {
    return this.http.post('/api/users', request);
  }
}

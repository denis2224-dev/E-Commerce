import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface CreateUserRequest {
  name: string;
  email: string;
  password: string;
}

export interface CurrentUser {
  id: number;
  name: string;
  email: string;
  phoneNumber?: string | null;
  createdAt: string;
}

export interface UpdateCurrentUserRequest {
  name: string;
  email: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}

@Injectable({ providedIn: 'root' })
export class UsersService {
  constructor(private http: HttpClient) {}

  createUser(request: CreateUserRequest) {
    return this.http.post('/api/users', request);
  }

  getCurrentUser() {
    return this.http.get<CurrentUser>('/api/me');
  }

  updateCurrentUser(request: UpdateCurrentUserRequest) {
    return this.http.put<CurrentUser>('/api/me', request);
  }

  changeCurrentUserPassword(request: ChangePasswordRequest) {
    return this.http.post<void>('/api/me/change-password', request);
  }

  deleteCurrentUser() {
    return this.http.delete<void>('/api/me');
  }
}

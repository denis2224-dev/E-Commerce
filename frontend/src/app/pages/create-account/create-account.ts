import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize, timeout } from 'rxjs';
import { Users } from '../../services/users';

@Component({
  selector: 'app-create-account',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './create-account.html',
  styleUrl: './create-account.scss',
})
export class CreateAccount {
  isSubmitting = false;
  errorMessage = '';

  createAccountForm;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private users: Users,
  ) {
    this.createAccountForm = this.formBuilder.nonNullable.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required],
      terms: [false, Validators.requiredTrue],
    });
  }

  submit() {
    this.errorMessage = '';
    this.createAccountForm.markAllAsTouched();

    const formValue = this.createAccountForm.getRawValue();
    if (formValue.password !== formValue.confirmPassword) {
      this.createAccountForm.controls.confirmPassword.setErrors({ passwordMismatch: true });
      return;
    }

    if (this.createAccountForm.invalid) {
      return;
    }

    this.isSubmitting = true;
    this.users
      .createUser({
        name: formValue.name,
        email: formValue.email,
        password: formValue.password,
      })
      .pipe(
        timeout(10000),
        finalize(() => (this.isSubmitting = false)),
      )
      .subscribe({
        next: () => {
          this.createAccountForm.reset();
          this.router.navigateByUrl('/sign-in');
        },
        error: (error: HttpErrorResponse) => {
          this.errorMessage =
            error.status === 409
              ? 'An account with this email already exists.'
              : 'Could not create your account. Please try again.';
        },
      });
  }
}

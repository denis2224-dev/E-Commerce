import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize, timeout } from 'rxjs';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-sign-in',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './sign-in.html',
  styleUrl: './sign-in.scss',
})
export class SignIn {
  isSubmitting = false;
  errorMessage = '';

  signInForm;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService,
  ) {
    this.signInForm = this.formBuilder.nonNullable.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      remember: [true],
    });
  }

  submit() {
    this.errorMessage = '';
    this.signInForm.markAllAsTouched();

    if (this.signInForm.invalid) {
      return;
    }

    const formValue = this.signInForm.getRawValue();

    this.isSubmitting = true;
    this.authService
      .login({
        email: formValue.email,
        password: formValue.password,
      })
      .pipe(
        timeout(10000),
        finalize(() => (this.isSubmitting = false)),
      )
      .subscribe({
        next: (response) => {
          this.authService.saveToken(response.token, formValue.remember);
          this.signInForm.reset({ email: '', password: '', remember: true });
          this.router.navigateByUrl('/account');
        },
        error: (error: HttpErrorResponse) => {
          this.errorMessage =
            error.status === 401
              ? 'Invalid email or password.'
              : 'Could not sign in. Please try again.';
        },
      });
  }
}

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResetPasswordLayoutComponent } from './reset-password-layout.component';

describe('ResetPasswordLayoutComponent', () => {
  let component: ResetPasswordLayoutComponent;
  let fixture: ComponentFixture<ResetPasswordLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResetPasswordLayoutComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ResetPasswordLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

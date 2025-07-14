import { ComponentFixture, TestBed } from '@angular/core/testing';

import {ResetsenhaComponent } from './reset-senha.component';

describe('LoginComponent', () => {
  let component: ResetsenhaComponent;
  let fixture: ComponentFixture<ResetsenhaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResetsenhaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResetsenhaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

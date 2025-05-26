import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCreateModalComponent } from './user-create-modal.component';

describe('UserCreateModalComponent', () => {
  let component: UserCreateModalComponent;
  let fixture: ComponentFixture<UserCreateModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserCreateModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserCreateModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

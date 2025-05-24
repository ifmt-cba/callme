import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserlistersComponent } from './userlisters.component';

describe('UserlistersComponent', () => {
  let component: UserlistersComponent;
  let fixture: ComponentFixture<UserlistersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserlistersComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserlistersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

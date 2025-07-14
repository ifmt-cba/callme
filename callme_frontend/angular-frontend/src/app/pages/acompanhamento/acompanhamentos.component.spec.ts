import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcompanhamentosComponent } from './acompanhamentos.component';

describe('AcompanhamentoComponent', () => {
  let component: AcompanhamentosComponent;
  let fixture: ComponentFixture<AcompanhamentosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AcompanhamentosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AcompanhamentosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
